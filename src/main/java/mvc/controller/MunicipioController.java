package mvc.controller;

import mvc.dao.MunicipioDao;
import mvc.model.GestorMunicipal;
import mvc.model.Municipio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/municipio")
public class MunicipioController {

    private MunicipioDao municipioDao;

    @Autowired
    public void setMunicipioDao(MunicipioDao municipioDao) {
        this.municipioDao=municipioDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    @RequestMapping("/list")
    public String listMunicipios(Model model) {
        model.addAttribute("municipios", municipioDao.getMunicipios());
        return "municipio/list";
    }

    @RequestMapping(value="/add")
    public String addGestorMunicipal(Model model) {
        model.addAttribute("municipio", new GestorMunicipal());
        return "municipio/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("municipio") Municipio municipio,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "municipio/add";
        municipioDao.addMunicipio(municipio);
        return "redirect:list";
    }


    @RequestMapping(value="/delete/{nombre}")
    public String processDelete(@PathVariable String nombre) {
        municipioDao.deleteMunicipio(nombre);
        return "redirect:../list";
    }

}
