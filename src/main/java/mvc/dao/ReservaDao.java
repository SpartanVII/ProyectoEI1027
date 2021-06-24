package mvc.dao;


import mvc.model.Notificacion;
import mvc.model.Reserva;
import mvc.model.Zona;
import mvc.model.enumerations.EstadoReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservaDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addReserva(Reserva reserva, String zonas) {
        jdbcTemplate.update("INSERT INTO Reserva  VALUES(?,?,?,?,?,?,?,?,?)",
                reserva.getIdentificador(), reserva.getNumPersonas(), reserva.getFecha(), reserva.getEstado(), reserva.getDniCiudadano(),
                reserva.getHoraEntrada(), reserva.getHoraSalida(), reserva.getNombreEspacio(), reserva.getIdentificadorZona());

        String[] zonitas = zonas.split(",");
        if(reserva.getNumPersonas()>=5)
            jdbcTemplate.update("INSERT INTO Ocupa VALUES(?,?)",reserva.getIdentificador(),zonitas[0]);
        if(reserva.getNumPersonas()>=10)
            jdbcTemplate.update("INSERT INTO Ocupa VALUES(?,?)",reserva.getIdentificador(),zonitas[1]);
        if(reserva.getNumPersonas()>=15)
            jdbcTemplate.update("INSERT INTO Ocupa VALUES(?,?)",reserva.getIdentificador(),zonitas[2]);;
        if(reserva.getNumPersonas()>=20)
            jdbcTemplate.update("INSERT INTO Ocupa VALUES(?,?)",reserva.getIdentificador(),zonitas[3]);;
    }


    public void deleteReserva(Integer identificador) {
        jdbcTemplate.update("DELETE from Reserva where identificador=?", identificador);

        jdbcTemplate.update("DELETE from Ocupa where identificador_reserva=?", identificador);
    }
    public void deleteReserva(Reserva reserva) {
        jdbcTemplate.update("DELETE from Reserva where identificador=?",
                reserva.getIdentificador());
    }

    public void updateReserva(Reserva reserva) {
        jdbcTemplate.update("UPDATE Reserva SET numPersonas=?, fecha=?, dni_ciudadano=?, horaEntrada=?, horaSalida=?, identificador_zona=?, nombreEspacio=? where identificador=?",
                reserva.getNumPersonas(), reserva.getFecha(), reserva.getDniCiudadano(), reserva.getHoraEntrada(), reserva.getHoraSalida(),
                reserva.getIdentificadorZona(), reserva.getNombreEspacio(), reserva.getIdentificador());
    }

    public void cancelaReserva(Reserva reserva, String estado) {
        jdbcTemplate.update("UPDATE Reserva SET  estado=? where identificador=?",
                     estado, reserva.getIdentificador());

    }


    public Reserva getReserva(Integer identificador) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Reserva WHERE identificador=?",
                    new ReservaRowMapper(), identificador);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Reserva> getReservas() {
        try {
            return jdbcTemplate.query("SELECT * from Reserva ORDER BY estado DESC, dni_ciudadano, fecha",
                    new ReservaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Reserva> getReservasParticular(String dni){
        try {
            return jdbcTemplate.query("SELECT * from Reserva WHERE dni_ciudadano=? ORDER BY estado DESC, fecha",
                    new ReservaRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Reserva> getReservasEnMiMunicipio(String dni){
        try {
            return jdbcTemplate.query("SELECT * FROM Reserva WHERE nombreEspacio " +
                            "IN (SELECT nombre from EspacioPublico WHERE nombre_municipio IN (" +
                            "SELECT nombre_municipio from GestorMunicipal WHERE dni=?))" +
                            "ORDER BY dni_ciudadano, fecha",
                    new ReservaRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Reserva> getReservasEnMiEspacio(String dni){
        try {
            return jdbcTemplate.query("SELECT * FROM Reserva WHERE nombreEspacio " +
                            "IN (SELECT nombre_espacioPublico from Controla WHERE dni_controlador=? AND fechafin IS NULL)" +
                            "ORDER BY dni_ciudadano, fecha",
                    new ReservaRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Integer getSiguienteIdentificadorReserva(){
        try {
            Reserva reserva = jdbcTemplate.queryForObject("SELECT * from Reserva ORDER BY identificador DESC LIMIT 1",
                    new ReservaRowMapper());
            assert reserva != null;
            return reserva.getIdentificador()+1;
        } catch (EmptyResultDataAccessException e) {
            return 1;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Zona> getZonasDisponibles(Reserva otraReserva){
        try {
            return jdbcTemplate.query("SELECT * from Zona WHERE nombre_espacioPublico=? AND identificador NOT IN (SELECT identificador_zona FROM ocupa WHERE identificador_reserva IN " +
                            "(SELECT identificador From Reserva WHERE nombreEspacio=? AND fecha=? AND horaEntrada=? AND horaSalida=? AND estado='PENDIENTE_USO'))",
                    new ZonaRowMapper(),otraReserva.getNombreEspacio(), otraReserva.getNombreEspacio(), otraReserva.getFecha(), otraReserva.getHoraEntrada(), otraReserva.getHoraSalida());

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }



}

