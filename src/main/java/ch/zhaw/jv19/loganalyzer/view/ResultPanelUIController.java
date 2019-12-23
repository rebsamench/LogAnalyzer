package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controls ui interactions of report panel. Provides form to select criteria for log record
 * search and displaying search results.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class ResultPanelUIController implements Initializable, UIPanelController {
    private HashMap<String, Object> formData;
    @FXML
    private TableView<LogRecord> resultTable;
    private ObservableList<LogRecord> tableData = FXCollections.observableArrayList();
    @FXML
    private Button exportButton;
    @FXML
    private VBox furtherAnalysisBox;
    @FXML
    private TextField secondsBefore;
    @FXML
    private TextField secondsAfter;
    private static final String REPORT_PANEL_DATETIME_PATTERN = "yyyy-MM-ddHH:mm:ss";
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // disable button when table is empty
        exportButton.disableProperty().bind(Bindings.isEmpty(tableData));
        //disable further analysis panel when no table row is selected
        furtherAnalysisBox.disableProperty().bind(Bindings.isEmpty(resultTable.getSelectionModel().getSelectedItems()));
        appDataController = AppDataController.getInstance();
    }

    public void showTableData(ArrayList<LogRecord> logRecordList) {
        tableData.addAll(logRecordList);
        resultTable.getItems().clear();
        resultTable.getColumns().clear();
        buildLogRecordResultTable(tableData);
    }

    /**
     * Builds log record table from table data
     * @param tableData table data
     */
    private void buildLogRecordResultTable(ObservableList<LogRecord> tableData) {
        TableColumn<LogRecord, Integer> idCol = new TableColumn<>("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<LogRecord, ZonedDateTime> createdCol = new TableColumn<>("Created");
        createdCol.setCellValueFactory(new PropertyValueFactory<>("created"));

        TableColumn<LogRecord, ZonedDateTime> lastChangedCol = new TableColumn<>("Last changed");
        lastChangedCol.setCellValueFactory(new PropertyValueFactory<>("lastChanged"));

        TableColumn<LogRecord, User> createdUserColumn = new TableColumn<>("User");
        createdUserColumn.setCellValueFactory(new PropertyValueFactory<>("user"));

        // omit unique_identifier, not relevant
        TableColumn<LogRecord, ZonedDateTime> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        TableColumn<LogRecord, Integer> millisecondsCol = new TableColumn<>("Milliseconds");
        millisecondsCol.setCellValueFactory(new PropertyValueFactory<>("milliseconds"));

        TableColumn<LogRecord, Site> siteCol = new TableColumn<>("Site");
        siteCol.setCellValueFactory(new PropertyValueFactory<>("site"));

        TableColumn<LogRecord, Busline> busLineCol = new TableColumn<>("Busline");
        busLineCol.setCellValueFactory(new PropertyValueFactory<>("busline"));

        TableColumn<LogRecord, Integer> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<LogRecord, String> eventTypeCol = new TableColumn<>("Type of event");
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("eventType"));

        TableColumn<LogRecord, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        resultTable.getColumns().addAll(idCol, createdCol, lastChangedCol,
                createdUserColumn, timestampCol, millisecondsCol, siteCol,
                busLineCol, addressCol, eventTypeCol, messageCol);
        resultTable.setItems(tableData);
    }

    /**
     * Exports current resultTable to Excel. Default file name is Export_[currentDateTime].xlsx.
     */
    @FXML
    private void exportResultTable() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export table");
        chooser.setInitialFileName("Export_" + DateUtil.getCurrentDateTimeString("yyyy-MM-dd_HHmm") + ".xlsx");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx"));
        File file = chooser.showSaveDialog(exportButton.getScene().getWindow());
        appDataController.exportToExcel(resultTable, file);
        appDataController.setMessage("File successfully saved to " + file.getAbsolutePath());
    }
}