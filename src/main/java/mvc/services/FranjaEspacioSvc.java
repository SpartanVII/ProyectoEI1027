package mvc.services;

import mvc.model.FranjaEspacio;

import java.time.LocalTime;

public class FranjaEspacioSvc implements FranjaEspacioService {

    private String nombreEspacio;
    private String entrada;
    private String salida;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    public FranjaEspacioSvc(){}

    public FranjaEspacioSvc(FranjaEspacio franjaEspacio){
        this.nombreEspacio=franjaEspacio.getNombreEspacio();
        this.entrada=franjaEspacio.getHoraEntrada().toString();
        this.salida=franjaEspacio.getHoraSalida().toString();

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

    public String getNombreEspacio() {
        return nombreEspacio;
    }

    public void setNombreEspacio(String nombreEspacio) {
        this.nombreEspacio = nombreEspacio;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getEntradaSalida(){return entrada+"/"+salida;}


    @Override
    public FranjaEspacio crearFranjaEspacio(){
        FranjaEspacio franjaEspacio=new FranjaEspacio();
        franjaEspacio.setNombreEspacio(nombreEspacio);
        franjaEspacio.setHoraEntrada(horaEntrada);
        franjaEspacio.setHoraSalida(horaSalida);
        return franjaEspacio;
    }


}
