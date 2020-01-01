package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLLogRecordWriteDAO implements LogRecordWriteDAO {

    public MySQLLogRecordWriteDAO() {}

    @Override
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
