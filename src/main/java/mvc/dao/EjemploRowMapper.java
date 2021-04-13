package mvc.dao;

import mvc.model.Ejemplo;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public final class EjemploRowMapper implements
        RowMapper<Ejemplo> {

    public Ejemplo mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Ejemplo classificacio = new Ejemplo();
        classificacio.setNomNadador(rs.getString("nom_nadador"));
        classificacio.setNomProva(rs.getString("nom_prova"));
        classificacio.setPosicio(rs.getInt("posicio"));
        classificacio.setTemps(rs.getObject("temps", LocalTime.class));
        return classificacio;
    }
}
