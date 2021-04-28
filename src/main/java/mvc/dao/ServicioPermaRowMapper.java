package mvc.dao;


import mvc.model.ServicioPerma;
import mvc.model.enumerations.TipoServicio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ServicioPermaRowMapper implements
        RowMapper<ServicioPerma> {

    public ServicioPerma mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        ServicioPerma servicioPerma = new ServicioPerma();
        servicioPerma.setNombre(rs.getString("nombre"));
        servicioPerma.setTipoServicio(rs.getString("tipoServicio"));
        return servicioPerma;
    }
}
