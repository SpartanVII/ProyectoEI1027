package mvc.model;

import mvc.model.enumerations.EstadoReserva;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    private String identificador;
    private int numPersonas;
    private LocalDate fecha;
    private EstadoReserva estado;
    private String dniCIudadano;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;


    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
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

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
