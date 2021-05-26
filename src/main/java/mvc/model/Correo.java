package mvc.model;

public class Correo {

    private String enviaCorreo;
    private String recibeCorreo;
    private String contrasena;
    private String asunto;
    private String mensaje;

    public Correo(String recibeCorreo, String asunto, String mensaje) {
        this.recibeCorreo = recibeCorreo;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.contrasena="Contrasena1";
        this.enviaCorreo="proyectoei1027@gmail.com";
    }

    public String getEnviaCorreo() {
        return enviaCorreo;
    }

    public void setEnviaCorreo(String enviaCorreo) {
        this.enviaCorreo = enviaCorreo;
    }

    public String getRecibeCorreo() {
        return recibeCorreo;
    }

    public void setRecibeCorreo(String recibeCorreo) {
        this.recibeCorreo = recibeCorreo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
