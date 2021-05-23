package mvc.dao;

import mvc.model.Municipio;
import mvc.model.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NotificacionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addNotificacion(Notificacion notificacion) {
        jdbcTemplate.update("INSERT INTO Notificaciones VALUES(?,?,?)",
                notificacion.getIdentificador(), notificacion.getDniCiudadano(), notificacion.getMensaje());
    }


    public void deleteNotificacion(Integer id) {
        jdbcTemplate.update("DELETE from Notificaciones where identificador=?",
                id);
    }

    public void deleteNotificacion(Notificacion notificacion) {
        jdbcTemplate.update("DELETE from Notificaciones where identificador=?",
                notificacion.getIdentificador());
    }


    public List<Notificacion> getMisNotificaciones(String dni) {
        try {
            return jdbcTemplate.query("SELECT * from Notificaciones WHERE dni_ciudadano=?",
                    new NotificacionRowMapper(),dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Integer getSiguienteIdentificadorNotificacion() {
        try {
            Notificacion notificacion = jdbcTemplate.queryForObject("SELECT * from Notificaciones ORDER BY identificador DESC LIMIT 1",
                    new NotificacionRowMapper());
            assert notificacion != null;
            return notificacion.getIdentificador() + 1;
        } catch (EmptyResultDataAccessException e) {
            return 1;
        } catch (Exception e) {
            return null;
        }
    }

}

