package mvc.dao;


import mvc.model.ServicioEstacional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServicioEstacionalDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addServicioEstacional(ServicioEstacional servicioEstacional) {
        jdbcTemplate.update("INSERT INTO ServicioEstacional VALUES(?,?)",
                servicioEstacional.getNombre(), servicioEstacional.getTipoServicio());
    }


    public void deleteServicioEstacional(String nombre) {
        jdbcTemplate.update("DELETE from ServicioEstacional where nombre=?",
                nombre);
    }
    public void deleteServicioEstacional(ServicioEstacional servicioEstacional) {
        jdbcTemplate.update("DELETE from ServicioEstacional where nombre=?",
                servicioEstacional.getNombre());
    }

    public void updateServicioEstacional(ServicioEstacional servicioEstacional) {
        jdbcTemplate.update("UPDATE ServicioEstacional SET tipoServicio=? where nombre=?",
                servicioEstacional.getTipoServicio(),servicioEstacional.getNombre());
    }


    public ServicioEstacional getServicioEstacional(String nombre) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from ServicioEstacional WHERE nombre=?",
                    new ServicioEstacionalRowMapper(), nombre);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<ServicioEstacional> getGestores() {
        try {
            return jdbcTemplate.query("SELECT * from ServicioEstacional",
                    new ServicioEstacionalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

