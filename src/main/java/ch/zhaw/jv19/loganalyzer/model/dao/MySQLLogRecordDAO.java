package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLLogRecordDAO implements LogRecordDAO {

    public MySQLLogRecordDAO(List logRecordList) {
        insertLogRecords(logRecordList);
    }

    @Override
    public ObservableList<LogRecord> getAllLogRecordsList() throws SQLException {
        ObservableList logRecordList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM logrecord;");
        ResultSet rs =  pstmt.executeQuery();
        while(rs.next()) {
            logRecordList.add(extractLogRecordFromResultSet(rs));
        }
        return logRecordList;
    }

    @Override
    public TableView<ObservableList> getAllLogRecordsTable() {
        TableView allLogRecordsTable = null;
        try {
            allLogRecordsTable = new TableView(getAllLogRecordsList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allLogRecordsTable;
    }

    @Override
    public void saveLogRecord(LogRecord logRecord) {

    }

    @Override
    public void updateLogRecord(LogRecord logRecord) {

    }

    private LogRecord extractLogRecordFromResultSet(ResultSet rs) throws SQLException {
        LogRecord logRecord = new LogRecord();
        logRecord.setDateTime(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("timestamp"), MySQLConst.DATETIMEPATTERN ));
        logRecord.setMilliSeconds( rs.getString("milliseconds") );
        logRecord.setEventType(rs.getString("type"));
        logRecord.setSource(rs.getString("source"));
        logRecord.setMessage( rs.getString("message"));
        return logRecord;
    }

    public int[] insertLogRecords(List<LogRecord> logRecordList){
        Connection connection = DBUtil.getConnection();
        try {
            for (LogRecord logRecord: logRecordList){
                PreparedStatement ps = connection.prepareStatement("INSERT INTO user logrecord (NULL, ?, ?, ?, ?, ?, ?, ?, ?)");

                ps.setString(1, DateUtil.convertDateTimeToUtcString(logRecord.getDateTime(), MySQLConst.DATETIMEPATTERN));
                ps.setString(2, logRecord.getMilliSeconds());
                ps.setString(3, logRecord.getEventType());
                ps.setString(4, logRecord.getSource());
                ps.setString(5, logRecord.getMessage());
                ps.setString(6, logRecord.getUser());
                ps.setString(7, logRecord.getSite());
                ps.setString(8, logRecord.getBusline());
                ps.addBatch();

                int i = ps.executeUpdate();
                if (i == 1) {
                    return ps.executeBatch();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
