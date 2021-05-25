package mvc.services;

import mvc.dao.EspacioPublicoDao;
import mvc.dao.ReservaDao;
import mvc.model.Reserva;
import mvc.model.Zona;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaSvc implements ReservaService{

    Integer identificador;
    String franja;
    int numPersonas;
    String  fecha;
    String dni;
    String zona;

    public ReservaSvc(Reserva reserva) {
        this.identificador = reserva.getIdentificador();
        this.numPersonas = reserva.getNumPersonas();
        this.fecha = reserva.getFecha().toString();
        this.dni = reserva.getDniCiudadano();
        this.zona = reserva.getIdentificadorZona();
    }

    public ReservaSvc() {
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public String getFranja() {
        return franja;
    }

    public void setFranja(String franja) {
        this.franja = franja;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    @Override
    public Reserva crearReserva() {
        Reserva res = new Reserva();
        res.setIdentificador(identificador);
        res.setDniCiudadano(dni);
        res.setNumPersonas(numPersonas);
        res.setFecha(LocalDate.parse(fecha));
        res.setIdentificadorZona(zona);
        res.setHoraEntrada(LocalTime.parse(franja.substring(0,5)));
        res.setHoraSalida(LocalTime.parse(franja.substring(8,13)));
        return res;
    }

}
