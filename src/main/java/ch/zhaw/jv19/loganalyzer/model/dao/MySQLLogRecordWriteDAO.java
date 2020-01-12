package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;

import java.sql.*;
import java.util.List;

/**
 * Receives a logRecordList and does the insert to the data base.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class MySQLLogRecordWriteDAO implements LogRecordWriteDAO {

    public MySQLLogRecordWriteDAO() {
    }

    /**
     * Gets a logRecordList and generates the insert statements for each logRecord. Statements are
     * processed as a batch.
     *
     * @param logRecordList list of logRecords
     * @return returns an int[], representing the altered rows in the data base.
     * @throws Exception database error, if insert fails
     */
    @Override
    public int[] insertLogRecords(List<LogRecord> logRecordList) throws Exception {
        Connection connection = DBUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT IGNORE INTO logrecord (createduser,unique_identifier,timestamp,site,busLine,address,milliseconds,type,source,message) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                for (LogRecord logRecord: logRecordList){
                // INSERT INTO logrecord (createduser,unique_identifier,timestamp,site,busLine,address,milliseconds,type,source, message) values ('admin', 'hueresiech', '2019-11-13 16:31:08', 1, 1, 1, 798420, 'Warning', 'Controller', 'Errors: +Mechanical')
                ps.setString(1, logRecord.getUser().getName());
                ps.setString(2, logRecord.getUniqueIdentifier());
                ps.setString(3, DateUtil.convertDateTimeToString(logRecord.getTimestamp(), MySQLConst.DATETIMEPATTERN));
                ps.setInt(4, (logRecord.getSite().getId()));
                ps.setInt(5, logRecord.getBusLine().getId());
                ps.setInt(6, logRecord.getAddress());
                ps.setInt(7, logRecord.getMilliseconds());
                ps.setString(8, logRecord.getEventType());
                ps.setString(9, logRecord.getSource());
                ps.setString(10, logRecord.getMessage());
                ps.addBatch();
            }
            return ps.executeBatch();
    }
}