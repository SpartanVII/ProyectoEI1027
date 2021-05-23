package mvc.dao;


import mvc.model.Notificacion;
import mvc.model.Reserva;
import mvc.model.enumerations.EstadoReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
        jdbcTemplate.update("INSERT INTO Reserva (identificador, numPersonas, fecha, dni_ciudadano, horaEntrada, " +
                        "horaSalida, identificador_zona) VALUES(?,?,?,?,?,?,?)",
                reserva.getIdentificador(), reserva.getNumPersonas(), reserva.getFecha(), reserva.getDniCiudadano(),
                reserva.getHoraEntrada(), reserva.getHoraSalida(), reserva.getIdentificadorZona());
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
        jdbcTemplate.update("UPDATE Reserva SET numPersonas=?, fecha=?, dni_ciudadano=?, horaEntrada=?, horaSalida=?, identificador_zona=? where identificador=?",
                reserva.getNumPersonas(), reserva.getFecha(), reserva.getDniCiudadano(),
                reserva.getHoraEntrada(), reserva.getHoraSalida(), reserva.getIdentificadorZona(), reserva.getIdentificador());
    }

    public void cancelaReserva(Reserva reserva, String estado) {
        jdbcTemplate.update("UPDATE Reserva SET numPersonas=?, fecha=?, estado=?, dni_ciudadano=?, horaEntrada=?, horaSalida=?, identificador_zona=? where identificador=?",
                    reserva.getNumPersonas(), reserva.getFecha(), estado, reserva.getDniCiudadano(), reserva.getHoraEntrada(),
                    reserva.getHoraSalida(), reserva.getIdentificadorZona(), reserva.getIdentificador());

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
            return jdbcTemplate.query("SELECT * from Reserva ORDER BY dni_ciudadano",
                    new ReservaRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Reserva> getReservasParticular(String dni){
        try {
            return jdbcTemplate.query("SELECT * from Reserva WHERE dni_ciudadano=?",
                    new ReservaRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Reserva> getReservasEnMiEspacio(String dni){
        try {
            return jdbcTemplate.query("SELECT * FROM Reserva WHERE identificador_zona " +
                            "IN (SELECT identificador FROM Zona WHERE nombre_espacioPublico " + 
                            "IN (SELECT nombre_espaciopublico from Controla WHERE dni_controlador=? AND fechafin IS NULL))" +
                            "ORDER BY dni_ciudadano",
                    new ReservaRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Integer getSiguienteIdentificadorReserva(){
        try {
            Reserva reserva = jdbcTemplate.queryForObject("SELECT * from Reserva ORDER BY identificador DESC LIMIT 1",
                    new ReservaRowMapper());
            return reserva.getIdentificador()+1;

        } catch (EmptyResultDataAccessException e) {
            return 1;
        } catch (Exception e) {
            return null;
        }
    }

}

