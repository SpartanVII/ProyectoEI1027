package mvc.dao;


import mvc.model.PeriodosServiciosEstacionalesEnEspacio;
import mvc.model.ServicioPerma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PeriodosServiciosEstacionalesEnEspacioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addPeriodosServiciosEstacionales(PeriodosServiciosEstacionalesEnEspacio periodosServiciosEstacionalesEnEspacio) {
        jdbcTemplate.update("INSERT INTO PeriodosServiciosEstacionalesEnEspacio VALUES(?,?,?,?)",
                periodosServiciosEstacionalesEnEspacio.getFechaInicio(), periodosServiciosEstacionalesEnEspacio.getFechaFin(),
                periodosServiciosEstacionalesEnEspacio.getNombreEspacioPublico(), periodosServiciosEstacionalesEnEspacio.getNombreServicioEstacional());
    }



    public void deletePeriodosServiciosEstacionales(PeriodosServiciosEstacionalesEnEspacio periodosServiciosEstacionalesEnEspacio) {
        jdbcTemplate.update("DELETE from PeriodosServiciosEstacionalesEnEspacio where fechaInicio=? and fechaFin=?",
                periodosServiciosEstacionalesEnEspacio.getFechaInicio(), periodosServiciosEstacionalesEnEspacio.getFechaFin());
    }

    /*No es util
    public ServicioPerma getPeriodosServiciosEstacionales(String nombre) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from ServicioPerma WHERE nombre=?",
                    new ServicioPermaRowMapper(), nombre);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }
    */

    public List<PeriodosServiciosEstacionalesEnEspacio> getPeriodosServiciosEstacionales() {
        try {
            return jdbcTemplate.query("SELECT * from PeriodosServiciosEstacionalesEnEspacio",
                    new PeriodosServiciosEstacionalesEnEspacioRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

