package mvc.model;


import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public class PeriodosServiciosEstacionalesEnEspacio {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaInicio;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaFin;
    private String nombreEspacioPublico;
    private String nombreServicioEstacional;

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
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
