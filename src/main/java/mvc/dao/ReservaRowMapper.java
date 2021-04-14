package mvc.dao;


import mvc.model.Reserva;
import mvc.model.enumerations.EstadoReserva;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ReservaRowMapper implements
        RowMapper<Reserva> {

    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Reserva reserva = new Reserva();
        reserva.setCodigo(rs.getString("codigo"));
        reserva.setNumPersonas(rs.getInt("numPersonas"));
        reserva.setFecha(rs.getDate("fecha"));
        reserva.setEstado(rs.getObject("estado", EstadoReserva.class));
        return reserva;
    }
}
