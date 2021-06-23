package mvc.controller;

import mvc.dao.CiudadanoDao;
import mvc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

class CiudadanoValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Ciudadano.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        Ciudadano ciudadano = (Ciudadano) obj;
        if (ciudadano.getNombre().trim().equals(""))
            errors.rejectValue("nombre", "obligatorio",
                    "Campo obligatorio");

        if (ciudadano.getEdad()<18)
            errors.rejectValue("edad", "obligatorio",
                    "Edad mínima 18 años");

        if (ciudadano.getDni().trim().equals("USADO"))
            errors.rejectValue("dni", "obligatorio",
                    "Ya existe un usuario registrado con este dni");

        if (ciudadano.getDni().trim().equals(""))
            errors.rejectValue("dni", "obligatorio",
                    "Campo obligatorio");

        if (ciudadano.getTelefono().trim().equals(""))
            errors.rejectValue("telefono", "obligatorio",
                    "Campo obligatorio");

        if (ciudadano.getCodPostal().trim().equals(""))
            errors.rejectValue("codPostal", "obligatorio",
                    "Campo obligatorio");

        if (ciudadano.getDireccion().trim().equals(""))
            errors.rejectValue("direccion", "obligatorio",
                    "Campo obligatorio");

        if (ciudadano.getEmail().trim().equals(""))
            errors.rejectValue("email", "obligatorio",
                    "Campo obligatorio");

        if (ciudadano.getCiudad().trim().equals(""))
            errors.rejectValue("ciudad", "obligatorio",
                    "Campo obligatorio");

        if (ciudadano.getPin().trim().equals(""))
            errors.rejectValue("pin", "obligatorio",
                    "Campo obligatorio");
    }
}

@Controller
@RequestMapping("/ciudadano")
public class CiudadanoController {

    private CiudadanoDao ciudadanoDao;

    @Autowired
    public void setCiudadanoDao(CiudadanoDao ciudadanoDao) {
        this.ciudadanoDao=ciudadanoDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    @RequestMapping("/indice")
    public String indice() {
        return "ciudadano/indice";
    }

    @RequestMapping("/list")
    public String listCiudadanos(Model model) {
        model.addAttribute("ciudadanos", ciudadanoDao.getCiudadanos());
        return "ciudadano/list";
    }

    @RequestMapping(value="/add")
    public String addCiudadano(Model model) {
        model.addAttribute("ciudadano", new Ciudadano());
        return "ciudadano/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("ciudadano") Ciudadano ciudadano, BindingResult bindingResult, HttpSession session){

        //Si el dni ya esta en la base de datos excepcion
        if(ciudadanoDao.getCiudadano(ciudadano.getDni())!=null) ciudadano.setDni("USADO");

        CiudadanoValidator ciudadanoValidator = new CiudadanoValidator();
        ciudadanoValidator.validate(ciudadano, bindingResult);
        if (bindingResult.hasErrors())
            return "ciudadano/add";

        ciudadanoDao.addCiudadano(ciudadano);

        //Añadimos el usuario a la sesión
        UserDetails user = new UserDetails();
        user.setRol("ciudadano");
        user.setNombre(ciudadano.getNombre());
        user.setGamil(ciudadano.getEmail());
        user.setUsername(ciudadano.getDni());
        session.setAttribute("user", user);

        //Enviamos correo de confirmacion
        CorreoController.enviaCorreo(new Correo(ciudadano.getEmail(),"Registro en el SANA",
                "Usted se ha registrado en el SANA correctamene.\n\tSu usario es: "+ciudadano.getDni()+"\n\tSu contraseña es: "+ciudadano.getPin()));

        return "redirect:indice";
    }

    @RequestMapping(value="/update/{dni}", method = RequestMethod.GET)
    public String editCiudadano(Model model, @PathVariable String dni) {
        model.addAttribute("ciudadano", ciudadanoDao.getCiudadano(dni));
        return "ciudadano/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("ciudadano") Ciudadano ciudadano,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "ciudadano/update";
        ciudadanoDao.updateCiudadano(ciudadano);
        return "redirect:list";
    }

    @RequestMapping(value="/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        ciudadanoDao.deleteCiudadano(dni);
        return "redirect:../list";
    }

}
