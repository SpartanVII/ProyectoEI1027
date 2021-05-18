package mvc.dao;

import mvc.model.Zona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ZonaDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addZona(Zona zona) {
        jdbcTemplate.update("INSERT INTO Zona VALUES(?,?,?,?)",
                zona.getIdentificador(), zona.getDescripcion() ,zona.getCapMaxima(), zona.getNombreEspacio());
    }


    public void deleteZona(String identificador) {
        jdbcTemplate.update("DELETE from Zona where identificador=?",
                identificador);
    }
    public void deleteZona(Zona zona) {
        jdbcTemplate.update("DELETE from Zona where identificador=?",
                zona.getIdentificador());
    }

    public void updateZona(Zona zona) {
        jdbcTemplate.update("UPDATE Zona SET descripcion=?, capMaxima=?, nombre_espacioPublico=?, where identificador=?",
                zona.getDescripcion(), zona.getCapMaxima(), zona.getIdentificador());
    }

    public Zona getZona(String identificador) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Zona WHERE identificador=?",
                    new ZonaRowMapper(), identificador);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Zona> getZonas() {
        try {
            return jdbcTemplate.query("SELECT * from Zona",
                    new ZonaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Zona> getZonasEspacio(String nombreEspacio) {
        try {
            return this.jdbcTemplate.query(
                    "SELECT * FROM zona WHERE nombre_espacioPublico=?",
                    new Object[] {nombreEspacio}, new ZonaRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Zona>();
        }
    }

}

