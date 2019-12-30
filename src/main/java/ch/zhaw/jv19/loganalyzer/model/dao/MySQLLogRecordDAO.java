package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.ImportFileConst;
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
    //TODO refactor: Insert should not happen in constructor
    public MySQLLogRecordDAO(List<LogRecord> logRecordList) throws Exception {
        insertLogRecords(logRecordList);
    }

    @Override
    public ObservableList<LogRecord> getAllLogRecordsList() throws Exception {
        ObservableList<LogRecord> logRecordList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM logrecord;");
        ResultSet rs =  pstmt.executeQuery();
        while(rs.next()) {
            logRecordList.add(extractLogRecordFromResultSet(rs));
        }
        return logRecordList;
    }

    @Override
    public TableView<LogRecord> getAllLogRecordsTable() {
        TableView<LogRecord> allLogRecordsTable = null;
        try {
            allLogRecordsTable = new TableView<>(getAllLogRecordsList());
        } catch (Exception e) {
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
        logRecord.setTimestamp(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("timestamp"), MySQLConst.DATETIMEPATTERN ));
        logRecord.setMilliseconds( rs.getInt("milliseconds") );
        logRecord.setEventType(rs.getString("type"));
        logRecord.setSource(rs.getString("source"));
        logRecord.setMessage( rs.getString("message"));
        return logRecord;
    }

    public int[] insertLogRecords(List<LogRecord> logRecordList) throws Exception {
        Connection connection = DBUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO logrecord (createduser,unique_identifier,timestamp,site,busline,address,milliseconds,type,source,message) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (LogRecord logRecord: logRecordList){
                // INSERT INTO logrecord (createduser,unique_identifier,timestamp,site,busline,address,milliseconds,type,source, message) values ('admin', 'hueresiech', '2019-11-13 16:31:08', 1, 1, 1, 798420, 'Warning', 'Controller', 'Errors: +Mechanical')
                ps.setString(1, logRecord.getUser().getName());
                ps.setString(2, logRecord.getUniqueIdentifier());
                ps.setString(3, DateUtil.convertDateTimeToString(logRecord.getTimestamp(), MySQLConst.DATETIMEPATTERN));
                ps.setInt(4, (logRecord.getSite().getId()));
                ps.setInt(5, logRecord.getBusline().getId());
                ps.setInt(6, logRecord.getAddress());
                ps.setInt(7, logRecord.getMilliseconds());
                ps.setString(8, logRecord.getEventType());
                ps.setString(9, logRecord.getSource());
                ps.setString(10, logRecord.getMessage());

                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
