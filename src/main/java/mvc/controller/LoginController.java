package mvc.controller;

import javax.servlet.http.HttpSession;

import mvc.dao.CiudadanoDao;
import mvc.dao.GestorMunicipalDao;
import mvc.model.Ciudadano;
import mvc.model.GestorMunicipal;
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
    @Autowired
    private GestorMunicipalDao gestorMunicipalDao;

    @Autowired
    private CiudadanoDao ciudadanoDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserDetails());
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String checkLogin(@ModelAttribute("user") UserDetails user,
                             BindingResult bindingResult, HttpSession session) {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Comprova que el login siga correcte
        // intentant carregar les dades de l'usuari
        Object usuario = null;
        if(gestorMunicipalDao.getGestorMunicipal(user.getUsername(), user.getPassword())!=null){
            usuario = gestorMunicipalDao.getGestorMunicipal(user.getUsername());
        }
        else if(ciudadanoDao.getCiudadano(user.getUsername(), user.getPassword())!=null){
            usuario = ciudadanoDao.getCiudadano(user.getUsername());
        }

        if (usuario == null) {
            bindingResult.rejectValue("password", "badpw", "Usuario o contraseña incorrectos");
            return "login";
        }

        // Autenticats correctament.
        // Guardem les dades de l'usuari autenticat a la sessió
        session.setAttribute("user", usuario);

        if(session.getAttribute("nextUrl")!=null) {
            String url = session.getAttribute("nextUrl").toString();
            session.removeAttribute("nextUrl");
            return "redirect:" + url;
        }

        // Torna a la pàgina principal
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

