package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.LogRecordWriteDAO;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLLogRecordWriteDAO;
import ch.zhaw.jv19.loganalyzer.util.properties.ImportFileConst;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Receives the collected import data from ImportPanelUIController and processes it to an appropriate form,
 * ready for the data base import.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class FileImportController {

    private final User user;
    private final Site site;
    private final BusLine busLine;
    private List<LogFile> logFileList;
    private List<LogRecord> logRecordList;
    private final AppDataController appDataController;

    public FileImportController(User user, Site site, BusLine busLine, List<File> fileList) throws Exception {
        appDataController = AppDataController.getInstance();
        this.user = user;
        this.site = site;
        this.busLine = busLine;
        createLogFiles(fileList);
        createLogRecordList(logFileList);
        saveToDB(logRecordList);
    }

    /**
     * Creates a list of log files.
     * The received files are processed and LogRecords are generated from each line in the input files.
     * The header rows of each file are skipped, because these are of minor interest.
     * The bus address is extracted and added to each logRecord and a unique identifier is created and added to
     * each LogRecord as well.
     *
     * @param fileList : list of the selected files for import.
     */
    private void createLogFiles(List<File> fileList) {
        try {
            logFileList = new ArrayList<>();
            for (File file : fileList) {
                LogFile logFile = new LogFile();
                FileInputStream fstream = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    String[] tokens = strLine.split("\t");
                    // ignore all records with different line lengths from 5
                    if (tokens.length == 5) {
                        LogRecord record = new LogRecord(trimTimestamp(tokens[0]), convertMilliSeconds(tokens[1]), tokens[2], tokens[3], tokens[4], user, site, busLine);
                        if (logFile.isAddressSet() || record.getMessage().contains("address")) {
                            if (logFile.isAddressSet()) {
                                record.setAddress(logFile.getAddress());
                                record.setUniqueIdentifier(record.getAddress());
                            } else if (record.getMessage().contains("address")) {
                                logFile.setAddress(record.getMessage());
                                record.setAddress(logFile.getAddress());
                                record.setUniqueIdentifier(logFile.getAddress());
                            }
                            logFile.addLogRecord(record);
                        }
                    }
                }
                logFileList.add(logFile);
            }
        } catch (IOException e) {
            appDataController.setMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Extracts the logRecords from the logFileList and collects them in a LogRecordList
     * This is the appropriate for data base insert.
     *
     * @param logFileList : list of logFiles
     */
    private void createLogRecordList(List<LogFile> logFileList) {
        logRecordList = new ArrayList<>();
        for (LogFile logFile : logFileList) {
            for (LogRecord logRecord : logFile.getRecordList()) {
                logRecordList.add(logRecord);
            }
        }
    }

    /**
     * Hands the LogRecordList over to the logRecordDAOWriter.
     *
     * @param logRecordList List of LogRecords
     * @throws Exception if saving fails
     */
    private void saveToDB(List<LogRecord> logRecordList) throws Exception {
        LogRecordWriteDAO logRecordDAOWriter = new MySQLLogRecordWriteDAO();
        logRecordDAOWriter.insertLogRecords(logRecordList);
    }

    /**
     * Converts the millisecond string to an int
     *
     * @param milliseconds : milliseconds as a string
     * @return milliseconds as an int
     */
    private int convertMilliSeconds(String milliseconds) {
        String[] millis = milliseconds.split(" ");
        return Integer.parseInt(millis[0]);
    }

    /**
     * The timestamp from the first logRecord in a logfile can contain an additional null. This will cause
     * an Exception when the String gets converted to ZonedDateTime because of the non conform pattern.
     *
     * @param timestamp timestamp as a String
     * @return timestamp as a String
     */
    private String trimTimestamp(String timestamp) {
        return timestamp.substring(0, ImportFileConst.DATETIMEPATTERNIMPORT.length());
    }
}