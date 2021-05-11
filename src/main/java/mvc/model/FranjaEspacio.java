package mvc.model;


import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class FranjaEspacio {

    @DateTimeFormat(pattern = "HH:mm")
    private LocalDateTime horaEntrada;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalDateTime horaSalida;

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }
}
