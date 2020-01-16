package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controls ui interactions of result panel. Provides methods to display search results in exportable table.
 *
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class ReportResultPanelUIController extends ExportPanelUIController implements Initializable, UIPanelController {
    @FXML
    private TableView<LogRecord> resultTable;
    private final ObservableList<LogRecord> tableData = FXCollections.observableArrayList();
    @FXML
    private Button exportButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        TableColumn<LogRecord, BusLine> busLineCol = new TableColumn<>("BusLine");
        busLineCol.setCellValueFactory(new PropertyValueFactory<>("busLine"));

        TableColumn<LogRecord, Integer> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<LogRecord, Source> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(new PropertyValueFactory<>("source"));

        TableColumn<LogRecord, EventType> eventTypeCol = new TableColumn<>("Type of event");
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("eventType"));

        TableColumn<LogRecord, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        // https://stackoverflow.com/questions/34638870/issues-with-unchecked-generics
        if (showMetaColumns) {
            resultTable.getColumns().addAll(Arrays.asList(idCol, createdCol, lastChangedCol,
                    createdUserColumn, timestampCol, millisecondsCol, siteCol,
                    busLineCol, addressCol, sourceCol, eventTypeCol, messageCol));
        } else {
            resultTable.getColumns().addAll(Arrays.asList(timestampCol, millisecondsCol, siteCol,
                    busLineCol, addressCol, sourceCol, eventTypeCol, messageCol));
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
        ObservableList<LogRecord> selectedItemsList;
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

    /**
     * Marks given LogRecord in resultTable by setting red background.
     * @param logRecord log record to mark
     */
    public void markLogRecordPermanently(LogRecord logRecord) {
        resultTable.setRowFactory(new Callback<>() {
            @Override
            public TableRow<LogRecord> call(TableView<LogRecord> resultTable) {
                return new TableRow<>() {
                    @Override
                    protected void updateItem(LogRecord logRecordFromTable, boolean b) {
                        super.updateItem(logRecordFromTable, b);
                        if (logRecordFromTable != null && logRecordFromTable.equals(logRecord)) {
                            setStyle("-fx-background-color: rgba(255, 0, 0, 0.2);-fx-text-fill: black;");
                        }
                    }
                };
            }
        });
    }

    @Override
    public boolean isAdminPanel() {
        return false;
    }
}