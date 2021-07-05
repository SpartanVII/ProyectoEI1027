package mvc.controller;

import mvc.dao.FranjaEspacioDao;
import mvc.model.EspacioPublico;
import mvc.model.FranjaEspacio;
import mvc.services.FranjaEspacioSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class FranjaEspacioValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return FranjaEspacioSvc.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {

        FranjaEspacioSvc franjaEspacio = (FranjaEspacioSvc) obj;


        if (franjaEspacio.getNombreEspacio().equals("SIN HORAS")) {
            errors.rejectValue("horaSalida", "obligatorio", "Ambos campos son obligatorios");
            return;
        }

        if (franjaEspacio.getNombreEspacio().equals("USADO"))
            errors.rejectValue("horaSalida", "obligatorio",
                    "Esta franja horaria ya existe");


        if (Duration.between(franjaEspacio.getHoraEntrada(),franjaEspacio.getHoraSalida()).toMinutes() < 15) {
            if (Duration.between(franjaEspacio.getHoraEntrada(),franjaEspacio.getHoraSalida()).toMinutes()>0)
                errors.rejectValue("horaSalida", "obligatorio",
                        "Debe haber una diferencia mínima de 15 minutos");
            else
                errors.rejectValue("horaSalida", "obligatorio",
                        "La hora de salida debe ser mayor a la de entrada");
        }
    }
}

@Controller
@RequestMapping("/franjaEspacio")
public class FranjaEspacioController {

    private FranjaEspacioDao franjaEspacioDao;
    private int pageLength = 6;
    @Autowired
    public void setFranjaEspacioDao(FranjaEspacioDao franjaEspacioDao) {
        this.franjaEspacioDao=franjaEspacioDao;
    }

    @RequestMapping("/list/{nombre}")
    public String listFranjas(Model model, @PathVariable String nombre, @RequestParam("nueva") Optional<String> nueva, @RequestParam("page") Optional<Integer> page ) {

        List<FranjaEspacioSvc> franjaEspacioSvcs = franjaEspacioDao.getFranjasEspacio(nombre);
        ArrayList<ArrayList<FranjaEspacioSvc>> franjasPaginas= new ArrayList<>();

        int ini=0;
        int fin=pageLength;
        while (fin<franjaEspacioSvcs.size()) {
            franjasPaginas.add(new ArrayList<>(franjaEspacioSvcs.subList(ini, fin)));
            ini+=pageLength;
            fin+=pageLength;
        }
        franjasPaginas.add(new ArrayList<>(franjaEspacioSvcs.subList(ini, franjaEspacioSvcs.size())));
        model.addAttribute("franjasPaged", franjasPaginas);

        // Paso 2: Crear la lista de numeros de pagina
        int totalPages = franjasPaginas.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        // Paso 3: selectedPage: usar parametro opcional page, o en su defecto
        int currentPage = page.orElse(0);
        model.addAttribute("selectedPage", currentPage);


        model.addAttribute("nueva",nueva.orElse("None"));
        model.addAttribute("nombre",nombre);
        return "franjaEspacio/list";
    }

    @RequestMapping(value="/add/{nombre}")
    public String addFranjas(Model model, @PathVariable String nombre) {

        FranjaEspacioSvc franjaEspacioSvc = new FranjaEspacioSvc();
        franjaEspacioSvc.setNombreEspacio(nombre);
        model.addAttribute("nombre",nombre);
        model.addAttribute("franja", franjaEspacioSvc);
        return "franjaEspacio/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("franja") FranjaEspacioSvc franjaEspacioSvc, Model model, BindingResult bindingResult){

        String nombre = franjaEspacioSvc.getNombreEspacio();
        FranjaEspacio franjaEspacio = franjaEspacioDao.getFranjaEspacio(franjaEspacioSvc.crearFranjaEspacio());

        if(franjaEspacio!=null)
            franjaEspacioSvc.setNombreEspacio("USADO");

    if(franjaEspacioSvc.getHoraEntrada()==null || franjaEspacioSvc.getHoraSalida()==null)
            franjaEspacioSvc.setNombreEspacio("SIN HORAS");
        Validator validator= new FranjaEspacioValidator();
        validator.validate(franjaEspacioSvc,bindingResult);

        if(bindingResult.hasErrors()){
            franjaEspacioSvc.setNombreEspacio(nombre);
            model.addAttribute("nombre",franjaEspacioSvc.getNombreEspacio());
            model.addAttribute("franja", franjaEspacioSvc);
            return "franjaEspacio/add";
        }
        franjaEspacioDao.addFranjaEspacio(franjaEspacioSvc.crearFranjaEspacio());
        return "redirect:/franjaEspacio/list/"+franjaEspacioSvc.getNombreEspacio()+"?nueva="+franjaEspacioSvc.getHoraEntrada().toString()+"/"+franjaEspacioSvc.getHoraSalida().toString();
    }




    @RequestMapping(value="/delete/{nombre}/{horaEntrada}/{horaSalida}")
    public String processDelete(@PathVariable String nombre, @PathVariable String horaEntrada,@PathVariable String horaSalida) {


        FranjaEspacio franjaEspacio= new FranjaEspacio(LocalTime.parse(horaEntrada),LocalTime.parse(horaSalida),nombre);

        franjaEspacioDao.deleteFranjaEspacio(franjaEspacio);
        return "redirect:/franjaEspacio/list/"+franjaEspacio.getNombreEspacio();
    }

}
