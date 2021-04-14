package mvc.model;


import mvc.model.enumerations.TipoServicio;

public class ServicioEstacional {


    private String nombre;
    private TipoServicio tipoServicio;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

}
