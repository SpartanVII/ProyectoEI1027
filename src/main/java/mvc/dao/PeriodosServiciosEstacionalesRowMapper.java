package mvc.dao;


import mvc.model.PeriodosServiciosEstacionales;
import mvc.model.ServicioPerma;
import mvc.model.enumerations.TipoServicio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public final class PeriodosServiciosEstacionalesRowMapper implements
        RowMapper<PeriodosServiciosEstacionales> {

    public PeriodosServiciosEstacionales mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        PeriodosServiciosEstacionales periodosServiciosEstacionales = new PeriodosServiciosEstacionales();
        periodosServiciosEstacionales.setFechaInicio(rs.getObject("fechaInicio", Date.class));
        periodosServiciosEstacionales.setFechaFin(rs.getDate("fechaFin"));
        return periodosServiciosEstacionales;
    }
}
