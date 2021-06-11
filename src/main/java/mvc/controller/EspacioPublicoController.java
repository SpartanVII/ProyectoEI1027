package mvc.controller;

import mvc.dao.EspacioPublicoDao;
import mvc.model.Ciudadano;
import mvc.model.EspacioPublico;
import mvc.model.UserDetails;
import org.apache.catalina.User;
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
@RequestMapping("/espacioPublico")
public class EspacioPublicoController {

    private EspacioPublicoDao espacioPublicoDao;

    @Autowired
    public void setEspacioPublicoDao(EspacioPublicoDao espacioPublicoDao) {
        this.espacioPublicoDao=espacioPublicoDao;
    }

    @RequestMapping("/listRegistrado")
    public String listEspacioPublico(Model model, HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("espaciosPublicos", espacioPublicoDao.getEspaciosPublicos());

        if (user.getRol().equals("gestorMunicipal"))
            return "espacioPublico/listGestor";

        if (user.getRol().equals("controlador"))
            return "espacioPublico/listControlador";

        return "espacioPublico/listConReserva";
    }

    @RequestMapping("/listSinRegistrar")
    public String listEspacioPublicoSinRegistrar(Model model) {
        model.addAttribute("espaciosPublicos", espacioPublicoDao.getEspaciosPublicos());
        return "espacioPublico/listSinRegistrar";
    }

    @RequestMapping(value="/add")
    public String addEspacioPublico(Model model) {
        model.addAttribute("espacioPublico", new EspacioPublico());
        return "espacioPublico/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("espacioPublico") EspacioPublico espacioPublico,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "espacioPublico/add";
        espacioPublicoDao.addEspacioPublico(espacioPublico);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{nombre}", method = RequestMethod.GET)
    public String editEspacioPublico(Model model, @PathVariable String nombre) {
        model.addAttribute("espacioPublico", espacioPublicoDao.getEspacioPublico(nombre));
        return "espacioPublico/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("espacioPublico") EspacioPublico espacioPublico,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "espacioPublico/update";
        espacioPublicoDao.updateEspacioPublico(espacioPublico);
        return "redirect:list";
    }

    @RequestMapping(value="/delete/{nombre}")
    public String processDelete(@PathVariable String nombre) {
        espacioPublicoDao.deleteEspacioPublico(nombre);
        return "redirect:../list";
    }

}
