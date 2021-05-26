package mvc.model;

public class UserDetails {
    private String username;
    private String password;
    private String nombre;
    private String rol;
    private String gamil;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGamil() {
        return gamil;
    }

    public void setGamil(String gamil) {
        this.gamil = gamil;
    }
}