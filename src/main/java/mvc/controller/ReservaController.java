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
        return Reserva.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {

        Reserva reserva = (Reserva) obj;

        if(reserva.getNumPersonas()<0) {
            //Error asociado a que la zona ya esta reservada el dia y hora
            if(reserva.getNumPersonas()==-111111111){
                errors.rejectValue("fecha", "obligatorio", "Zona ocupada en la hora y fecha seleccionadas");
            }else {
                //Volvemos a poner las personas en positivo
                errors.rejectValue("numPersonas", "obligatorio", "Max."+-1*reserva.getNumPersonas() +" personas. \n" +
                        "Reserve esta y las zonas adyacentes necesarias");

            }
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
    private int pageLength = 8;

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


    @RequestMapping("/list")
    public String listReservas(Model model, @RequestParam("page") Optional<Integer> page, HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");
        List<Reserva> reservas;
        if (user.getRol().equals("ciudadano")) reservas= reservaDao.getReservasParticular(user.getUsername());
        else if (user.getRol().equals("gestorMunicipal")) reservas = reservaDao.getReservas();
        else reservas = reservaDao.getReservasEnMiEspacio(user.getUsername());

        ArrayList<ArrayList<Reserva>> reservasPage= new ArrayList<>();


        int ini=0;
        int fin=pageLength-1;
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

        if(user.getRol().equals("ciudadano")) return "reserva/particular";
        if(user.getRol().equals("gestorMunicipal")) return "reserva/listGestorMunicipal";

        //Controlador
        return "reserva/listControlador";
    }

    @RequestMapping(value="/cancela/{identificador}", method = RequestMethod.GET)
    public String cancelaReserva(Model model, HttpSession session,  @PathVariable Integer identificador) {

        Reserva reserva = reservaDao.getReserva(identificador);

        UserDetails user = (UserDetails) session.getAttribute("user");
        String estado="";
        //Si lo elimina el propio ciudadano la reserva se cancelara automaticamente
        if(user.getRol().equals("ciudadano")){
            estado ="CANCELADA_CIUDADANO";
            reservaDao.cancelaReserva(reserva, estado);
            return "redirect:/reserva/list";
        }

        //Si la cancelan el gestor o el controlador se le notificara al ciudadano
        if(user.getRol().equals("gestorMunicipal")) estado = "CANCELADA_GESTOR";
        if(user.getRol().equals("controlador")) estado =  "CANCELADA_CONTROLADOR";


        //Cancela reserva
        reservaDao.cancelaReserva(reserva, estado);
        String mensaje = "Su reserva con fecha "+reserva.getFecha().toString()+" ha sido cancelada porque ";
        Notificacion notificacion = new Notificacion(reserva.getDniCiudadano(), mensaje);
        model.addAttribute("notificacion", notificacion);

        //Correo de aviso de la cancelacion
        CorreoController.enviaCorreo(new Correo(user.getGamil(),"Cancelación de su reserva del dia "+reserva.getFecha().toString(),
                    "La reserva que tenia lugar en la zona"+reserva.getIdentificadorZona()+" de " +reserva.getHoraEntrada()+
                            " a "+reserva.getHoraSalida()+" ha sido cancelada.\n"+ "Para consultar los motivos dirijase a la sección de notificaciones de su cuenta\n"
                            +"En caso de que no se le indicarán en una notificación se los comunicaremos más adelante.\n Muchas gracias y disculpe las molestias"));


        return "notificacion/add";
    }


    @RequestMapping(value="/add/{zona}")
    public String addReservaEspacio(Model model,  @PathVariable String zona, HttpSession session) {
        UserDetails user = (UserDetails) session.getAttribute("user");

        ReservaSvc reservaService = new ReservaSvc();
        reservaService.setDni(user.getUsername());
        reservaService.setZona(zona);
        reservaService.setIdentificador(reservaDao.getSiguienteIdentificadorReserva());
        reservaService.setNombreEspacio(zonaDao.getZona(zona).getNombreEspacio());

        model.addAttribute("franjas",franjaEspacioDao.getFranjasEspacio(zonaDao.getZona(zona).getNombreEspacio()));
        model.addAttribute("reserva", reservaService);
        return "reserva/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("reserva") ReservaSvc reservaService, HttpSession session, BindingResult bindingResult) {

        UserDetails user = (UserDetails) session.getAttribute("user");
        Reserva reserva = reservaService.crearReserva();
        int numPersonas = reserva.getNumPersonas();
        int personasMaxZona=zonaDao.getZona(reserva.getIdentificadorZona()).getCapMaxima();

        //Ponemos las personas ennegativo para que lo detecte el validador que no caben todas
        if(personasMaxZona-numPersonas<0) numPersonas=-1*personasMaxZona;
        //Error asociado a que la zona ya esta reservada el dia y hora
        else if(!reservaDao.resersvaDisponible(reserva)) numPersonas=-111111111;

        reserva.setNumPersonas(numPersonas);
        ReservaValidator reservaValidator = new ReservaValidator();
        reservaValidator.validate(reserva, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("franjas",franjaEspacioDao.getFranjasEspacio(reserva.getNombreEspacio()));
            return "/reserva/add";
        }
        reservaDao.addReserva(reserva);

        //Correo de confirmacion de la reserva
        CorreoController.enviaCorreo(new Correo(user.getGamil(),"Reserva para el dia "+reserva.getFecha().toString(),
                "Usted ha realizado una reserva en la zona "+reserva.getIdentificadorZona()+".\n" +
                        "La reserva comienza "+reserva.getHoraEntrada()+" y acaba a las "+reserva.getHoraSalida()+"."));

        return "redirect:list";
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