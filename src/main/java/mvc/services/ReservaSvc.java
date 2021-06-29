package mvc.services;


import mvc.model.Reserva;
import mvc.model.Zona;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaSvc implements ReservaService{

    private Integer identificador;
    private String franja;
    private int numPersonas;
    private String estado;
    private String  fecha;
    private String dni;
    private String zona1;
    private String zona2;
    private String zona3;
    private String zona4;
    private String zonas;
    private String nombreEspacio;

    public ReservaSvc(Reserva reserva, List<Zona> zonas) {
        this.identificador = reserva.getIdentificador();
        this.franja=reserva.getHoraEntrada().toString()+" / "+reserva.getHoraSalida().toString();
        this.numPersonas=reserva.getNumPersonas();
        this.estado=reserva.getEstado();
        this.fecha = reserva.getFecha().toString();
        this.dni = reserva.getDniCiudadano();
        StringBuilder zonass= new StringBuilder();
        for(int i=0; i<zonas.size();i++){
            zonass.append(zonas.get(i).getIdentificador());
            if(i+1!=zonas.size()) zonass.append(", ");
        }
        this.zonas=zonass.toString();
        this.nombreEspacio=reserva.getNombreEspacio();
    }

    public String getZonas() {
        return  zonas;
    }

    public ReservaSvc() {
    }

    public String getEstado() {
        return estado;
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

    public String getFusionZonas() {
        StringBuilder dev = new StringBuilder();
        dev.append(zona1);
        if(zona2!=null) dev.append(",").append(zona2);
        if(zona3!=null) dev.append(",").append(zona3);
        if(zona4!=null) dev.append(",").append(zona4);

        return dev.toString();
    }

    public String getZona1() {
        return zona1;
    }

    public String getZona2() {
        return zona2;
    }

    public String getZona3() {
        return zona3;
    }

    public String getZona4() {
        return zona4;
    }

    public void setZona1(String zona) {
        this.zona1 = zona;
    }
    public void setZona2(String zona) {
        this.zona2 = zona;
    }
    public void setZona3(String zona) {
        this.zona3 = zona;
    }
    public void setZona4(String zona) {
        this.zona4 = zona;
    }

    public String getNombreEspacio() {
        return nombreEspacio;
    }

    public void setNombreEspacio(String nombreEspacio) {
        this.nombreEspacio = nombreEspacio;
    }

    @Override
    public Reserva crearReserva() {
        Reserva res = new Reserva();
        res.setIdentificador(identificador);
        res.setDniCiudadano(dni);
        res.setNumPersonas(numPersonas);
        res.setFecha(LocalDate.parse(fecha));
        res.setHoraEntrada(LocalTime.parse(franja.substring(0,5)));
        res.setHoraSalida(LocalTime.parse(franja.substring(8,13)));
        res.setNombreEspacio(nombreEspacio);
        res.setEstado("PENDIENTE_USO");
        return res;
    }

}
