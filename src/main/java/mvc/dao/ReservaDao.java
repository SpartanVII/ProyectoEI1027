package mvc.dao;


import mvc.model.Reserva;
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
        jdbcTemplate.update("INSERT INTO Reserva VALUES(?,?,?,?,?,?,?,?)",
                reserva.getIdentificador(), reserva.getNumPersonas(), reserva.getFecha(),"", reserva.getDniCiudadano(),
                reserva.getHoraEntrada(), reserva.getHoraSalida(), reserva.getIdentificadorZona());
    }


    public void deleteReserva(String identificador) {
        jdbcTemplate.update("DELETE from Reserva where identificador=?",
                identificador);
    }
    public void deleteReserva(Reserva reserva) {
        jdbcTemplate.update("DELETE from Reserva where identificador=?",
                reserva.getIdentificador());
    }

    public void updateReserva(Reserva reserva) {
        jdbcTemplate.update("UPDATE Reserva SET numPersonas=?, fecha=?, estado=?, dni_ciudadano=?, horaEntrada=?, horaSalida=?, identificador_zona=? where identificador=?",
                reserva.getNumPersonas(), reserva.getFecha(), reserva.getEstado(), reserva.getDniCiudadano(),
                reserva.getHoraEntrada(), reserva.getHoraSalida(), reserva.getIdentificador(), reserva.getIdentificadorZona());
    }


    public Reserva getReserva(String codigo) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Reserva WHERE identificador=?",
                    new ReservaRowMapper(), codigo);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Reserva> getReservas() {
        try {
            return jdbcTemplate.query("SELECT * from Reserva",
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
            return jdbcTemplate.query("SELECT * FROM Reserva WHERE espacioPublico = (SELECT nombre_espaciopublico from Controla WHERE dni_controlador=? AND fechafin=NULL)",
                    new ReservaRowMapper(), dni);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

