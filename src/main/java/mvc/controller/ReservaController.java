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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ReservaValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return ReservaSvc.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {

        ReservaSvc reservasvc = (ReservaSvc) obj;
        Reserva reserva = reservasvc.crearReserva();

        if(reservasvc.getNumPersonas()>0 && reservasvc.getZona1().equals("--------"))
            errors.rejectValue("zona1", "obligatorio",
                    "Debe seleccionar una zona");

        if (reservasvc.getNumPersonas()>5) {
            if (reservasvc.getZona2().equals("--------"))
                errors.rejectValue("zona2", "obligatorio",
                        "Debe seleccionar una zona");

            if (reservasvc.getZona1().equals(reservasvc.getZona2()))
                errors.rejectValue("zona2", "obligatorio",
                        "Debe seleccionar una zona distinta a la zona 1");
        }

        if (reservasvc.getNumPersonas()>10) {
            if (reservasvc.getZona3().equals("--------"))
                errors.rejectValue("zona3", "obligatorio",
                        "Debe seleccionar una zona");

            if (reservasvc.getZona1().equals(reservasvc.getZona3()))
                errors.rejectValue("zona3", "obligatorio",
                        "Debe seleccionar una zona distinta a la zona 1");

            if (reservasvc.getZona2().equals(reservasvc.getZona3()))
                errors.rejectValue("zona3", "obligatorio",
                        "Debe seleccionar una zona distinta a la zona 2");

        }

        if(reservasvc.getNumPersonas()>15){
            if (reservasvc.getZona4().equals("--------"))
                errors.rejectValue("zona4", "obligatorio",
                        "Debe seleccionar una zona");

            if (reservasvc.getZona1().equals(reservasvc.getZona4()))
                errors.rejectValue("zona4", "obligatorio",
                        "Debe seleccionar una zona distinta a la zona 1");

            if (reservasvc.getZona2().equals(reservasvc.getZona4()))
                errors.rejectValue("zona4", "obligatorio",
                        "Debe seleccionar una zona distinta a la zona 2");

            if (reservasvc.getZona3().equals(reservasvc.getZona4()))
                errors.rejectValue("zona4", "obligatorio",
                        "Debe seleccionar una zona distinta a la zona 3");
        }


        if (reserva.getFecha().compareTo(LocalDate.now())<0) {
            errors.rejectValue("fecha", "obligatorio",
                    "La fecha de reserva debe ser mayor o igual a la actual");

            if (reserva.getHoraEntrada().compareTo(LocalTime.now()) < 0)
                errors.rejectValue("franja", "obligatorio",
                        "La hora de entrada debe ser mayor a la actual");
        }

    }
}

@Controller
@RequestMapping("/reserva")
public class ReservaController {

    private ReservaDao reservaDao;
    private FranjaEspacioDao franjaEspacioDao;
    private ZonaDao zonaDao;
    private NotificacionDao notificacionDao;
    private int pageLength = 8;

    @Autowired
    public void setReservaDao(ReservaDao reservaDao) {
        this.reservaDao = reservaDao;
    }

    @Autowired
    public void setNotificacionDao(NotificacionDao notificacionDao) {
        this.notificacionDao = notificacionDao;
    }

    @Autowired
    public void setFranjaEspacioDao(FranjaEspacioDao franjaEspacioDao) {
        this.franjaEspacioDao = franjaEspacioDao;
    }

    @Autowired
    public void setZonaDao(ZonaDao zonaDao) {
        this.zonaDao = zonaDao;
    }


    @RequestMapping("/list")
    public String listReservas(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("nueva") Optional<String> nueva, HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");
        List<ReservaSvc> reservas;
        if (user.getRol().equals("ciudadano")) reservas= reservaDao.getReservasParticular(user.getUsername());
        else if (user.getRol().equals("gestorMunicipal")) reservas = reservaDao.getReservasEnMiMunicipio(user.getUsername());
        else reservas = reservaDao.getReservasEnMiEspacio(user.getUsername());

        ArrayList<ArrayList<ReservaSvc>> reservasPage= new ArrayList<>();


        int ini=0;
        int fin=pageLength;
        while (fin<reservas.size()) {
            reservasPage.add(new ArrayList<>(reservas.subList(ini, fin)));
            ini+=pageLength;
            fin+=pageLength;
        }
        reservasPage.add(new ArrayList<>(reservas.subList(ini, reservas.size())));

        model.addAttribute("reservasPaged", reservasPage);

        // Paso 2: Crear la lista de numeros de pagina
        int totalPages = reservasPage.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        // Paso 3: selectedPage: usar parametro opcional page, o en su defecto, 1
        int currentPage = page.orElse(0);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("nueva", nueva.orElse("None"));

        if(user.getRol().equals("ciudadano")) return "reserva/particular";
        if(user.getRol().equals("gestorMunicipal")) return "reserva/listGestorMunicipal";

        //Controlador
        return "reserva/listControlador";
    }

