package mvc.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

public class Ejemplo {
    private String nomNadador;
    private String nomProva;
    @DateTimeFormat(pattern = "HH:mm:ss.SSS")
    private LocalTime temps;
    private int posicio;

    public String getNomNadador() {
        return nomNadador;
    }

    public void setNomNadador(String nomNadador) {
        this.nomNadador = nomNadador;
    }

    public String getNomProva() {
        return nomProva;
    }

    public void setNomProva(String nomProva) {
        this.nomProva = nomProva;
    }

    public LocalTime getTemps() {
        return temps;
    }

    public void setTemps(LocalTime temps) {
        this.temps = temps;
    }

    public int getPosicio() {
        return posicio;
    }

    public void setPosicio(int posicio) {
        this.posicio = posicio;
    }
}
