package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.MySQLLogRecordDAO;
import ch.zhaw.jv19.loganalyzer.view.ImportPanelUIController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileImportController {

    private User user;
    private Site site;
    private Busline busline;
    private List<LogRecord> logRecordList;

    public FileImportController() {}

    public FileImportController(User user, Site site, Busline busline, List fileList) {
        this.user = user;
        this.site = site;
        this.busline = busline;
        logRecordList = new ArrayList<>();
        createLogRecordList(fileList);
        saveToDB(logRecordList);
    }

    private void createLogRecordList(List<File> fileList) {
        try {
            for (File file : fileList) {
                FileInputStream fstream = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                while ((strLine = br.readLine()) != null) {
                    String[] tokens = strLine.split("\t");
                    LogRecord record = new LogRecord(tokens[0], Integer.parseInt(tokens[1]), tokens[2], tokens[3], tokens[4], user, site, busline);
                    logRecordList.add(record);
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void saveToDB(List logRecordList) {
        MySQLLogRecordDAO logRecordDAOWriter = new MySQLLogRecordDAO(logRecordList);
    }
}