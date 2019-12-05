package ch.zhaw.jv19.loganalyzer.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileImport {

    private User user;
    private Site site;
    private Busline busline;
    private ArrayList<LogFile> logFileList;

    public FileImport(User user, Site site, Busline busline, List fileList) {
        this.user = user;
        this.site = site;
        this.busline = busline;
        logFileList = new ArrayList<>();
        createLogFileList(fileList);
        saveToDB(logFileList);
    }

    private void createLogFileList(List fileList) {
        for (Object file : fileList) {
                logFileList.add(readLogFiles((File) file));
        }
    }

    private LogFile readLogFiles(File file){
        try{
            LogFile logFile = new LogFile();
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String[] tokens = strLine.split("\t");
                LogRecord record = new LogRecord(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
                logFile.creatRecordList(record);
            }
            return logFile;

        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void saveToDB(ArrayList logFileList) {
        // TODO
    }
}
