package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Busline;
import ch.zhaw.jv19.loganalyzer.model.Site;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLBuslineDAO implements BusLineDAO {

    @Override
    public Busline getBuslineByName(Busline busline) throws SQLException {
        Site site = null;
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM site WHERE name = '?';");
        ResultSet rs = pstmt.executeQuery();
        return extractBuslineFromResultSet(rs);
    }

    @Override
    public ObservableList<Busline> getAllBuslinesList() throws SQLException {
        ObservableList buslineList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM busline;");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            buslineList.add(extractBuslineFromResultSet(rs));
        }
        return buslineList;
    }

    @Override
    public TableView<ObservableList> getAllBuslinesTable() {
        return null;
    }

    @Override
    public int saveBusline(Busline busline) throws SQLException {
        String[] values = {
                StringUtil.addQuotes.apply(busline.getCreatedUser()),
        };
        String statementTemplate =
                "INSERT INTO busline (createdUser, name, bustype) " +
                        "VALUES (" +
                        values[0] + "," +
                        values[1] + "," +
                        values[2] +
                        ") ON DUPLICATE KEY UPDATE " +
                        " createdUser" + MySQLConst.EQUALS + values[0] +
                        " name " + MySQLConst.EQUALS + values[1] +
                        " bustype " + MySQLConst.EQUALS + values[2] +
                        MySQLConst.ENDQUERY;
        return DBUtil.executeUpdate(statementTemplate);
    }

    private Busline extractBuslineFromResultSet(ResultSet rs) throws SQLException {
        Busline busline = new Busline();
        busline.setId(rs.getInt("id"));
        busline.setCreated(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("created"), MySQLConst.DATETIMEPATTERN));
        busline.setCreatedUser(rs.getString("createduser"));
        busline.setName(rs.getString("name"));
        busline.setBustype(rs.getString("bustype"));
        return busline;
    }

}
