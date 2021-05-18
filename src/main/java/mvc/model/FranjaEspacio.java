package mvc.model;


import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class FranjaEspacio {

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaEntrada;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaSalida;
    private String nombreEspacio;

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getNombreEspacio() {
        return nombreEspacio;
    }

    public void setNombreEspacio(String nombreEspacio) {
        this.nombreEspacio = nombreEspacio;
    }

    @Override
    public String toString() {
        return horaEntrada+" / "+horaSalida;
    }
}
