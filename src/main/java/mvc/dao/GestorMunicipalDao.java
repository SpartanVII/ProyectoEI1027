package mvc.dao;

import mvc.model.Ejemplo;
import mvc.model.GestorMunicipal;
import mvc.model.Municipio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GestorMunicipalDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addGestionarMunicipal(GestorMunicipal gestorMunicipal) {
        jdbcTemplate.update("INSERT INTO Municipio VALUES(?,?,?,?,?,?,?,?)",
                gestorMunicipal.getNombre(),gestorMunicipal.getDni(),gestorMunicipal.getTelefono(), gestorMunicipal.getCodPostal(),
                gestorMunicipal.getPais(),gestorMunicipal.getDireccion(), gestorMunicipal.getEmail(), gestorMunicipal.getNombreMunicipio());
    }


    public void deleteGestorMunicipal(String dni) {
        jdbcTemplate.update("DELETE from GestorMunicipal where dni=?",
                dni);
    }
    public void deleteGestorMunipal(GestorMunicipal gestorMunicipal) {
        jdbcTemplate.update("DELETE from GestorMunicipal where dni=?",
                gestorMunicipal.getDni());
    }

    public void updateGestorMunicipal(GestorMunicipal gestorMunicipal) {
        jdbcTemplate.update("UPDATE GestorMunicipal SET nombre=?, telefono=?, codPostal=?, pais=?, direccion=?, email=?, nombre_municipio=? where dni=?",
                gestorMunicipal.getNombre(), gestorMunicipal.getTelefono(), gestorMunicipal.getCodPostal(), gestorMunicipal.getPais(),
                gestorMunicipal.getDireccion(), gestorMunicipal.getEmail(), gestorMunicipal.getNombreMunicipio(), gestorMunicipal.getDni());
    }


    public GestorMunicipal getGestorMunicipal(String dni) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from GestorMunicipal WHERE dni=?",
                    new GestorMunicipalRowMapper(), dni);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<GestorMunicipal> getGestores() {
        try {
            return jdbcTemplate.query("SELECT * from GestorMunicipal",
                    new GestorMunicipalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

