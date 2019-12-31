package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;

import java.util.List;

public interface LogRecordWriteDAO {
    int[] insertLogRecords(List<LogRecord> logRecordList) throws Exception;
}