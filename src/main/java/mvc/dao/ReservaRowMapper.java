package mvc.dao;


import mvc.model.Reserva;
import mvc.model.enumerations.EstadoReserva;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public final class ReservaRowMapper implements
        RowMapper<Reserva> {

    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Reserva reserva = new Reserva();
        reserva.setIdentificador(rs.getString("identificador"));
        reserva.setNumPersonas(rs.getInt("numPersonas"));
        reserva.setFecha(rs.getObject("fecha", LocalDate.class));
        reserva.setEstado(rs.getObject("estado", EstadoReserva.class));
        reserva.setDniCIudadano(rs.getString("dni_ciudadano"));
        reserva.setHoraEntrada(rs.getObject("horaEntrada", LocalTime.class));
        reserva.setHoraSalida(rs.getObject("horaSalida", LocalTime.class));
        return reserva;
    }
}
