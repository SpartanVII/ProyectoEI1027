package mvc.dao;


import mvc.model.FranjaEspacio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public final class FranjaEspacioRowMapper implements
        RowMapper<FranjaEspacio> {

    public  FranjaEspacio mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        FranjaEspacio franjaEspacio = new  FranjaEspacio();
        franjaEspacio.setHoraEntrada(rs.getObject("horaEntrada", LocalDateTime.class));
        franjaEspacio.setHoraSalida(rs.getObject("horaSalida", LocalDateTime.class));
        return franjaEspacio;
    }
}
