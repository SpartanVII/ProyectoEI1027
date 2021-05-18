package mvc.controller;

import mvc.dao.EspacioPublicoDao;
import mvc.dao.FranjaEspacioDao;
import mvc.dao.ReservaDao;
import mvc.dao.ZonaDao;
import mvc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private ReservaDao reservaDao;
    private FranjaEspacioDao franjaEspacioDao;
    private ZonaDao zonaDao;

    @Autowired
    public void setReservaDao(ReservaDao reservaDao) {
        this.reservaDao = reservaDao;
    }

    @Autowired
    public void setFranjaEspacioDao(FranjaEspacioDao franjaEspacioDao) {
        this.franjaEspacioDao = franjaEspacioDao;
    }

    @Autowired
    public void setZonaDao(ZonaDao zonaDao) {
        this.zonaDao = zonaDao;
    }

    @ModelAttribute("franjas")
    public List<FranjaEspacio> franjaList() { return  franjaEspacioDao.getFranjaEspacioList(); }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    @RequestMapping("/list")
    public String listReservas(Model model) {
        model.addAttribute("reservas", reservaDao.getReservas());
        return "reserva/list";
    }

    @RequestMapping(value = "/add")
    public String addReserva(HttpSession session, Model model) {
        model.addAttribute("reserva", new Reserva());
        return "reserva/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("reserva") Reserva reserva,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "reserva/add";
        reservaDao.addReserva(reserva);
        return "redirect:list";
    }


    @RequestMapping(value="/add/{identificador}")
    public String addReservaEspacio(Model model, @PathVariable String identificador,
                                    HttpSession session) {
        model.addAttribute("zona", zonaDao.getZona(identificador));
        Ciudadano ciudadano = (Ciudadano) session.getAttribute("user");
        model.addAttribute("dni", ciudadano.getDni());
        return "reserva/add";
    }

    @RequestMapping(value = "/delete/{identificador}")
    public String processDelete(@PathVariable String identificador) {
        reservaDao.deleteReserva(identificador);
        return "redirect:../list";
    }

    @RequestMapping("/listParticular")
    public String listReservaDni(Model model, HttpSession session) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        model.addAttribute("reservas", reservaDao.getReservasParticular(user.getUsername()));
        return "reserva/particular";
    }

}