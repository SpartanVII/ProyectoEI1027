package mvc.dao;


import mvc.model.EspacioPublico;
import mvc.model.enumerations.TipoAcceso;
import mvc.model.enumerations.TipoTerreno;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class EspacioPublicoRowMapper implements
        RowMapper<EspacioPublico> {

    public EspacioPublico mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        EspacioPublico espacioPublico = new EspacioPublico();
        espacioPublico.setNombre(rs.getString("nombre"));
        espacioPublico.setDescripcion(rs.getString("descripcion"));
        espacioPublico.setLocalizacionGeografica(rs.getString("localizacionGeografica"));
        espacioPublico.setOcupacion(rs.getInt("ocupacion"));
        espacioPublico.setLongitud(rs.getInt("longitud"));
        espacioPublico.setAmplitud(rs.getInt("amplitud"));
        espacioPublico.setOrientacion(rs.getString("orientacion"));
        espacioPublico.setComentario(rs.getString("comentario"));
        espacioPublico.setTipoTerreno(rs.getString("tipoTerreno"));
        espacioPublico.setTipoAcceso(rs.getString("tipoAcceso"));
        espacioPublico.setNombreMunicipio(rs.getString("nombre_municipio"));
        return espacioPublico;
    }
}
