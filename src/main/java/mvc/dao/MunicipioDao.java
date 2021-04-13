package mvc.dao;

import mvc.model.Ejemplo;
import mvc.model.Municipio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MunicipioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addMunicipio(Municipio municipio) {
        jdbcTemplate.update("INSERT INTO Municipio VALUES(?)",
                municipio.getNombre());
    }


    public void deleteMunicipio(String municipio) {
        jdbcTemplate.update("DELETE from Municipio where nombre=?",
                municipio);
    }
    public void deleteMunicipio(Municipio municipio) {
        jdbcTemplate.update("DELETE from Municipio where nombre=?",
                municipio.getNombre());
    }



    public Municipio getMunicipio(String municipio) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Municipio WHERE nombre=?",
                    new MunicipioRowMapper(), municipio);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Municipio> getMunicipios() {
        try {
            return jdbcTemplate.query("SELECT * from Municipio",
                    new MunicipioRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

