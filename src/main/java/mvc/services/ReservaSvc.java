package mvc.services;

import mvc.dao.EspacioPublicoDao;
import mvc.model.Zona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaSvc implements ReservaService{
/*
    @Override
    public Map<String, List<Zona>> getClassificationByCountry(String nombre) {
        List<Zona> classProva = classificacioDao.getClassificacioProva(prova);
        List<Zona> espacioZona = EspacioPublicoDao.getZ(nombre);
        HashMap<String,List<Nadador>> nadadorsPerPais =
                new HashMap<String,List<Nadador>>();
        for (Classificacio clsf : classProva) {
            Nadador nadador = nadadorDao.getNadador(clsf.getNomNadador());
            if (!nadadorsPerPais.containsKey(nadador.getPais()))
                nadadorsPerPais.put(nadador.getPais(),
                        new ArrayList<Nadador>());
            nadadorsPerPais.get(nadador.getPais()).add(nadador);
        }
        return nadadorsPerPais;
    }
*/
}
