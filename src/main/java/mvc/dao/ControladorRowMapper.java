package mvc.dao;


import mvc.model.Controlador;
import mvc.model.GestorMunicipal;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ControladorRowMapper implements
        RowMapper<Controlador> {

    public Controlador mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Controlador controlador = new Controlador();
        controlador.setNombre(rs.getString("nombre"));
        controlador.setEdad(rs.getInt("edad"));
        controlador.setDni(rs.getString("dni"));
        controlador.setTelefono(rs.getString("telefono"));
        controlador.setCodPostal(rs.getString("codPostal"));
        controlador.setPais(rs.getString("pais"));
        controlador.setDireccion(rs.getString("direccion"));
        controlador.setEmail(rs.getString("email"));
        controlador.setEmail(rs.getString("pin"));
        return controlador;
    }
}
