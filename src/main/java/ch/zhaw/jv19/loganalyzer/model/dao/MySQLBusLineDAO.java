package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.BusLine;
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
 * Provides functionality for extraction, update and save of busLine data from and to data base.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class MySQLBusLineDAO implements BusLineDAO {

    /**
     * Returns a list of all busLines in the data base.
     *
     * @return list of busLines
     * @throws Exception  if query fails
     */
    @Override
    public ObservableList<BusLine> getAllBusLinesList() throws Exception {
        ObservableList<BusLine> busLineList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        if (con != null) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM busLine;");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                busLineList.add(extractBusLineFromResultSet(rs));
            }
        }
        return busLineList;
    }

    /**
     * Saves a single busLine instance to the data base.
     *
     * @param busLine busLine instance
     * @throws Exception if saving fails
     */
    @Override
    public void saveBusLine(BusLine busLine) throws Exception {
        String[] values = {
                StringUtil.wrapQuotes.apply(busLine.getCreatedUser()),
                StringUtil.wrapQuotes.apply(busLine.getName()),
                StringUtil.wrapQuotes.apply(busLine.getBusType()),
        };
        String statementTemplate =
                "INSERT INTO busLine (createduser, name, bustype) " +
                        "VALUES (" +
                        values[0] + "," +
                        values[1] + "," +
                        values[2] +
                        ") ON DUPLICATE KEY UPDATE " +
                        " createduser" + MySQLConst.EQUALS + values[0] +
                        ", name " + MySQLConst.EQUALS + values[1] +
                        ", bustype " + MySQLConst.EQUALS + values[2] +
                        MySQLConst.ENDQUERY;
        DBUtil.executeUpdate(statementTemplate);
    }

    /**
     * Extracts a single busLine from a result set.
     *
     * @param rs result set
     * @return bus line
     * @throws SQLException if extraction fails
     */
    private BusLine extractBusLineFromResultSet(ResultSet rs) throws SQLException {
        BusLine busLine = new BusLine();
        busLine.setId(rs.getInt("id"));
        busLine.setCreated(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("created"), MySQLConst.DATETIMEPATTERN));
        busLine.setCreatedUser(rs.getString("createduser"));
        busLine.setName(rs.getString("name"));
        busLine.setBusType(rs.getString("bustype"));
        return busLine;
    }

    /**
     * Updates busLine date in the data base provided in a list.
     *
     * @param busLineList : list for busLines
     * @throws SQLException if updating fails
     */
    public void updateBusLineData(ObservableList<BusLine> busLineList) throws SQLException {
        Connection connection = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE BUSLINE SET createduser = ?, name = ?, bustype = ? WHERE id = ?");
            for (BusLine busLine : busLineList) {
                ps.setString(1, busLine.getCreatedUser());
                ps.setString(2, busLine.getName());
                ps.setString(3, busLine.getBusType());
                ps.setInt(4, busLine.getId());
                ps.addBatch();
            }
    }
}
