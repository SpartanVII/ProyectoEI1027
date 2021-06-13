package mvc.dao;


import mvc.model.Notificacion;
import mvc.model.Reserva;
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


    public void addReserva(Reserva reserva) {
        jdbcTemplate.update("INSERT INTO Reserva  VALUES(?,?,?,?,?,?,?,?,?)",
                reserva.getIdentificador(), reserva.getNumPersonas(), reserva.getFecha(), reserva.getEstado(), reserva.getDniCiudadano(),
                reserva.getHoraEntrada(), reserva.getHoraSalida(), reserva.getNombreEspacio(), reserva.getIdentificadorZona());
    }


    public void deleteReserva(Integer identificador) {
        jdbcTemplate.update("DELETE from Reserva where identificador=?",
                identificador);
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

    public boolean resersvaDisponible(Reserva otraReserva){
        try {
            List<Reserva> reservas = jdbcTemplate.query("SELECT * from Reserva WHERE identificador_zona=? AND fecha=? AND horaEntrada=? AND horaSalida=? AND estado='PENDIENTE_USO'",
                    new ReservaRowMapper(),otraReserva.getIdentificadorZona(), otraReserva.getFecha(), otraReserva.getHoraEntrada(), otraReserva.getHoraSalida());

            return reservas.isEmpty();
        } catch (EmptyResultDataAccessException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}

