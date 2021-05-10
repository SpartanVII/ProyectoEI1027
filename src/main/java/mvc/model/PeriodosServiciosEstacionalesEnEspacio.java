package mvc.model;


import java.util.Date;

public class PeriodosServiciosEstacionalesEnEspacio {


    private Date fechaInicio;
    private Date fechaFin;
    private String nombreEspacioPublico;
    private String nombreServicioEstacional;

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNombreEspacioPublico() {
        return nombreEspacioPublico;
    }

    public void setNombreEspacioPublico(String nombreEspacioPublico) {
        this.nombreEspacioPublico = nombreEspacioPublico;
    }

    public String getNombreServicioEstacional() {
        return nombreServicioEstacional;
    }

    public void setNombreServicioEstacional(String nombreServicioEstacional) {
        this.nombreServicioEstacional = nombreServicioEstacional;
    }
}
