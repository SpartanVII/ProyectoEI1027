package mvc.controller;

import mvc.dao.GestorMunicipalDao;
import mvc.dao.ZonaDao;
import mvc.model.GestorMunicipal;
import mvc.model.Zona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/zona")
public class ZonaController {
    private ZonaDao zonaDao;

    @Autowired
    public void setZonaDao(ZonaDao zonaDao) {
        this.zonaDao=zonaDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    @RequestMapping("/list")
    public String listZonas(Model model) {
        model.addAttribute("zonas", zonaDao.getZonas());
        return "zona/list";
    }

    @RequestMapping(value="/add")
    public String addZona(Model model) {
        model.addAttribute("zona", new Zona());
        return "zona/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("zona") Zona zona,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "zona/add";
        zonaDao.addZona(zona);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{identificador}", method = RequestMethod.GET)
    public String editGestor(Model model, @PathVariable String identificador) {
        model.addAttribute("zona", zonaDao.getZona(identificador));
        return "zona/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("zona") Zona zona,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "zona/update";
        zonaDao.updateZona(zona);
        return "redirect:list";
    }

    @RequestMapping(value="/delete/{identificador}")
    public String processDelete(@PathVariable String identificador) {
        zonaDao.deleteZona(identificador);
        return "redirect:list";
    }

    @RequestMapping(value="/particular/{nombre}")
    public String listaZonasEspacio(Model model, @PathVariable String nombre) {
        model.addAttribute("zonas",zonaDao.getZonasEspacio(nombre));
        return "zona/list";
    }

}
