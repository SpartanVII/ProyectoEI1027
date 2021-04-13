package mvc.controller;

import mvc.dao.EjemploDao;
import mvc.model.Ejemplo;
import es.uji.ei1027.clubesportiu.services.ClassificacioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ejemplo")
public class EjemploController {
    private ClassificacioService classificacioService;
    private EjemploDao classificacioDao;
    //
    @Autowired
    public void setClassificacioService(ClassificacioService classificacioService) {
        this.classificacioService = classificacioService;
    }

    @RequestMapping("/perpais")
    public String listClsfPerPais(Model model) {
        model.addAttribute("classificacions",
                classificacioService.getClassificationByCountry("Duos Sincro"));
        return "ejemplo/perpais";
    }

    //
    @Autowired
    public void setClassificacioDao(EjemploDao classificacioDao) {
        this.classificacioDao=classificacioDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    @RequestMapping("/list")
    public String listClassificacio(Model model) {
        model.addAttribute("classificacions", classificacioDao.getClassificacions());
        return "ejemplo/list";
    }


    @RequestMapping(value = "/delete/{nNadador}/{nProva}")
    public String processDeleteClassif(@PathVariable String nNadador,
                                       @PathVariable String nProva) {
        classificacioDao.deleteClassificacio(nNadador, nProva);
        return "redirect:../../list";
    }

    @RequestMapping(value="/update/{nNadador}/{nProva}", method = RequestMethod.GET)
    public String editClassificacio(Model model, @PathVariable String nNadador, @PathVariable String nProva) {
        model.addAttribute("ejemplo", classificacioDao.getClassificacio(nNadador,nProva));
        return "ejemplo/update";
    }


    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("ejemplo") Ejemplo classificacio,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "ejemplo/update";
        classificacioDao.updateClassificacio(classificacio);
        return "redirect:list";
    }

    @RequestMapping(value="/add")
    public String addNadador(Model model) {
        model.addAttribute("ejemplo", new Ejemplo());
        return "ejemplo/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("ejemplo") Ejemplo classificacio,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "ejemplo/add";
        classificacioDao.addClassificacio(classificacio);
        return "redirect:list";
    }
}
