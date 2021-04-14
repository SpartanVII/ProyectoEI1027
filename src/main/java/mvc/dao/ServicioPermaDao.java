package mvc.dao;


import mvc.model.ServicioPerma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServicioPermaDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addServicioPerma(ServicioPerma servicioPerma) {
        jdbcTemplate.update("INSERT INTO ServicioPerma VALUES(?,?)",
                servicioPerma.getNombre(), servicioPerma.getTipoServicio());
    }


    public void deleteServicioPerma(String nombre) {
        jdbcTemplate.update("DELETE from ServicioPerma where nombre=?",
                nombre);
    }
    public void deleteServicioPerma(ServicioPerma servicioPerma) {
        jdbcTemplate.update("DELETE from ServicioPerma where nombre=?",
                servicioPerma.getNombre());
    }

    public void updateServicioPerma(ServicioPerma servicioPerma) {
        jdbcTemplate.update("UPDATE ServicioPerma SET tipoServicio=? where nombre=?",
                servicioPerma.getTipoServicio(), servicioPerma.getNombre());
    }


    public ServicioPerma getServicioPerma(String nombre) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from ServicioPerma WHERE nombre=?",
                    new ServicioPermaRowMapper(), nombre);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<ServicioPerma> getGestores() {
        try {
            return jdbcTemplate.query("SELECT * from ServicioPerma",
                    new ServicioPermaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

