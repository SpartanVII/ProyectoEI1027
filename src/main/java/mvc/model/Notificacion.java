package mvc.model;

public class Notificacion {

    private String dniCiudadano;
    private String mensaje;

    public Notificacion() {
    }

    public Notificacion(String dniCiudadano, String mensaje) {
        this.dniCiudadano = dniCiudadano;
        this.mensaje = mensaje;
    }

    public Notificacion(String dniCiudadano) {
        this.dniCiudadano = dniCiudadano;
        this.mensaje ="";
    }

    public String getDniCiudadano() {
        return dniCiudadano;
    }

    public void setDniCiudadano(String dniCiudadano) {
        this.dniCiudadano = dniCiudadano;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
