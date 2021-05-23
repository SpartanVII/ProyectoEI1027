package mvc.controller;

import mvc.dao.NotificacionDao;
import mvc.model.Notificacion;
import mvc.model.UserDetails;
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
@RequestMapping("/notificacion")
public class NotificacionController {

    private NotificacionDao notificacionDao;

    @Autowired
    public void setNotificacionDao(NotificacionDao notificacionDao) {
        this.notificacionDao=notificacionDao;
    }


    @RequestMapping("/list")
    public String listNotificacion(Model model, HttpSession session) {
        UserDetails userDetails= (UserDetails) session.getAttribute("user") ;
        model.addAttribute("notificaciones", notificacionDao.getMisNotificaciones(userDetails.getUsername()));
        return "notificacion/list";
    }

    @RequestMapping(value="/add")
    public String addNotificacion(Model model) {
        model.addAttribute("notificacion", new Notificacion());
        return "notificacion/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("notificacion") Notificacion notificacion, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "notificacion/add";

        System.out.println(notificacionDao.getSiguienteIdentificadorNotificacion());
        notificacion.setIdentificador(notificacionDao.getSiguienteIdentificadorNotificacion());
        notificacionDao.addNotificacion(notificacion);

        return "redirect:/reserva/list";
    }


    @RequestMapping(value="/delete/{identificador}")
    public String processDelete(@PathVariable Integer identificador) {
        notificacionDao.deleteNotificacion(identificador);
        return "redirect:../list";
    }

}
