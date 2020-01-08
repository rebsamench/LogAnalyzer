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

/**
 * Provides functionality for extraction, update and save of busline data from and to data base.
 *
 * @autor: Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class MySQLBuslineDAO implements BusLineDAO {

    /**
     * Reads a single busline from data base.
     *
     * @param name : busline name
     * @return : Busline instance
     * @throws Exception
     */
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

    /**
     * Returns a list of all buslines in the data base.
     *
     * @return : list of buslines
     * @throws Exception
     */
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

    /**
     * Saves a single busline instance to the data base.
     *
     * @param busline : busline instance
     * @return : int represents the row count.
     * @throws Exception
     */
    @Override
    public int saveBusline(Busline busline) throws Exception {
        String[] values = {
                StringUtil.wrapQuotes.apply(busline.getCreatedUser()),
                StringUtil.wrapQuotes.apply(busline.getName()),
                StringUtil.wrapQuotes.apply(busline.getBustype()),
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

    /**
     * Extracts a single busline from a result set.
     *
     * @param rs : result set
     * @return
     * @throws SQLException
     */
    public Busline extractBuslineFromResultSet(ResultSet rs) throws SQLException {
        Busline busline = new Busline();
        busline.setId(rs.getInt("id"));
        busline.setCreated(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("created"), MySQLConst.DATETIMEPATTERN));
        busline.setCreatedUser(rs.getString("createduser"));
        busline.setName(rs.getString("name"));
        busline.setBustype(rs.getString("bustype"));
        return busline;
    }

    /**
     * Updates busline date in the data base provided in a list.
     *
     * @param buslineList : list for buslines
     * @return : int[] representing the updated rows
     * @throws SQLException
     */
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
