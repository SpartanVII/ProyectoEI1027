package mvc.services;

import mvc.dao.EspacioPublicoDao;
import mvc.dao.ReservaDao;
import mvc.model.Reserva;
import mvc.model.Zona;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaSvc implements ReservaService{

    @Autowired
    ReservaDao reservaDao;

    String dni;
    String zona;
    String franja;
    int numPersonas;
    private LocalDate fecha;

    @Override
    public Reserva crearReserva(String dni, String zona) {
        List<Reserva> reservas = new ArrayList<>();
        Reserva res = new Reserva();
        for(Reserva r: reservaDao.getReservas()){
            if(r.getDniCiudadano().equals(dni))
                reservas.add(r);
        }
        return res;
    }

}
