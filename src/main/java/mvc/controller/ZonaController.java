package mvc.controller;

import mvc.dao.GestorMunicipalDao;
import mvc.dao.ZonaDao;
import mvc.model.EspacioPublico;
import mvc.model.GestorMunicipal;
import mvc.model.Zona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/zona")
public class ZonaController {
    private ZonaDao zonaDao;
    private int pageLength=6;
    @Autowired
    public void setZonaDao(ZonaDao zonaDao) {
        this.zonaDao=zonaDao;
    }

    // Operacions: Crear, llistar, actualitzar, esborrar
    // ...
    @RequestMapping("/list")
    public String listZonas(Model model) {
        model.addAttribute("zonas", zonaDao.getZonas());
        return "zona/list";
    }

    @RequestMapping(value="/add")
    public String addZona(Model model) {
        model.addAttribute("zona", new Zona());
        return "zona/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("zona") Zona zona, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "zona/add";
        zonaDao.addZona(zona);
        return "redirect:list";
    }

    @RequestMapping(value="/update/{identificador}", method = RequestMethod.GET)
    public String editGestor(Model model, @PathVariable String identificador) {
        model.addAttribute("zona", zonaDao.getZona(identificador));
        return "zona/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("zona") Zona zona, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "zona/update";
        zonaDao.updateZona(zona);
        return "redirect:list";
    }

    @RequestMapping(value="/delete/{identificador}")
    public String processDelete(@PathVariable String identificador) {
        zonaDao.deleteZona(identificador);
        return "redirect:list";
    }

    @RequestMapping(value="/particular/{nombre}")
    public String listaZonasEspacio(Model model, @RequestParam("page") Optional<Integer> page, @PathVariable String nombre) {

        List<Zona> zonas = zonaDao.getZonasEspacio(nombre);
        System.out.println(nombre);
        ArrayList<ArrayList<Zona>> zonasPaged= new ArrayList<>();

        int ini=0;
        int fin=pageLength-1;
        while (fin<zonas.size()) {
            zonasPaged.add(new ArrayList<>(zonas.subList(ini, fin)));
            ini+=pageLength;
            fin+=pageLength;
        }
        zonasPaged.add(new ArrayList<>(zonas.subList(ini, zonas.size())));
        model.addAttribute("zonasPaged", zonasPaged);

        // Paso 2: Crear la lista de numeros de pagina
        int totalPages = zonasPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        // Paso 3: selectedPage: usar parametro opcional page, o en su defecto, 1
        int currentPage = page.orElse(0);
        model.addAttribute("selectedPage", currentPage);
        model.addAttribute("zonas", zonasPaged);
        model.addAttribute("nombreEspacio", zonas.get(0).getNombreEspacio());
        return "zona/list";
    }

}
