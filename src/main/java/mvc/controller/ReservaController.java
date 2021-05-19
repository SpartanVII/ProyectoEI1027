package mvc.controller;

import mvc.dao.*;
import mvc.model.*;
import mvc.services.ReservaService;
import mvc.services.ReservaSvc;
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

import java.util.List;

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private ReservaDao reservaDao;
    private FranjaEspacioDao franjaEspacioDao;

    @Autowired
    public void setReservaDao(ReservaDao reservaDao) {
        this.reservaDao = reservaDao;
    }

    @Autowired
    public void setFranjaEspacioDao(FranjaEspacioDao franjaEspacioDao) {
        this.franjaEspacioDao = franjaEspacioDao;
    }

    @ModelAttribute("franjas")
    public List<FranjaEspacio> franjaList() { return  franjaEspacioDao.getFranjaEspacioList(); }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    @RequestMapping("/list")
    public String listReservas(Model model, HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");

        if(user.getRol().equals("ciudadano")){
            model.addAttribute("reservas", reservaDao.getReservasParticular(user.getUsername()));
            return "reserva/particular";
        }

        if(user.getRol().equals("gestorMunicipal")){
            model.addAttribute("reservas", reservaDao.getReservas());
            return "reserva/listGestorMunicipal";
        }

        //Controlador
        model.addAttribute("reservas", reservaDao.getReservasEnMiEspacio(user.getUsername()));
        return "reserva/listControlador";
    }

    @RequestMapping(value="/cancela/{identificador}", method = RequestMethod.GET)
    public String cancelaReserva(Model model, HttpSession session,  @PathVariable String identificador) {

        //AÑADIR VALIDAOR PARA COMPROBAR SI LA RESERVA ESTA PENIENTE DE USO SI NO ES ASI EL BOTON NO HARA NADA
        UserDetails user = (UserDetails) session.getAttribute("user");
        String estado="";

        if(user.getRol().equals("ciudadano")) estado ="CANCELADA_CIUDADANO";
        if(user.getRol().equals("gestorMunicipal")) estado = "CANCELADA_GESTOR";
        if(user.getRol().equals("controlador")) estado =  "CANCELADA_CONTROLADOR";


        if(reservaDao.getReserva(identificador).getEstado().equals("PENDIENTE_USO")) {
            //Cancela reserva
            reservaDao.cancelaReserva(reservaDao.getReserva(identificador), estado);
            String mensaje = "Su reserva ha sido cancelada porque ";
            Notificacion notificacion = new Notificacion(reservaDao.getReserva(identificador).getDniCiudadano(), mensaje);
            model.addAttribute("notificacion", notificacion);

            return "notificacion/add";
        }

        return "redirect:/reserva/list";
    }
    /*
    @RequestMapping(value="/cancela", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("reserva") Reserva reserva, @ModelAttribute("notificacion") Notificacion notificacion, HttpSession session) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        String estado = "";

        if(user.getRol().equals("gestorMunicipal")) estado = "CANCELADA_GESTOR";
        if(user.getRol().equals("controlador")) estado =  "CANCELADA_CONTROLADOR";
        NotificacionDao not = new NotificacionDao();
        not.addNotificacion(notificacion);

        if(reserva.getEstado().equals("PENDIENTE")){
            reservaDao.cancelaReserva(reserva,estado);

        }

        return "redirect:/reserva/list";
    }

    */
    @RequestMapping(value = "/add")
    public String addReserva(HttpSession session, Model model) {
        model.addAttribute("reserva", new Reserva());
        return "reserva/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("reserva") ReservaSvc reservaService,  BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            System.out.println(bindingResult);
            return "reserva/add";}
        reservaDao.addReserva(reservaService.crearReserva());
        return "redirect:list";
    }

    @RequestMapping(value="/add/{identificador}")
    public String addReservaEspacio(Model model, @PathVariable String identificador,
                                    HttpSession session) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        ReservaSvc reservaService = new ReservaSvc();
        reservaService.setDni(user.getUsername());
        reservaService.setZona(identificador);
        model.addAttribute("reserva", reservaService);
        return "reserva/add";
    }



    @RequestMapping(value = "/delete/{identificador}")
    public String processDelete(@PathVariable String identificador) {
        reservaDao.deleteReserva(identificador);
        return "redirect:../list";
    }

}