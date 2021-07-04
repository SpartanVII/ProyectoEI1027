package mvc.controller;

import mvc.dao.*;
import mvc.model.EspacioPublico;
import mvc.model.GestorMunicipal;
import mvc.model.UserDetails;
import mvc.services.ReservaSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;




class EspacioPublicoValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return UserDetails.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {

        EspacioPublico espacioPublico = (EspacioPublico) obj;

        if (espacioPublico.getNombre().equals("USADO"))
            errors.rejectValue("nombre", "obligatorio",
                    "Ya existe un espacio con este nombre");
    }
}

@Controller
@RequestMapping("/espacioPublico")
public class EspacioPublicoController {

    private EspacioPublicoDao espacioPublicoDao;
    private GestorMunicipalDao gestorMunicipalDao;
    private FranjaEspacioDao franjaEspacioDao;
    private MunicipioDao municipioDao;
    private int pageLength = 6;

    @Autowired
    public void setEspacioPublicoDao(EspacioPublicoDao espacioPublicoDao) {
        this.espacioPublicoDao=espacioPublicoDao;
    }

    @Autowired
    public void setGestorMunicipalDao(GestorMunicipalDao gestorMunicipalDao){ this.gestorMunicipalDao=gestorMunicipalDao;}

    @Autowired
    public void setFranjaEspacioDao(FranjaEspacioDao franjaEspacioDao){this.franjaEspacioDao=franjaEspacioDao;}

    @Autowired
    public void setMunicipioDao(MunicipioDao municipioDao){this.municipioDao=municipioDao;}

    @RequestMapping("/listRegistrado")
    public String listRegistradoPaged(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("nuevo") Optional<String> nuevo,  HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");

        //Solo los espacios abiertos y restringidos
        List<EspacioPublico> espaciosPublicos;
        if (user.getRol().equals("ciudadano")) espaciosPublicos= espacioPublicoDao.getEspaciosPublicosNoCerrados();
        else espaciosPublicos = espacioPublicoDao.getEspaciosPublicos();
        ArrayList<ArrayList<EspacioPublico>> espaciosPaginas= new ArrayList<>();

        int ini=0;
        int fin=pageLength;
        while (fin<espaciosPublicos.size()) {
            espaciosPaginas.add(new ArrayList<>(espaciosPublicos.subList(ini, fin)));
            ini+=pageLength;
            fin+=pageLength;
        }
        espaciosPaginas.add(new ArrayList<>(espaciosPublicos.subList(ini, espaciosPublicos.size())));
        model.addAttribute("espaciosPublicosPaged", espaciosPaginas);

        // Paso 2: Crear la lista de numeros de pagina
        int totalPages = espaciosPaginas.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        // Paso 3: selectedPage: usar parametro opcional page, o en su defecto, 1
        int currentPage = page.orElse(0);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("nuevo", nuevo.orElse("None"));

        if (user.getRol().equals("gestorMunicipal")) return "espacioPublico/listGestor";
        if (user.getRol().equals("controlador")) return "espacioPublico/listControlador";

        return "espacioPublico/listConReserva";
    }



    @RequestMapping("/listSinRegistrar")
    public String listEspacioPublicoSinRegistrar(Model model, @RequestParam("page") Optional<Integer> page) {
        List<EspacioPublico> espaciosPublicos = espacioPublicoDao.getEspaciosPublicosNoCerrados();
        ArrayList<ArrayList<EspacioPublico>> espaciosPaginas= new ArrayList<>();

        int ini=0;
        int fin=pageLength;
        while (fin<espaciosPublicos.size()) {
            espaciosPaginas.add(new ArrayList<>(espaciosPublicos.subList(ini, fin)));
            ini+=pageLength;
            fin+=pageLength;
        }
        espaciosPaginas.add(new ArrayList<>(espaciosPublicos.subList(ini, espaciosPublicos.size())));
        model.addAttribute("espaciosPublicosPaged", espaciosPaginas);

        // Paso 2: Crear la lista de numeros de pagina
        int totalPages = espaciosPaginas.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        // Paso 3: selectedPage: usar parametro opcional page, o en su defecto, 1
        int currentPage = page.orElse(0);
        model.addAttribute("selectedPage", currentPage);


        return "espacioPublico/listSinRegistrar";
    }

