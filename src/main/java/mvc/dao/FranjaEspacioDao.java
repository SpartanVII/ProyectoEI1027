package mvc.dao;


import mvc.controller.CorreoController;
import mvc.model.*;
import mvc.services.FranjaEspacioSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FranjaEspacioDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addFranjaEspacio(FranjaEspacio franjaEspacio) {
        jdbcTemplate.update("INSERT INTO FranjaEspacio VALUES(?,?,?)",
                franjaEspacio.getHoraEntrada(), franjaEspacio.getHoraSalida(), franjaEspacio.getNombreEspacio());
    }


    public void deleteFranjaEspacio(FranjaEspacio franjaEspacio) {
        List<Reserva> reservas = jdbcTemplate.query("SELECT * from Reserva where horaEntrada=? and horaSalida=? and nombreEspacio=?",
                new ReservaRowMapper(), franjaEspacio.getHoraEntrada(), franjaEspacio.getHoraSalida(), franjaEspacio.getNombreEspacio());

        for(Reserva reserva: reservas){
            if (reserva.getEstado().equals("PENDIENTE_USO")){
                //Este mensaje no haria falta porque dias antes de la eliminación retirariamos la opción para reservar en esta franja
                String mensaje = "La reserva que tenia de "+reserva.getHoraEntrada()+" a "+reserva.getHoraSalida()
                        +" en la fecha "+reserva.getFecha()+" ha sido cancelada debido a la reciente eliminacion " +
                        "de la franja horaria. Disculpe las molestias";

                Ciudadano ciudadano=jdbcTemplate.queryForObject("SELECT * from Ciudadano WHERE dni=?", new CiudadanoRowMapper(), reserva.getDniCiudadano());
                assert ciudadano != null;
                CorreoController.enviaCorreo(new Correo(ciudadano.getEmail(),"Cancelación de su reserva con fecha "+reserva.getFecha()+
                        " de "+ reserva.getHoraEntrada()+" a "+reserva.getHoraSalida(), mensaje));
            }
            jdbcTemplate.update("DELETE from Ocupa where identificador_reserva=?", reserva.getIdentificador());
            jdbcTemplate.update("DELETE from Reserva where identificador=?", reserva.getIdentificador());
        }

        jdbcTemplate.update("DELETE from FranjaEspacio WHERE horaEntrada=? and horaSalida=? and nombre_espacioPublico=?",
                franjaEspacio.getHoraEntrada(), franjaEspacio.getHoraSalida(), franjaEspacio.getNombreEspacio());
    }




    public FranjaEspacio getFranjaEspacio(FranjaEspacio franjaEspacio) {
        try {
            return jdbcTemplate.queryForObject("SELECT * from FranjaEspacio where nombre_espacioPublico=? " +
                            "and horaEntrada=? and horaSalida=?",
                    new FranjaEspacioRowMapper(), franjaEspacio.getNombreEspacio(), franjaEspacio.getHoraEntrada(), franjaEspacio.getHoraSalida());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<FranjaEspacioSvc> getFranjasEspacio(String nombreEspacio) {
        try {
            List<FranjaEspacio> franjaEspacios = jdbcTemplate.query("SELECT * from FranjaEspacio WHERE nombre_espacioPublico=?",
                    new FranjaEspacioRowMapper(), nombreEspacio);

            List<FranjaEspacioSvc> franjaEspacioSvcs = new ArrayList<>();
            for(FranjaEspacio franjaEspacio:franjaEspacios){
                franjaEspacioSvcs.add(new FranjaEspacioSvc(franjaEspacio));

            }
            return franjaEspacioSvcs;
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<FranjaEspacio> getFranjasEspacioNormales(String nombreEspacio) {
        try {
            return jdbcTemplate.query("SELECT * from FranjaEspacio WHERE nombre_espacioPublico=?",
                    new FranjaEspacioRowMapper(), nombreEspacio);

        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }


}

