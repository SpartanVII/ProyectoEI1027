package mvc.model;

import mvc.model.enumerations.TipoAcceso;
import mvc.model.enumerations.TipoTerreno;

public class EspacioPublico {

    private String nombre;
    private String descripcion;
    private String localizacionGeografica;
    private int ocupacion;
    private int longitud;
    private int amplitud;
    private String orientacion;
    private String comentario;
    private TipoTerreno tipoTerreno;
    private TipoAcceso tipoAcceso;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLocalizacionGeografica() {
        return localizacionGeografica;
    }

    public void setLocalizacionGeografica(String localizacionGeografica) {
        this.localizacionGeografica = localizacionGeografica;
    }

    public int getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(int ocupacion) {
        this.ocupacion = ocupacion;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public int getAmplitud() {
        return amplitud;
    }

    public void setAmplitud(int amplitud) {
        this.amplitud = amplitud;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public TipoTerreno getTipoTerreno() {
        return tipoTerreno;
    }

    public void setTipoTerreno(TipoTerreno tipoTerreno) {
        this.tipoTerreno = tipoTerreno;
    }

    public TipoAcceso getTipoAcceso() {
        return tipoAcceso;
    }

    public void setTipoAcceso(TipoAcceso tipoAcceso) {
        this.tipoAcceso = tipoAcceso;
    }
}
