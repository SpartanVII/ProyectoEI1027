package mvc.dao;


import mvc.model.ServPermanentInst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServPermanentInstDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addServPermanentInst(ServPermanentInst servPermanentInst) {
        jdbcTemplate.update("INSERT INTO ServPermanentInst VALUES(?,?)",
                servPermanentInst.getNombre(),servPermanentInst.getTipoServicio());
    }


    public void deleteServPermanentInst(String nombre) {
        jdbcTemplate.update("DELETE from ServPermanentInst where nombre=?",
                nombre);
    }
    public void deleteServPermanentInst(ServPermanentInst servPermanentInst) {
        jdbcTemplate.update("DELETE from ServPermanentInst where nombre=?",
                servPermanentInst.getNombre());
    }

    public void updateServPermanentInst(ServPermanentInst servPermanentInst) {
        jdbcTemplate.update("UPDATE ServPermanentInst SET tipoServicio=? where nombre=?",
                servPermanentInst.getTipoServicio(),servPermanentInst.getNombre());
    }


    public ServPermanentInst getServPermanentInst(String nombre) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from ServPermanentInst WHERE nombre=?",
                    new ServPermanentInstRowMapper(), nombre);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<ServPermanentInst> getGestores() {
        try {
            return jdbcTemplate.query("SELECT * from ServPermanentInst",
                    new ServPermanentInstRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

}

