package mvc.controller;

import mvc.dao.EspacioPublicoDao;
import mvc.dao.PeriodosServiciosEstacionalesEnEspacioDao;
import mvc.dao.ServicioEstacionalDao;
import mvc.dao.ServicioPermaDao;
import mvc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

class ServicioValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return PeriodosServiciosEstacionalesEnEspacio.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        PeriodosServiciosEstacionalesEnEspacio periodosServiciosEstacionalesEnEspacio = (PeriodosServiciosEstacionalesEnEspacio) obj;

        if (periodosServiciosEstacionalesEnEspacio.getFechaInicio().compareTo(periodosServiciosEstacionalesEnEspacio.getFechaFin())>0)
            errors.rejectValue("fechaFin", "obligatorio",
                    "La fecha de fin debe ser mayor a la de inicio");

        if (periodosServiciosEstacionalesEnEspacio.getFechaInicio().compareTo(LocalDate.now())<0)
            errors.rejectValue("fechaInicio", "obligatorio",
                    "La fecha de inicio debe ser mayor o igual a la actual");

        if (periodosServiciosEstacionalesEnEspacio.getFechaFin().compareTo(LocalDate.now())<=0)
            errors.rejectValue("fechaFin", "obligatorio",
                    "La fecha de fin debe ser mayor a la actual");

    }
}

@Controller
@RequestMapping("/servicio")
public class ServicioController {
    private ServicioPermaDao servicioPermaDao;
    private EspacioPublicoDao espacioPublicoDao;
    private ServicioEstacionalDao servicioEstacionalDao;
    private PeriodosServiciosEstacionalesEnEspacioDao periodosServiciosEstacionalesEnEspacioDao;

    @Autowired
    public void setServicioPermaDao(ServicioPermaDao servicioPermaDao) {
        this.servicioPermaDao = servicioPermaDao;
    }

    @Autowired
    public void setEspacioPublicoDao(EspacioPublicoDao espacioPublicoDao) { this.espacioPublicoDao = espacioPublicoDao; }

    @Autowired
    public void setServicioEstacionalDao(ServicioEstacionalDao servicioEstacionalDao) { this.servicioEstacionalDao = servicioEstacionalDao; }

    @Autowired
    public void setPeriodosServiciosEstacionalesEnEspacioDao(PeriodosServiciosEstacionalesEnEspacioDao periodosServiciosEstacionalesEnEspacioDao) {
        this.periodosServiciosEstacionalesEnEspacioDao = periodosServiciosEstacionalesEnEspacioDao;
    }

    @RequestMapping("/list")
    public String listServiciosPerma(Model model) {
        model.addAttribute("serviciosPerma", servicioPermaDao.getServicios());
        return "/list";
    }

    @RequestMapping(value="/listEnEspacio/{nombreEspacio}", method = RequestMethod.GET)
    public String listServiciosPermaEnEspacio(Model model, @PathVariable String nombreEspacio, @RequestParam("nuevo") Optional<String> nuevo ) {
        model.addAttribute("nuevo",nuevo.orElse("None"));
        model.addAttribute("espacio", espacioPublicoDao.getEspacioPublico(nombreEspacio));
        model.addAttribute("serviciosPerma", servicioPermaDao.getServiciosEspacio(nombreEspacio));
        model.addAttribute("serviciosEstacionales", servicioEstacionalDao.getServiciosEstacionalesEspacio(nombreEspacio));
        return "/servicio/listServicios";
    }

    @RequestMapping(value="/deleteDeEspacio/{nombreEspacio}/{nombreServicio}")
    public String processDelete(Model model, @PathVariable String nombreEspacio, @PathVariable String nombreServicio) {
        if (servicioEstacionalDao.getServicioEstacional(nombreServicio)!=null)
            servicioEstacionalDao.deleteServicioEstacionalDeEspacio(nombreServicio,nombreEspacio);

        else
            servicioPermaDao.deleteServicioPermaDeEspacio(nombreServicio, nombreEspacio);

        return "redirect:/servicio/listEnEspacio/"+nombreEspacio;
    }

    @RequestMapping(value="/addPermanente/{nombreEspacio}")
    public String addServicioPermanente(Model model, @PathVariable String nombreEspacio) {
        model.addAttribute("espacio", nombreEspacio);
        model.addAttribute("serviciosPerma", servicioPermaDao.getServiciosDisponiblesParaEspacio(nombreEspacio));
        return "servicio/addPermanente";
    }

    @RequestMapping(value="/addPermanente/{nombreEspacio}/{nombreServicio}")
    public String addServicioPermanente( @PathVariable String nombreEspacio, @PathVariable String nombreServicio) {
        servicioPermaDao.addServicioPermaEnEspacio(nombreServicio, nombreEspacio);
        return "redirect:/servicio/listEnEspacio/"+nombreEspacio+"?nuevo="+nombreServicio;
    }

    @RequestMapping(value="/addEstacional/{nombreEspacio}")
    public String addServicioEstacional(Model model, @PathVariable String nombreEspacio) {
        model.addAttribute("espacio", nombreEspacio);
        model.addAttribute("serviciosEstacionales", servicioEstacionalDao.getServiciosEstacionalesDisponiblesParaEspacio(nombreEspacio));
        return "servicio/addEstacional";
    }

    @RequestMapping(value="/addEstacional/{nombreEspacio}/{nombreServicio}")
    public String addServicioEstacionalIntermedio(Model model, @PathVariable String nombreEspacio, @PathVariable String nombreServicio) {
        PeriodosServiciosEstacionalesEnEspacio periodo = new PeriodosServiciosEstacionalesEnEspacio();
        periodo.setNombreServicioEstacional(nombreServicio);
        periodo.setNombreEspacioPublico(nombreEspacio);
        model.addAttribute("fechaMinima",LocalDate.now());
        model.addAttribute("periodo", periodo);
        return "servicio/addEstacionalIntermedio";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("periodo") PeriodosServiciosEstacionalesEnEspacio periodosServiciosEstacionalesEnEspacio,
                                   BindingResult bindingResult) {
        ServicioValidator servicioValidator = new ServicioValidator();
        servicioValidator.validate(periodosServiciosEstacionalesEnEspacio, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("fechaMinima",LocalDate.now());
            model.addAttribute("periodo", periodosServiciosEstacionalesEnEspacio);
            return "/servicio/addEstacionalIntermedio";
        }
        periodosServiciosEstacionalesEnEspacioDao.addPeriodosServiciosEstacionales(periodosServiciosEstacionalesEnEspacio);
        return "redirect:/servicio/listEnEspacio/"+periodosServiciosEstacionalesEnEspacio.getNombreEspacioPublico()+"?nuevo="+periodosServiciosEstacionalesEnEspacio.getNombreServicioEstacional();
    }

}
