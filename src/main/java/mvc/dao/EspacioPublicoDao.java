package mvc.dao;


import mvc.model.EspacioPublico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EspacioPublicoDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addEspacioPublico(EspacioPublico espacioPublico) {
        jdbcTemplate.update("INSERT INTO EspacioPublico VALUES(?,?,?,?,?,?,?,?,?,?,?)",
                espacioPublico.getNombre(), espacioPublico.getDescripcion(), espacioPublico.getLocalizacionGeografica(),
                espacioPublico.getOcupacion(), espacioPublico.getLongitud(), espacioPublico.getAmplitud(), espacioPublico.getOrientacion(),
                espacioPublico.getComentario(), espacioPublico.getTipoTerreno(), espacioPublico.getTipoAcceso(), espacioPublico.getNombreMunicipio());
    }


    public void deleteEspacioPublico(String nombre) {
        jdbcTemplate.update("DELETE from EspacioPublico where nombre=?",
                nombre);
    }
    public void deleteEspacioPublico(EspacioPublico espacioPublico) {
        jdbcTemplate.update("DELETE from EspacioPublico where nombre=?",
                espacioPublico.getNombre());
    }

    public void updateEspacioPublico(EspacioPublico espacioPublico) {
        jdbcTemplate.update("UPDATE EspacioPublico SET descripcion=?, localizacionGeografica=?, ocupacion=?, longitud=?, " +
                        "amplitud=?, orientacion=?, comentario=?, tipoTerreno=?, tipoAcceso=?, nombre_municipio=? where nombre=?",
                espacioPublico.getDescripcion(), espacioPublico.getLocalizacionGeografica(), espacioPublico.getOcupacion(), espacioPublico.getLongitud(),
                espacioPublico.getAmplitud(), espacioPublico.getOrientacion(), espacioPublico.getComentario(), espacioPublico.getTipoTerreno(),
                espacioPublico.getTipoAcceso(), espacioPublico.getNombreMunicipio(), espacioPublico.getNombre());
    }


    public EspacioPublico getEspacioPublico(String nombre) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from EspacioPublico WHERE nombre=?",
                    new EspacioPublicoRowMapper(), nombre);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<EspacioPublico> getEspaciosPublicos() {
        try {
            return jdbcTemplate.query("SELECT * from EspacioPublico ORDER BY tipoAcceso DESC, nombre",
                    new EspacioPublicoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

