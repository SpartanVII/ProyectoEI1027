package mvc.dao;


import mvc.model.Municipio;
import mvc.model.Notificacion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class NotificacionRowMapper implements
        RowMapper<Notificacion> {

    public Notificacion mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdentificador(rs.getInt("identificador"));
        notificacion.setDniCiudadano(rs.getString("dni_ciudadano"));
        notificacion.setMensaje(rs.getString("mensaje"));

        return notificacion;
    }
}
