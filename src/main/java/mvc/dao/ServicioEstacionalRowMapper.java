package mvc.dao;


import mvc.model.ServicioEstacional;
import mvc.model.enumerations.TipoServicio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ServicioEstacionalRowMapper implements
        RowMapper<ServicioEstacional> {

    public ServicioEstacional mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        ServicioEstacional servicioEstacional = new ServicioEstacional();
        servicioEstacional.setNombre(rs.getString("nombre"));
        servicioEstacional.setTipoServicio(rs.getObject("tipoServicio", TipoServicio.class));
        return servicioEstacional;
    }
}
