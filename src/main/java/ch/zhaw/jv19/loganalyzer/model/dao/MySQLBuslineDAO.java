package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Busline;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLBuslineDAO implements BusLineDAO {

    @Override
    public Busline getBuslineByName(String name) throws Exception {
        Busline busline = null;
        Connection con = DBUtil.getConnection();
        if (con != null) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM busline WHERE name = '?';");
            ResultSet rs = pstmt.executeQuery();
            busline = extractBuslineFromResultSet(rs);
        }
        return busline;
    }

    @Override
    public ObservableList<Busline> getAllBuslinesList() throws Exception {
        ObservableList<Busline> buslineList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        if (con != null) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM busline;");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                buslineList.add(extractBuslineFromResultSet(rs));
            }
        }
        return buslineList;
    }

    @Override
    public int saveBusline(Busline busline) throws Exception {
        String[] values = {
                StringUtil.addQuotes.apply(busline.getCreatedUser()),
                StringUtil.addQuotes.apply(busline.getName()),
                StringUtil.addQuotes.apply(busline.getBustype()),
        };
        String statementTemplate =
                "INSERT INTO busline (createduser, name, bustype) " +
                        "VALUES (" +
                        values[0] + "," +
                        values[1] + "," +
                        values[2] +
                        ") ON DUPLICATE KEY UPDATE " +
                        " createduser" + MySQLConst.EQUALS + values[0] +
                        ", name " + MySQLConst.EQUALS + values[1] +
                        ", bustype " + MySQLConst.EQUALS + values[2] +
                        MySQLConst.ENDQUERY;
        return DBUtil.executeUpdate(statementTemplate);
    }

    public Busline extractBuslineFromResultSet(ResultSet rs) throws SQLException {
        Busline busline = new Busline();
        busline.setId(rs.getInt("id"));
        busline.setCreated(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("created"), MySQLConst.DATETIMEPATTERN));
        busline.setCreatedUser(rs.getString("createduser"));
        busline.setName(rs.getString("name"));
        busline.setBustype(rs.getString("bustype"));
        return busline;
    }

    public int[] updateBuslineData(ObservableList<Busline> buslineList) throws SQLException {
        Connection connection = DBUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE BUSLINE SET createduser = ?, name = ?, bustype = ? WHERE id = ?");
            for (Busline busline : buslineList) {
                ps.setString(1, busline.getCreatedUser());
                ps.setString(2, busline.getName());
                ps.setString(3, busline.getBustype());
                ps.setInt(4, busline.getId());
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
