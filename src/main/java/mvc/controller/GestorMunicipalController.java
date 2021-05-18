package mvc.controller;

import mvc.dao.GestorMunicipalDao;
import mvc.model.GestorMunicipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/gestorMunicipal")
public class GestorMunicipalController {

    private GestorMunicipalDao gestorMunicipalDao;

    @RequestMapping("/indice")
    public String indice() {
        return "gestorMunicipal/indice";
    }


    @Autowired
    public void setGestorMunicipalDao(GestorMunicipalDao gestorMunicipalDao) {
        this.gestorMunicipalDao=gestorMunicipalDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    @RequestMapping("/list")
    public String listGestoresMunicipales(Model model) {
        model.addAttribute("gestores", gestorMunicipalDao.getGestores());
        return "gestorMunicipal/list";
    }

    @RequestMapping(value="/add")
    public String addGestorMunicipal(Model model) {
        model.addAttribute("gestor", new GestorMunicipal());
        return "gestorMunicipal/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("gestor") GestorMunicipal gestor,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "gestorMunicipal/add";
        gestorMunicipalDao.addGestorMunicipal(gestor);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{dni}", method = RequestMethod.GET)
    public String editGestor(Model model, @PathVariable String dni) {
        model.addAttribute("gestor", gestorMunicipalDao.getGestorMunicipal(dni));
        return "gestorMunicipal/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("gestor") GestorMunicipal gestor,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "gestorMunicipal/update";
        gestorMunicipalDao.updateGestorMunicipal(gestor);
        return "redirect:list";
    }

    @RequestMapping(value="/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        gestorMunicipalDao.deleteGestorMunicipal(dni);
        return "redirect:../list";
    }

}
