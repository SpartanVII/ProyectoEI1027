package mvc.dao;

import mvc.model.Ejemplo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EjemploDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix la ejemplo a la base de dades */
    public void addClassificacio(Ejemplo classificacio) {
        jdbcTemplate.update("INSERT INTO Ejemplo VALUES(?, ?, ?, ?)",
                classificacio.getNomNadador(), classificacio.getNomProva(), classificacio.getPosicio(), classificacio.getTemps());
    }

    /* Esborra la ejemplo de la base de dades */
    public void deleteClassificacio(String nomNadador, String nomProva) {
        jdbcTemplate.update("DELETE from Ejemplo where nom_nadador=? AND nom_prova=?",
                nomNadador, nomProva);
    }
    public void deleteClassificacio(Ejemplo classificacio) {
        jdbcTemplate.update("DELETE from Ejemplo where nom_nadador=? AND nom_prova=?",
                classificacio.getNomNadador(), classificacio.getNomProva());
    }

    /* Actualitza els atributs de la ejemplo
       (excepte el nomNadador y nomProva, que és la clau primària) */
    public void updateClassificacio(Ejemplo classificacio) {
        jdbcTemplate.update("UPDATE Ejemplo SET posicio=?, temps=? where nom_nadador=? and nom_prova=?",
                classificacio.getPosicio(), classificacio.getTemps(), classificacio.getNomNadador(), classificacio.getNomProva());
    }

    /* Obté la ejemplo amb el nom donat. Torna null si no existeix. */
    public Ejemplo getClassificacio(String nomNadador, String nomProva) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from Ejemplo WHERE nom_nadador=? and nom_prova=?",
                    new EjemploRowMapper(), nomNadador, nomProva);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté totes les classificacions. Torna una llista buida si no n'hi ha cap. */
    public List<Ejemplo> getClassificacions() {
        try {
            return jdbcTemplate.query("SELECT * from Ejemplo",
                    new EjemploRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<Ejemplo>();
        }
    }

    public List<Ejemplo> getClassificacioProva(String nomProva) {
        try {
            return this.jdbcTemplate.query(
                    "SELECT * FROM ejemplo WHERE nom_prova=?",
                    new Object[] {nomProva}, new EjemploRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Ejemplo>();
        }
    }

    //
    //
    //
    public List<Ejemplo> getClassificacioPais(String nomPais) {
        try {
            return this.jdbcTemplate.query(
                    "SELECT * FROM ejemplo WHERE nom_nadador=(SELECT Nom FROM Nadador WHERE Pais=?)",
                    new Object[] {nomPais}, new EjemploRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Ejemplo>();
        }
    }

}

