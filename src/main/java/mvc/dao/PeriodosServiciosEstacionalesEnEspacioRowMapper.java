package mvc.dao;


import mvc.model.PeriodosServiciosEstacionalesEnEspacio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class PeriodosServiciosEstacionalesEnEspacioRowMapper implements
        RowMapper<PeriodosServiciosEstacionalesEnEspacio> {

    public PeriodosServiciosEstacionalesEnEspacio mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        PeriodosServiciosEstacionalesEnEspacio periodosServiciosEstacionalesEnEspacio = new PeriodosServiciosEstacionalesEnEspacio();
        periodosServiciosEstacionalesEnEspacio.setFechaInicio(rs.getDate("fechaInicio"));
        periodosServiciosEstacionalesEnEspacio.setFechaFin(rs.getDate("fechaFin"));
        periodosServiciosEstacionalesEnEspacio.setNombreEspacioPublico(rs.getString("nombre_espacioPublico"));
        periodosServiciosEstacionalesEnEspacio.setNombreServicioEstacional(rs.getString("nombre_servicioEstacional"));
        return periodosServiciosEstacionalesEnEspacio;
    }
}
