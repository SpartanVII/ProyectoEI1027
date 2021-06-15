package mvc.dao;

import mvc.model.EspacioPublico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EspacioPublicoDao {

    private JdbcTemplate jdbcTemplate;
    private int tamZona= 5;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addEspacioPublico(EspacioPublico espacioPublico) {
        jdbcTemplate.update("INSERT INTO EspacioPublico VALUES(?,?,?,?,?,?,?,?,?,?,?)",
                espacioPublico.getNombre(), espacioPublico.getDescripcion(), espacioPublico.getLocalizacionGeografica(),
                espacioPublico.getOcupacion(), espacioPublico.getLongitud(), espacioPublico.getAmplitud(), espacioPublico.getOrientacion(),
                espacioPublico.getComentario(), espacioPublico.getTipoTerreno(), espacioPublico.getTipoAcceso(), espacioPublico.getNombreMunicipio());

        if(espacioPublico.getTipoAcceso().equals("RESTRINGIDO")){

            for (int i = 1; i <= espacioPublico.getOcupacion()/tamZona; i++) {
                jdbcTemplate.update("INSERT INTO Zona VALUES(?,?,?,?)",i+"-"+espacioPublico.getNombre(),
                        "Sin descripcion", 5,espacioPublico.getNombre());
            }
            jdbcTemplate.update("INSERT INTO FranjaEspacio VALUES(?,?,?)", LocalTime.parse("10:00"),LocalTime.parse("11:00"), espacioPublico.getNombre());
            jdbcTemplate.update("INSERT INTO FranjaEspacio VALUES(?,?,?)", LocalTime.parse("11:00"),LocalTime.parse("12:00"), espacioPublico.getNombre());
            jdbcTemplate.update("INSERT INTO FranjaEspacio VALUES(?,?,?)", LocalTime.parse("14:00"),LocalTime.parse("16:00"), espacioPublico.getNombre());
        }

    }




    public void deleteEspacioPublico(String nombre) {
        //Habría que hacer algun metodo alternativo para que no se perdiera tanta información
        jdbcTemplate.update("DELETE from SeFijaEnEspacio where nombre_espacioPublico=?",
                nombre);
        jdbcTemplate.update("DELETE from PeriodosServiciosEstacionalesEnEspacio where nombre_espacioPublico=?",
                nombre);
        jdbcTemplate.update("DELETE from PeriodosServiciosEstacionalesEnEspacio where nombre_espacioPublico=?",
                nombre);
        jdbcTemplate.update("DELETE from Controla where nombre_espacioPublico=?",
                nombre);
        jdbcTemplate.update("DELETE from Reserva where nombreEspacio=?",
                nombre);
        jdbcTemplate.update("DELETE from FranjaEspacio where nombre_espacioPublico=?",
                nombre);
        jdbcTemplate.update("DELETE from Zona where nombre_espacioPublico=?",
                nombre);
        jdbcTemplate.update("DELETE from EspacioPublico where nombre=?",
                nombre);
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

    public List<EspacioPublico> getEspaciosPublicosNoCerrados() {
        try {
            return jdbcTemplate.query("SELECT * from EspacioPublico WHERE tipoAcceso!='CERRADO' ORDER BY tipoAcceso DESC, nombre",
                    new EspacioPublicoRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public void creaZonas(String nombreEspacio, int num){
        for (int i = 0; i < num; i++) {
            jdbcTemplate.update("INSERT INTO Zona VALUES(?,?,?,?)",i+"-"+nombreEspacio,
                    "Sin descripcion", 5,nombreEspacio);
        }
    }

}

