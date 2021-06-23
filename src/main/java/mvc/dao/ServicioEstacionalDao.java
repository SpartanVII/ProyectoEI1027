package mvc.dao;


import mvc.model.PeriodosServiciosEstacionalesEnEspacio;
import mvc.model.ServicioEstacional;
import mvc.model.ServicioPerma;
import mvc.services.ServicioEstacionalCompleto;
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


    public List<ServicioEstacional> getServiciosEstacionales() {
        try {
            return jdbcTemplate.query("SELECT * from ServicioEstacional",
                    new ServicioEstacionalRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<ServicioEstacionalCompleto> getServiciosEstacionalesEspacio(String nombreEspacio) {
        try {
            List<ServicioEstacional> servicioEstacionals= jdbcTemplate.query("SELECT * from ServicioEstacional WHERE nombre IN (SELECT nombre_servicioestacional " +
                            "FROM periodosserviciosestacionalesenespacio WHERE nombre_espaciopublico=? )", new ServicioEstacionalRowMapper(), nombreEspacio);
            List<ServicioEstacionalCompleto> listaDefinitiva= new ArrayList<>();

            //Creamos la clase auxiliar para almacenar las fechas
            for (ServicioEstacional servicioEstacional: servicioEstacionals){
                 PeriodosServiciosEstacionalesEnEspacio ser=
                         jdbcTemplate.queryForObject("SELECT * from PeriodosServiciosEstacionalesEnEspacio WHERE nombre_espacioPublico=? AND nombre_servicioEstacional=?",
                         new PeriodosServiciosEstacionalesEnEspacioRowMapper(), nombreEspacio, servicioEstacional.getNombre());
                assert ser != null;
                listaDefinitiva.add(new ServicioEstacionalCompleto(servicioEstacional,ser));
            }

            return listaDefinitiva;

        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<ServicioEstacional> getServiciosEstacionalesDisponiblesParaEspacio(String nombreEspacio) {
        try {
            return jdbcTemplate.query("SELECT * from ServicioEstacional WHERE nombre NOT IN (SELECT nombre_servicioestacional " +
                            "FROM periodosserviciosestacionalesenespacio WHERE nombre_espaciopublico=? )",
                    new ServicioEstacionalRowMapper(), nombreEspacio);

        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public void addServicioEstacionalEnEspacio(String nombreServicioEstacional, String nombreEspacio) {
        jdbcTemplate.update("INSERT INTO SeFijaEnEspacio VALUES(?,?)",
                nombreServicioEstacional, nombreEspacio);
    }

    public void deleteServicioEstacionalDeEspacio(String nombreServicioEstacional, String nombreEspacioPublico) {
        jdbcTemplate.update("DELETE from periodosserviciosestacionalesenespacio where nombre_servicioestacional=? AND nombre_espaciopublico=?",
                nombreServicioEstacional, nombreEspacioPublico);
    }

}

