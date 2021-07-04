package mvc.controller;

import mvc.dao.CiudadanoDao;
import mvc.dao.FranjaEspacioDao;
import mvc.model.Ciudadano;
import mvc.model.Correo;
import mvc.model.FranjaEspacio;
import mvc.model.UserDetails;
import mvc.services.FranjaEspacioSvc;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

class FranjaEspacioValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return FranjaEspacioSvc.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {

        FranjaEspacioSvc franjaEspacio = (FranjaEspacioSvc) obj;


        if (franjaEspacio.getNombreEspacio().equals("SIN HORAS")) {
            errors.rejectValue("horaSalida", "obligatorio", "Ambos campos son obligatorios");
            return;
        }

        if (franjaEspacio.getNombreEspacio().equals("USADO"))
            errors.rejectValue("horaSalida", "obligatorio",
                    "Esta franja horaria ya existe");


        if (Duration.between(franjaEspacio.getHoraEntrada(),franjaEspacio.getHoraSalida()).toMinutes() < 15) {
            if (Duration.between(franjaEspacio.getHoraEntrada(),franjaEspacio.getHoraSalida()).toMinutes()>0)
                errors.rejectValue("horaSalida", "obligatorio",
                        "Debe haber una diferencia mínima de 15 minutos");
            else
                errors.rejectValue("horaSalida", "obligatorio",
                        "La hora de salida debe ser mayor a la de entrada");
        }
    }
}

@Controller
@RequestMapping("/franjaEspacio")
public class FranjaEspacioController {

    private FranjaEspacioDao franjaEspacioDao;

    @Autowired
    public void setFranjaEspacioDao(FranjaEspacioDao franjaEspacioDao) {
        this.franjaEspacioDao=franjaEspacioDao;
    }

    @RequestMapping("/list/{nombre}")
    public String listFranjas(Model model, @PathVariable String nombre ) {
        model.addAttribute("nombre",nombre);
        model.addAttribute("franjas", franjaEspacioDao.getFranjasEspacio(nombre));
        return "franjaEspacio/list";
    }

    @RequestMapping(value="/add/{nombre}")
    public String addFranjas(Model model, @PathVariable String nombre) {

        FranjaEspacioSvc franjaEspacioSvc = new FranjaEspacioSvc();
        franjaEspacioSvc.setNombreEspacio(nombre);
        model.addAttribute("nombre",nombre);
        model.addAttribute("franja", franjaEspacioSvc);
        return "franjaEspacio/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("franja") FranjaEspacioSvc franjaEspacioSvc, Model model, BindingResult bindingResult){

        String nombre = franjaEspacioSvc.getNombreEspacio();
        FranjaEspacio franjaEspacio = franjaEspacioDao.getFranjaEspacio(franjaEspacioSvc.creaFranjaEspacio());

        if(franjaEspacio!=null)
            franjaEspacioSvc.setNombreEspacio("USADO");

    if(franjaEspacioSvc.getHoraEntrada()==null || franjaEspacioSvc.getHoraSalida()==null)
            franjaEspacioSvc.setNombreEspacio("SIN HORAS");
        Validator validator= new FranjaEspacioValidator();
        validator.validate(franjaEspacioSvc,bindingResult);

        if(bindingResult.hasErrors()){
            franjaEspacioSvc.setNombreEspacio(nombre);
            model.addAttribute("nombre",franjaEspacioSvc.getNombreEspacio());
            model.addAttribute("franja", franjaEspacioSvc);
            return "franjaEspacio/add";
        }
        franjaEspacioDao.addFranjaEspacio(franjaEspacioSvc.creaFranjaEspacio());
        return "redirect:/franjaEspacio/list/"+franjaEspacioSvc.getNombreEspacio();
    }




    @RequestMapping(value="/delete/{nombre}/{horaEntrada}/{horaSalida}")
    public String processDelete(@PathVariable String nombre, @PathVariable String horaEntrada,@PathVariable String horaSalida) {


        FranjaEspacio franjaEspacio= new FranjaEspacio(LocalTime.parse(horaEntrada),LocalTime.parse(horaSalida),nombre);

        franjaEspacioDao.deleteFranjaEspacio(franjaEspacio);
        return "redirect:/franjaEspacio/list/"+franjaEspacio.getNombreEspacio();
    }

}