    @RequestMapping(value="/cancela/{identificador}", method = RequestMethod.GET)
    public String cancelaReserva(Model model, HttpSession session,  @PathVariable Integer identificador) {

        ReservaSvc reserva = reservaDao.getReserva(identificador);

        UserDetails user = (UserDetails) session.getAttribute("user");
        String estado="";
        //Si lo elimina el propio ciudadano la reserva se cancelara automaticamente
        if(user.getRol().equals("ciudadano")){
            estado ="CANCELADA_CIUDADANO";
            reservaDao.cancelaReserva(reserva.crearReserva(), estado);
            return "redirect:/reserva/list";
        }
        //Si la cancelan el gestor o el controlador se le notificara al ciudadano
        if(user.getRol().equals("gestorMunicipal")) estado = "CANCELADA_GESTOR";
        if(user.getRol().equals("controlador")) estado =  "CANCELADA_CONTROLADOR";

        //Cancela reserva
        reservaDao.cancelaReserva(reserva.crearReserva(), estado);
        String mensaje = "Su reserva con fecha "+reserva.getFecha()+" ha sido cancelada porque ";
        Notificacion notificacion = new Notificacion(reserva.getDni(), mensaje);
        model.addAttribute("notificacion", notificacion);

        //Correo de aviso de la cancelacion
        try {
            CorreoController.enviaCorreo(new Correo(user.getGmail(),"Cancelación de su reserva del dia "+reserva.getFecha()+" y hora "+ reserva.getFranja(),
                    "La reserva que tenia lugar en la/s zona/s"+reserva.getFusionZonas()+" de " +reserva.getFranja()+" ha sido cancelada.\n"+
                            "Para consultar los motivos dirijase a la sección de notificaciones de su cuenta\n"
                            +"En caso de que no se le indicarán en una notificación se los comunicaremos más adelante.\n Muchas gracias y disculpe las molestias"));
        }catch (Exception e ){
            System.out.println("No se pudo enviar el correo");
            System.out.println("Esto sucede a poque los correos de destino son ficticios");
        }



        return "notificacion/add";
    }


    @RequestMapping(value="/add/{nombreEspacio}")
    public String addReservaEspacio(Model model,  @PathVariable String nombreEspacio, @ModelAttribute("reserva") ReservaSvc reservaService, HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");

        reservaService.setDni(user.getUsername());
        reservaService.setIdentificador(reservaDao.getSiguienteIdentificadorReserva());
        model.addAttribute("reserva", reservaService);
        List<Zona> disponibles = reservaDao.getZonasDisponibles(reservaService.crearReserva());
        disponibles.add(0,new Zona("--------"));
        model.addAttribute("zonas",disponibles );
        return "reserva/add";
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("reserva") ReservaSvc reserva, BindingResult bindingResult, HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");
        ReservaValidator reservaValidator = new ReservaValidator();
        reservaValidator.validate(reserva, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Zona> disponibles = reservaDao.getZonasDisponibles(reserva.crearReserva());
            disponibles.add(0,new Zona("--------"));
            model.addAttribute("zonas",disponibles );
            model.addAttribute("franjas",franjaEspacioDao.getFranjasEspacio(reserva.getNombreEspacio()));
            return "/reserva/add";
        }
        reservaDao.addReserva(reserva.crearReserva() , reserva.getFusionZonas());
        
        //Añadimos una notificaion
        Notificacion notificacion = new Notificacion();
        notificacion.setIdentificador(notificacionDao.getSiguienteIdentificadorNotificacion());
        notificacion.setDniCiudadano(reserva.getDni());
        notificacion.setMensaje("Usted ha realizado una reserva en la/s zona/s "+reserva.getFusionZonas()+".\n" +
                "La reserva es el dia "+reserva.getFecha()+" , de "+reserva.getFranja()+".");
        notificacionDao.addNotificacion(notificacion);

        //Correo de confirmacion de la reserva
        CorreoController.enviaCorreo(new Correo(user.getGmail(),"Nueva reserva el día "+reserva.getFecha()+" a las  "+reserva.getFranja(),
                "Usted ha realizado una reserva en la/s zona/s "+reserva.getFusionZonas()+" para un total de "+reserva.getNumPersonas()+" personas.\n"+
                        "La reserva es el dia "+reserva.getFecha()+" , a las "+reserva.getFranja()+"."));

        return "redirect:list?nueva="+reserva.getIdentificador();
    }



    @RequestMapping(value = "/delete/{identificador}")
    public String processDelete(@PathVariable Integer identificador) {
        reservaDao.deleteReserva(identificador);
        return "redirect:../list";
    }


    /*
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




        ReservaValidator reservaValidator = new ReservaValidator();
        reservaValidator.validate(reserva, bindingResult);
        if (bindingResult.hasErrors())
            return "reserva/update";
        reservaDao.updateReserva(reservaService.crearReserva());
        return "redirect:list";
    }*/
}