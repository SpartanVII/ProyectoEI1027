package mvc.dao;


import mvc.model.Ciudadano;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CiudadanoRowMapper implements
        RowMapper<Ciudadano> {

    public Ciudadano mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Ciudadano ciudadano = new Ciudadano();
        ciudadano.setNombre(rs.getString("nombre"));
        ciudadano.setEdad(rs.getInt("edad"));
        ciudadano.setDni(rs.getString("dni"));
        ciudadano.setTelefono(rs.getString("telefono"));
        ciudadano.setCodPostal(rs.getString("codPostal"));
        ciudadano.setPais(rs.getString("pais"));
        ciudadano.setDireccion(rs.getString("direccion"));
        ciudadano.setEmail(rs.getString("email"));
        ciudadano.setCiudad(rs.getString("ciudad"));
        ciudadano.setPin(rs.getString("pin"));
        return ciudadano;
    }
}
