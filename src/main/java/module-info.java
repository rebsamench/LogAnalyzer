module LogAnalyzer {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql.rowset;
    requires org.controlsfx.controls;

    opens ch.zhaw.javavertriefung.loganalyzer.app;
}