    @RequestMapping(value="/info/{nombre}")
    public String addEspacioPublico(Model model, HttpSession session, @PathVariable String nombre ) {

        UserDetails user = (UserDetails) session.getAttribute("user");
        EspacioPublico espacioPublico = espacioPublicoDao.getEspacioPublico(nombre);
        model.addAttribute("ocupacion", espacioPublicoDao.getOcupacionActual(nombre));
        model.addAttribute("espacioPublico", espacioPublico);

        if (user.getRol().equals("ciudadano")){
            ReservaSvc reservaService = new ReservaSvc();
            reservaService.setDni(user.getUsername());
            reservaService.setNombreEspacio(nombre);
            model.addAttribute("reserva", reservaService);
            model.addAttribute("franjas",franjaEspacioDao.getFranjasEspacioNormales(nombre));
            model.addAttribute("fechaMinima", LocalDate.now().toString());
            return "espacioPublico/infoConReserva";
        }

        if(user.getRol().equals("controlador")) return "espacioPublico/infoControlador";
        GestorMunicipal gestorMunicipal= gestorMunicipalDao.getGestorMunicipal(user.getUsername());

        if(gestorMunicipal.getNombreMunicipio().equals(espacioPublico.getNombreMunicipio())) model.addAttribute("esMio","si");
        else model.addAttribute("esMio","no");

        return  "espacioPublico/infoGestor";
    }

    @RequestMapping(value="/add")
    public String addEspacioPublico(Model model, HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");

        String municipioGestor = gestorMunicipalDao.getGestorMunicipal(user.getUsername()).getNombreMunicipio();
        EspacioPublico espacioPublico = new EspacioPublico();
        espacioPublico.setNombreMunicipio(municipioGestor);
        model.addAttribute("espacioPublico", espacioPublico);
        return "espacioPublico/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("espacioPublico") EspacioPublico espacioPublico, Model model, BindingResult bindingResult) {

        String nombre = espacioPublico.getNombre();
        if(espacioPublicoDao.getEspacioPublico(espacioPublico.getNombre())!=null) espacioPublico.setNombre("USADO");
        Validator validator = new EspacioPublicoValidator();
        validator.validate(espacioPublico,bindingResult);
        if (bindingResult.hasErrors()){
            espacioPublico.setNombre(nombre);
            model.addAttribute("espacioPublico", espacioPublico);
            return "espacioPublico/add";
        }

        espacioPublicoDao.addEspacioPublico(espacioPublico);

        return "redirect:/espacioPublico/listRegistrado?nuevo="+espacioPublico.getNombre();
    }

    @RequestMapping(value="/update/{nombre}", method = RequestMethod.GET)
    public String editEspacioPublico(Model model, @PathVariable String nombre) {
        model.addAttribute("espacioPublico", espacioPublicoDao.getEspacioPublico(nombre));
        model.addAttribute("municipios", municipioDao.getMunicipios());
        return "espacioPublico/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("espacioPublico") EspacioPublico espacioPublico,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "espacioPublico/update";
        espacioPublicoDao.updateEspacioPublico(espacioPublico);
        return "redirect:listRegistrado";
    }

    @RequestMapping(value="/delete/{nombre}")
    public String processDelete(@PathVariable String nombre) {
        espacioPublicoDao.deleteEspacioPublico(nombre);
        return "redirect:/espacioPublico/listRegistrado";
    }

    @RequestMapping(value="/updateGestor/{nombre}", method = RequestMethod.GET)
    public String elegirOpcionEditar(Model model, @PathVariable String nombre) {
        model.addAttribute("espacio", espacioPublicoDao.getEspacioPublico(nombre));
        return "espacioPublico/indiceEditar";
    }
}
