package mvc.dao;


import mvc.model.GestorMunicipal;
import mvc.model.Municipio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class GestorMunicipalRowMapper implements
        RowMapper<GestorMunicipal> {

    public GestorMunicipal mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        GestorMunicipal gestorMunicipal = new GestorMunicipal();
        gestorMunicipal.setNombre(rs.getString("nombre"));
        gestorMunicipal.setDni(rs.getString("dni"));
        gestorMunicipal.setTelefono(rs.getString("telefono"));
        gestorMunicipal.setCodPostal(rs.getString("codPostal"));
        gestorMunicipal.setPais(rs.getString("pais"));
        gestorMunicipal.setDireccion(rs.getString("direccion"));
        gestorMunicipal.setEmail(rs.getString("email"));
        gestorMunicipal.setNombreMunicipio(rs.getString("nombre_municipio"));
        gestorMunicipal.setPin(rs.getString("pin"));
        return gestorMunicipal;
    }
}
