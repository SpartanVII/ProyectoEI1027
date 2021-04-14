package mvc.dao;


import mvc.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
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
        jdbcTemplate.update("INSERT INTO Reserva VALUES(?,?,?,?)",
                reserva.getCodigo(), reserva.getNumPersonas(), reserva.getFecha(), reserva.getEstado());
    }


    public void deleteReserva(String codigo) {
        jdbcTemplate.update("DELETE from Reserva where codigo=?",
                codigo);
    }
    public void deleteReserva(Reserva reserva) {
        jdbcTemplate.update("DELETE from Reserva where codigo=?",
                reserva.getCodigo());
    }

    public void updateReserva(Reserva reserva) {
        jdbcTemplate.update("UPDATE Reserva SET numPersonas=?, fecha=?, estado=? where codigo=?",
                reserva.getNumPersonas(), reserva.getFecha(), reserva.getEstado(), reserva.getCodigo());
    }


    public Reserva getReserva(String codigo) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Reserva WHERE codigo=?",
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

}

