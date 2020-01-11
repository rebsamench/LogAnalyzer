package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class ReportInspectionDialogUIController extends ExportPanelUIController implements Initializable, UIPanelController {
    @FXML
    private HBox resultPanelContainer;
    private AppDataController appDataController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
    }

    public void showSurroundingLogRecordsTable(LogRecord logRecord, int secondsBefore, int secondsAfter) {
        TreeMap<String, Object> searchConditions = new TreeMap<>();
        searchConditions.put("loggedTimestampFrom", logRecord.getTimestamp().minusSeconds(secondsBefore));
        searchConditions.put("loggedTimestampUpTo", logRecord.getTimestamp().plusSeconds(secondsAfter));
        searchConditions.put("site", FXCollections.observableArrayList(logRecord.getSite()));
        searchConditions.put("busLine", FXCollections.observableArrayList(logRecord.getBusline()));
        searchConditions.put("address", logRecord.getAddress());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportResultPanel.fxml"));
        Parent root = null;
        try {
            AnchorPane resultTablePanel = fxmlLoader.load();
            ReportResultPanelUIController uiPanelController = fxmlLoader.getController();
            uiPanelController.showTableData(appDataController.getLogRecordsListByConditions(searchConditions), false);
            // reselect log record from which analysis started
            uiPanelController.selectLogRecord(logRecord);
            resultPanelContainer.getChildren().add(resultTablePanel);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
