package mvc.controller;

import mvc.dao.ControladorDao;
import mvc.model.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/controlador")
public class ControladorController {

    private ControladorDao controladorDao;

    @Autowired
    public void setMunicipioDao(ControladorDao controladorDao) {
        this.controladorDao=controladorDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    @RequestMapping("/list")
    public String listControladors(Model model) {
        model.addAttribute("controladores", controladorDao.getControladores());
        return "controlador/list";
    }

    @RequestMapping(value="/add")
    public String addControlador(Model model) {
        model.addAttribute("controlador", new Controlador());
        return "controlador/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("controlador") Controlador controlador,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "controlador/add";
        controladorDao.addControlador(controlador);
        return "redirect:list";
    }


    @RequestMapping(value="/delete/{dni}")
    public String processDelete(@PathVariable String dni) {
        controladorDao.deleteControlador(dni);
        return "redirect:../list";
    }

}
