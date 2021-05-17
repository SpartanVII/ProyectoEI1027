package mvc.controller;

import mvc.dao.CiudadanoDao;
import mvc.dao.GestorMunicipalDao;
import mvc.model.Ciudadano;
import mvc.model.GestorMunicipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

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
    public String processAddSubmit(@ModelAttribute("ciudadano") Ciudadano ciudadano,
                                   BindingResult bindingResult,
                                   HttpSession session) {
        if (bindingResult.hasErrors())
            return "ciudadano/add";
        ciudadanoDao.addCiudadano(ciudadano);
        session.setAttribute("user", ciudadano);
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
