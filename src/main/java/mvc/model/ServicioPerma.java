package mvc.model;


import mvc.model.enumerations.TipoServicio;

public class ServicioPerma {


    private String nombre;
    private String tipoServicio;

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

}
