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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/espacioPublico")
public class EspacioPublicoController {

    private EspacioPublicoDao espacioPublicoDao;
    private int pageLength = 6;

    @Autowired
    public void setEspacioPublicoDao(EspacioPublicoDao espacioPublicoDao) {
        this.espacioPublicoDao=espacioPublicoDao;
    }

    @RequestMapping("/listRegistrado")
    public String listNadadorsPaged(Model model, @RequestParam("page") Optional<Integer> page,  HttpSession session) {

        UserDetails user = (UserDetails) session.getAttribute("user");

        //Solo los espacios abiertos y restringidos
        List<EspacioPublico> espaciosPublicos;
        if (user.getRol().equals("ciudadano")) espaciosPublicos= espacioPublicoDao.getEspaciosPublicosNoCerrados();
        else espaciosPublicos = espacioPublicoDao.getEspaciosPublicos();

        ArrayList<ArrayList<EspacioPublico>> espaciosPaginas= new ArrayList<>();

        int ini=0;
        int fin=pageLength-1;
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


        if (user.getRol().equals("gestorMunicipal")) return "espacioPublico/listGestor";

        if (user.getRol().equals("controlador")) return "espacioPublico/listControlador";

        return "espacioPublico/listConReserva";
    }



    @RequestMapping("/listSinRegistrar")
    public String listEspacioPublicoSinRegistrar(Model model, @RequestParam("page") Optional<Integer> page) {
        List<EspacioPublico> espaciosPublicos = espacioPublicoDao.getEspaciosPublicosNoCerrados();
        ArrayList<ArrayList<EspacioPublico>> espaciosPaginas= new ArrayList<>();

        int ini=0;
        int fin=pageLength-1;
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
        return "redirect:listRegistrado";
    }

    @RequestMapping(value="/delete/{nombre}")
    public String processDelete(@PathVariable String nombre) {
        espacioPublicoDao.deleteEspacioPublico(nombre);
        return "redirect:../list";
    }

    @RequestMapping(value="/updateGestor/{nombre}", method = RequestMethod.GET)
    public String elegirOpcionEditar(Model model, @PathVariable String nombre) {
        model.addAttribute("espacio", espacioPublicoDao.getEspacioPublico(nombre));
        return "gestorMunicipal/indiceEditar";
    }
}
