package mvc.dao;


import mvc.model.FranjaEspacio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FranjaEspacioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addFranjaEspacio(FranjaEspacio franjaEspacio) {
        jdbcTemplate.update("INSERT INTO FranjaEspacio VALUES(?,?)",
                franjaEspacio.getHoraEntrada(), franjaEspacio.getHoraSalida());
    }


    public void deleteFranjaEspacio(FranjaEspacio franjaEspacio) {
        jdbcTemplate.update("DELETE from FranjaEspacio WHERE horaEntrada=? and horaSalida=?",
                franjaEspacio.getHoraEntrada(), franjaEspacio.getHoraSalida());
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

    public List<FranjaEspacio> getFranjaEspacioList() {
        try {
            return jdbcTemplate.query("SELECT * from FranjaEspacio",
                    new FranjaEspacioRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

