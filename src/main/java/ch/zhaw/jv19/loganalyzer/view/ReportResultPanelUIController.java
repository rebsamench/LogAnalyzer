package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.Even;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controls ui interactions of report panel. Provides form to select criteria for log record
 * search and displaying search results.
 *
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class ReportResultPanelUIController extends ExportPanelUIController implements Initializable, UIPanelController {
    @FXML
    private TableView<LogRecord> resultTable;
    private ObservableList<LogRecord> tableData = FXCollections.observableArrayList();
    @FXML
    private Button exportButton;
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        // disable button when table is empty
        exportButton.disableProperty().bind(Bindings.isEmpty(tableData));
        resultTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }

    /**
     * Displays given log record list in resultTable
     * @param logRecordList   ArrayList of log records to display in table
     * @param showMetaColumns determines, weather technical meta columns are displayed
     */
    public void showTableData(ArrayList<LogRecord> logRecordList, boolean showMetaColumns) {
        tableData.clear();
        resultTable.getColumns().clear();
        tableData.addAll(logRecordList);
        buildLogRecordResultTable(tableData, showMetaColumns);
    }

    /**
     * Builds log record table from table data
     * @param tableData       table data
     * @param showMetaColumns determines, weather technical meta columns are displayed
     */
    private void buildLogRecordResultTable(ObservableList<LogRecord> tableData, boolean showMetaColumns) {
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

        TableColumn<LogRecord, Source> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(new PropertyValueFactory<>("source"));

        TableColumn<LogRecord, EventType> eventTypeCol = new TableColumn<>("Type of event");
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("eventType"));

        TableColumn<LogRecord, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        if (showMetaColumns) {
            resultTable.getColumns().addAll(idCol, createdCol, lastChangedCol,
                    createdUserColumn, timestampCol, millisecondsCol, siteCol,
                    busLineCol, addressCol, sourceCol, eventTypeCol, messageCol);
        } else {
            resultTable.getColumns().addAll(timestampCol, millisecondsCol, siteCol,
                    busLineCol, addressCol, sourceCol, eventTypeCol, messageCol);
        }
        resultTable.setItems(tableData);
    }

    /**
     * Exports current resultTable to Excel.
     */
    @FXML
    private void exportResultTable() {
        super.exportTable(resultTable);
    }

    /**
     * Gets List of log records, which the user selected
     * @return Observable list of selected log records
     */
    public ObservableList<LogRecord> getSelectedItems() {
        ObservableList<LogRecord> selectedItemsList = FXCollections.observableArrayList();
        selectedItemsList = resultTable.getSelectionModel().getSelectedItems();
        return selectedItemsList;
    }

    /**
     * Selects given LogRecord in resultTable
     * @param logRecord log record to select
     */
    public void selectLogRecord(LogRecord logRecord) {
        resultTable.getSelectionModel().select(logRecord);
    }

}