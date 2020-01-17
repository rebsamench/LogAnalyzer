package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.LogRecord;

import java.util.List;

public interface LogRecordWriteDAO {
    void insertLogRecords(List<LogRecord> logRecordList) throws Exception;
}