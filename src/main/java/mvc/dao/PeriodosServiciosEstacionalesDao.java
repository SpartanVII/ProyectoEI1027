package mvc.dao;


import mvc.model.PeriodosServiciosEstacionales;
import mvc.model.ServicioPerma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PeriodosServiciosEstacionalesDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addPeriodosServiciosEstacionales(PeriodosServiciosEstacionales periodosServiciosEstacionales) {
        jdbcTemplate.update("INSERT INTO ServicioPerma VALUES(?,?)",
                periodosServiciosEstacionales.getFechaInicio(), periodosServiciosEstacionales.getFechaFin());
    }



    public void deletePeriodosServiciosEstacionales(PeriodosServiciosEstacionales periodosServiciosEstacionales) {
        jdbcTemplate.update("DELETE from PeriodosServiciosEstacionales where fechaInicio=? and fechaFin=?",
                periodosServiciosEstacionales.getFechaInicio(), periodosServiciosEstacionales.getFechaFin());
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

    public List<PeriodosServiciosEstacionales> getPeriodosServiciosEstacionales() {
        try {
            return jdbcTemplate.query("SELECT * from PeriodosServiciosEstacionales",
                    new PeriodosServiciosEstacionalesRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

