package mvc.dao;


import mvc.model.Zona;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ZonaRowMapper implements
        RowMapper<Zona> {

    public Zona mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Zona zona = new Zona();
        zona.setIdentificador(rs.getString("identificador"));
        zona.setCapMaxima(rs.getInt("capMaxima"));
        zona.setNombreEspacio(rs.getString("nombre_espacioPublico"));
        zona.setNombreEspacio(rs.getString("descripcion"));
        return zona;
    }
}
