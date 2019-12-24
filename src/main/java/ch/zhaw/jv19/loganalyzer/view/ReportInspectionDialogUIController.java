package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppData;
import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.model.LogRecord;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ReportInspectionDialogUIController extends ExportPanelUIController implements Initializable, UIPanelController {
    @FXML
    private ReportResultPanelUIController reportResultPanelUIController; // controller of included sub panel
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
    }

    public void showSurroundingLogRecords(LogRecord logRecord, int secondsBefore, int secondsAfter) {
        HashMap<String, Object> searchConditions = new HashMap<>();
        searchConditions.put("loggedTimestampFrom", logRecord.getTimestamp().minusSeconds(secondsBefore));
        searchConditions.put("loggedTimestampUpTo", logRecord.getTimestamp().plusSeconds(secondsAfter));
        searchConditions.put("site", FXCollections.observableArrayList(logRecord.getSite()));
        searchConditions.put("busLine", FXCollections.observableArrayList(logRecord.getBusline()));
        searchConditions.put("address", logRecord.getAddress());
        reportResultPanelUIController.showTableData(appDataController.getLogRecordsListByConditions(searchConditions));
        // reselect log record from which analysis started
        reportResultPanelUIController.selectLogRecord(logRecord);

    }
}
