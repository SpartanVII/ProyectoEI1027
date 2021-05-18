package mvc.controller;

import javax.servlet.http.HttpSession;

import mvc.dao.CiudadanoDao;
import mvc.dao.ControladorDao;
import mvc.dao.GestorMunicipalDao;
import mvc.model.Ciudadano;
import mvc.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return UserDetails.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        // Exercici: Afegeix codi per comprovar que
        // l'usuari i la contrasenya no estiguen buits
        UserDetails user = (UserDetails) obj;
        if (user.getUsername().trim().equals(""))
            errors.rejectValue("username", "obligatori",
                    "Campo obligatorio");

        if (user.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatori",
                    "Campo obligatorio");
    }
}

@Controller
public class LoginController {

    private GestorMunicipalDao gestorMunicipalDao;
    private CiudadanoDao ciudadanoDao;
    private ControladorDao controladorDao;

    @Autowired
    public void setCiudadanoDaoDao(CiudadanoDao ciudadanoDao) {
        this.ciudadanoDao = ciudadanoDao;
    }

    @Autowired
    public void setGestorMunicipalDao(GestorMunicipalDao gestorMunicipalDao) {
        this.gestorMunicipalDao=gestorMunicipalDao;
    }

    @Autowired
    public void setControladorDao(ControladorDao controladorDao) {
        this.controladorDao = controladorDao;
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserDetails());
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") UserDetails user, BindingResult bindingResult, HttpSession session) {

        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }

        if(gestorMunicipalDao.getGestorMunicipal(user.getUsername(), user.getPassword())!=null){
            user.setRol("gestorMunicipal");
            user.setPassword(""); //Como ya no necesitamos la contraseña, la borramos por motivos de seguridad
            session.setAttribute("user", user);
            return "redirect:/gestorMunicipal/indice";
        }

        if(ciudadanoDao.getCiudadano(user.getUsername(), user.getPassword())!=null){
            user.setRol("ciudadano");
            user.setPassword("");
            session.setAttribute("user", user);
            return "redirect:/ciudadano/indice";
        }

        if(controladorDao.getControlador(user.getUsername(), user.getPassword())!=null){
            user.setRol("controlador");
            user.setPassword("");
            session.setAttribute("user", user);
            return "redirect:/controlador/indice";
        }

        bindingResult.rejectValue("password", "badpw", "Usuario o contraseña incorrectos");
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

