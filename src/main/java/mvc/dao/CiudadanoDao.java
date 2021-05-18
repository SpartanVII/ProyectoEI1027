package mvc.dao;

import mvc.model.Ciudadano;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CiudadanoDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addCiudadano(Ciudadano ciudadano) {
        jdbcTemplate.update("INSERT INTO Ciudadano VALUES(?,?,?,?,?,?,?,?,?,?)",
                ciudadano.getNombre(),ciudadano.getEdad(), ciudadano.getDni(), ciudadano.getTelefono(), ciudadano.getCodPostal(),
                ciudadano.getPais(), ciudadano.getDireccion(), ciudadano.getEmail(), ciudadano.getCiudad(), ciudadano.getPin());
    }


    public void deleteCiudadano(String dni) {
        jdbcTemplate.update("DELETE from Ciudadano where dni=?",
                dni);
    }
    public void deleteCiudadano(Ciudadano ciudadano) {
        jdbcTemplate.update("DELETE from Ciudadano where dni=?",
                ciudadano.getDni());
    }

    public void updateCiudadano(Ciudadano ciudadano) {
        jdbcTemplate.update("UPDATE Ciudadano SET nombre=?, edad=?, telefono=?, codPostal=?, pais=?, direccion=?, " +
                            "email=?, ciudad=?, pin=? where dni=?",
                ciudadano.getNombre(), ciudadano.getEdad(), ciudadano.getTelefono(), ciudadano.getCodPostal(), ciudadano.getPais(), ciudadano.getDireccion(),
                ciudadano.getEmail(), ciudadano.getCiudad(), ciudadano.getPin(), ciudadano.getDni());
    }


    public Ciudadano getCiudadano(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Ciudadano WHERE dni=?",
                    new CiudadanoRowMapper(), dni);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Ciudadano getCiudadano(String dni, String pin) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Ciudadano WHERE dni=? AND pin=?",
                    new CiudadanoRowMapper(), dni, pin);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Ciudadano> getCiudadanos() {
        try {
            return jdbcTemplate.query("SELECT * from Ciudadano",
                    new CiudadanoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

