package mvc.services;

import mvc.model.PeriodosServiciosEstacionalesEnEspacio;
import mvc.model.ServicioEstacional;

public class ServicioEstacionalCompleto {

    private String nombre;
    private String tipoServicio;
    private String duracion;

    public ServicioEstacionalCompleto(ServicioEstacional servicioEstacional, PeriodosServiciosEstacionalesEnEspacio per){
        this.nombre=servicioEstacional.getNombre();
        this.tipoServicio=servicioEstacional.getTipoServicio();
        this.duracion=per.getFechaInicio().toString()+" al "+per.getFechaFin().toString();

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
