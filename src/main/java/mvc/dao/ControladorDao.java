package mvc.dao;

import mvc.model.Controlador;
import mvc.model.GestorMunicipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ControladorDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addControlador(Controlador controlador) {
        jdbcTemplate.update("INSERT INTO Controlador VALUES(?,?,?,?,?,?,?,?,?)",
                controlador.getNombre(), controlador.getEdad(), controlador.getDni(), controlador.getTelefono(),
                controlador.getCodPostal(), controlador.getPais(), controlador.getDireccion(),
                controlador.getEmail(), controlador.getPin());
    }


    public void deleteControlador(String dni) {
        jdbcTemplate.update("DELETE from Controlador where dni=?",
                dni);
    }
    public void deleteControlador(Controlador controlador) {
        jdbcTemplate.update("DELETE from Controlador where dni=?",
                controlador.getDni());
    }

    public void updateControlador(Controlador controlador) {
        jdbcTemplate.update("UPDATE Controlador SET nombre=?, edad=?, telefono=?, codPostal=?, pais=?, direccion=?, email=?, pin=? where dni=?",
                controlador.getNombre(), controlador.getEdad(), controlador.getTelefono(), controlador.getCodPostal(), controlador.getPais(),
                controlador.getDireccion(), controlador.getEmail(), controlador.getDni(), controlador.getPin());
    }


    public Controlador getControlador(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Controlador WHERE dni=?",
                    new ControladorRowMapper(), dni);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Controlador getControlador(String dni, String pin) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Controlador WHERE dni=? AND pin=?",
                    new ControladorRowMapper(), dni, pin);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Controlador> getControladores() {
        try {
            return jdbcTemplate.query("SELECT * from Controlador",
                    new ControladorRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

