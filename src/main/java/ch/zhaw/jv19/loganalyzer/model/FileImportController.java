package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.MySQLLogRecordDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileImportController {

    private User user;
    private Site site;
    private Busline busline;
    private List<LogFile> logFileList;
    private List<LogRecord> logRecordList;


    public FileImportController() {}

    public FileImportController(User user, Site site, Busline busline, List<File> fileList) {
        this.user = user;
        this.site = site;
        this.busline = busline;
        createLogFiles(fileList);
        createLogRecordList(logFileList);
        saveToDB(logRecordList);
    }

    private void createLogFiles(List<File> fileList) {
        try {
            logFileList = new ArrayList<>();
            for (File file : fileList) {
                LogFile logFile = new LogFile();
                FileInputStream fstream = null;
                fstream = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                READ_RECORDS: while ((strLine = br.readLine()) != null) {
                    String[] tokens = strLine.split("\t");
                    LogRecord record = new LogRecord(tokens[0], convertMilliSeconds(tokens[1]), tokens[2], tokens[3], tokens[4], user, site, busline);
                    if (record.getMessage().contains("address") == false && logFile.isAddressSet() == false) {
                        break READ_RECORDS;
                    }
                    else {
                        if (record.getMessage().contains("address")) {
                            logFile.setAddress(record.getMessage());
                            record.setUniqueIdentifier(logFile.getAddress());
                            logFile.addLogRecord(record);
                        }
                        else {
                            record.setUniqueIdentifier(logFile.getAddress());
                            logFile.addLogRecord(record);
                        }
                    }
                }
            logFileList.add(logFile);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLogRecordList(List<LogFile> logFileList) {
        logRecordList = new ArrayList<>();
        for (LogFile logFile : logFileList) {
            for (LogRecord logRecord : logFile.getRecordList()) {
                logRecordList.add(logRecord);
            }
        }
    }

    private void saveToDB(List<LogRecord> logRecordList) {
        MySQLLogRecordDAO logRecordDAOWriter = new MySQLLogRecordDAO(logRecordList);
    }

    private int convertMilliSeconds(String milliseconds) {
        String[] millis = milliseconds.split(" ");
        return Integer.parseInt(millis[0]);
    }
}