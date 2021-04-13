package mvc.dao;


import mvc.model.ServPermanentInst;
import mvc.model.enumerations.TipoServicio;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ServPermanentInstRowMapper implements
        RowMapper<ServPermanentInst> {

    public ServPermanentInst mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        ServPermanentInst servPermanentInst = new ServPermanentInst();
        servPermanentInst.setNombre(rs.getString("nombre"));
        servPermanentInst.setTipoServicio(rs.getObject("tipoServicio", TipoServicio.class));
        return servPermanentInst;
    }
}
