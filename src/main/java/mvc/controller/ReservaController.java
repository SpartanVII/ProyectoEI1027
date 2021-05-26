package mvc.controller;

import mvc.dao.*;
import mvc.model.*;
import mvc.services.ReservaSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

class ReservaValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Reserva.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        Reserva reserva = (Reserva) obj;

        if(reserva.getNumPersonas()<=0) {
            if(reserva.getNumPersonas()==0)
                errors.rejectValue("numPersonas", "obligatorio", "En esta zona no quedan plazas libres");
            else
                //Mostramos la ocupación libre con un número positivo
                errors.rejectValue("numPersonas", "obligatorio", "En esta zona hay " + reserva.getNumPersonas() * (-1) + " plazas libres");
        }

        if (reserva.getFecha().compareTo(LocalDate.now())<0)
            errors.rejectValue("fecha", "obligatorio",
                    "La fecha de reserva debe ser mayor a la actual");

        if (reserva.getFecha().compareTo(LocalDate.now())==0 && reserva.getHoraEntrada().compareTo(LocalTime.now())<0)
            errors.rejectValue("franja", "obligatorio",
                    "La hora de entrada debe ser mayor a la actual");
    }
}

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
    public String cancelaReserva(Model model, HttpSession session,  @PathVariable Integer identificador) {

        UserDetails user = (UserDetails) session.getAttribute("user");
        String estado="";

        //Si lo elimina el propio ciudadano la reserva se cancelara automaticamente
        if(user.getRol().equals("ciudadano")){
            estado ="CANCELADA_CIUDADANO";
            reservaDao.cancelaReserva(reservaDao.getReserva(identificador), estado);
            return "redirect:/reserva/list";
        }

        //Si la cancelan el gestor o el controlador se le notificara al ciudadano
        if(user.getRol().equals("gestorMunicipal")) estado = "CANCELADA_GESTOR";
        if(user.getRol().equals("controlador")) estado =  "CANCELADA_CONTROLADOR";

        if(reservaDao.getReserva(identificador).getEstado().equals("PENDIENTE_USO")) {
            //Cancela reserva
            reservaDao.cancelaReserva(reservaDao.getReserva(identificador), estado);
            String mensaje = "Su reserva con fecha "+reservaDao.getReserva(identificador).getFecha().toString()+" ha sido cancelada porque ";
            Notificacion notificacion = new Notificacion(reservaDao.getReserva(identificador).getDniCiudadano(), mensaje);
            model.addAttribute("notificacion", notificacion);

            return "notificacion/add";
        }

        return "redirect:/reserva/list";
    }


    @RequestMapping(value="/add/{zona}")
    public String addReservaEspacio(Model model, @PathVariable String zona, HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");
        ReservaSvc reservaService = new ReservaSvc();
        reservaService.setDni(user.getUsername());
        reservaService.setZona(zona);
        reservaService.setIdentificador(reservaDao.getSiguienteIdentificadorReserva());
        model.addAttribute("reserva", reservaService);
        return "reserva/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("reserva") ReservaSvc reservaService, HttpSession session, BindingResult bindingResult) {
        UserDetails user = (UserDetails) session.getAttribute("user");
        Reserva reserva = reservaService.crearReserva();

        //Capacidad máxima de la zona se le resta personas que tienen reserva en esa zona
        String idZona = reserva.getIdentificadorZona();
        int ocupacionLibre = zonaDao.getZona(idZona).getCapMaxima() - reservaDao.getOcupacionZona(idZona,reserva.getFecha(),reserva.getHoraEntrada());
        int ocupacionPosible = ocupacionLibre - reserva.getNumPersonas();

        if (ocupacionPosible<0)
            //lo ponemos en negativo para que el validador capte que es un valor no válido
            reserva.setNumPersonas(-1*ocupacionLibre);

        ReservaValidator reservaValidator = new ReservaValidator();
        reservaValidator.validate(reserva, bindingResult);
        if (bindingResult.hasErrors()){
            return "reserva/add";}

        reservaDao.addReserva(reserva);

        //Correo de confirmacion de la reserva
        CorreoController.enviaCorre(new Correo(user.getGamil(),"Reserva para el dia "+reserva.getFecha().toString(),
                "Usted ha realizado una reserva en la zona "+reserva.getIdentificadorZona()+" para "+reserva.getNumPersonas()+" personas.\n" +
                        "La reserva comienza "+reserva.getHoraEntrada()+" y acaba a las "+reserva.getHoraSalida()+"."));

        return "redirect:list";
    }



    @RequestMapping(value = "/delete/{identificador}")
    public String processDelete(@PathVariable Integer identificador) {
        reservaDao.deleteReserva(identificador);
        return "redirect:../list";
    }



    @RequestMapping(value="/update/{identificador}", method = RequestMethod.GET)
    public String editReserva(Model model, @PathVariable Integer identificador){
        Reserva actual = reservaDao.getReserva(identificador);

        ReservaSvc reservaService = new ReservaSvc(actual);
        model.addAttribute("reserva", reservaService);
        return "reserva/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("reserva") ReservaSvc reservaService, BindingResult bindingResult) {

        Reserva reserva = reservaService.crearReserva();

        //Capacidad máxima de la zona se le resta personas que tienen reserva en esa zona y se le suma la cantidad de personas en la reserva que aun no ha sido actualizada
        String idZona = reserva.getIdentificadorZona();
        int ocupacionLibre = zonaDao.getZona(idZona).getCapMaxima() - reservaDao.getOcupacionZona(idZona,reserva.getFecha(),reserva.getHoraEntrada())
                                + reservaDao.getReserva(reserva.getIdentificador()).getNumPersonas();

        int ocupacionPosible = ocupacionLibre - reserva.getNumPersonas();

        if (ocupacionPosible<0)
            //lo ponemos en negativo para que el validador capte que es un valor no válido
            reserva.setNumPersonas(-1*ocupacionLibre);

        ReservaValidator reservaValidator = new ReservaValidator();
        reservaValidator.validate(reserva, bindingResult);
        if (bindingResult.hasErrors())
            return "reserva/update";
        reservaDao.updateReserva(reservaService.crearReserva());
        return "redirect:list";
    }
}