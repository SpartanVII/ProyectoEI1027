package mvc.dao;


import mvc.model.Reserva;
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
        reserva.setIdentificador(rs.getInt("identificador"));
        reserva.setNumPersonas(rs.getInt("numPersonas"));
        reserva.setFecha(rs.getObject("fecha", LocalDate.class));
        reserva.setEstado(rs.getString("estado"));
        reserva.setDniCiudadano(rs.getString("dni_ciudadano"));
        reserva.setHoraEntrada(rs.getObject("horaEntrada", LocalTime.class));
        reserva.setHoraSalida(rs.getObject("horaSalida", LocalTime.class));
        reserva.setNombreEspacio(rs.getString("nombreEspacio"));
        return reserva;
    }
}
