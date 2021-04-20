package mvc.model;

import mvc.model.enumerations.EstadoReserva;

import java.util.Date;

public class Reserva {

    private String identificador;
    private int numPersonas;
    private Date fecha;
    private EstadoReserva estado;
    private String dniCIudadano;

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public String getDniCIudadano() {
        return dniCIudadano;
    }

    public void setDniCIudadano(String dniCIudadano) {
        this.dniCIudadano = dniCIudadano;
    }
}
