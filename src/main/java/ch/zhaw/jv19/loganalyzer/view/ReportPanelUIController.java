package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReportPanelUIController implements Initializable, UIPanelController {
    private HashMap<String, Object> formData;
    @FXML
    private DatePicker createdFrom;
    @FXML
    private TextField createdFromTime;
    @FXML
    private DatePicker createdUpTo;
    @FXML
    private TextField createdUpToTime;
    @FXML
    private CheckComboBox<User> createdUser;
    @FXML
    private DatePicker loggedTimestampFrom;
    @FXML
    private TextField loggedTimestampFromTime;
    @FXML
    private DatePicker loggedTimestampUpTo;
    @FXML
    private TextField loggedTimestampUpToTime;
    @FXML
    private CheckComboBox<String> recordType;
    @FXML
    private CheckComboBox<String> site;
    @FXML
    private CheckComboBox<String> busLine;
    @FXML
    private TextField message;
    @FXML
    private TextArea sqlStatement;
    @FXML
    private TableView<ObservableList> resultTable;
    @FXML
    private Button exportButton;
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportButton.disableProperty().bind(
                Bindings.isEmpty(resultTable.getItems())
        );
        // fill comboboxes on first use, since appdatacontroller is not yet set when initialize method is called
        createdUser.addEventHandler(ComboBox.ON_SHOWING, event -> fillUserComboBox());
        recordType.addEventHandler(ComboBox.ON_SHOWING, event -> fillRecordTypeComboBox());
        site.addEventHandler(ComboBox.ON_SHOWING, event -> fillSiteComboBox());
        busLine.addEventHandler(ComboBox.ON_SHOWING, event -> fillBusLineComboBox());
        createdUser.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user.getName();
            }

            @Override
            public User fromString(String string) {
                return createdUser.getItems().stream().filter(user ->
                        user.getName().equals(string)).findFirst().orElse(null);
            }
        });
        // listener for focus change in order to validate entered time
        createdFromTime.focusedProperty().addListener((o, oldValue, newValue) -> timeFieldChanged(newValue, createdFromTime));
        createdUpToTime.focusedProperty().addListener((o, oldValue, newValue) -> timeFieldChanged(newValue, createdUpToTime));
        loggedTimestampFromTime.focusedProperty().addListener((o, oldValue, newValue) -> timeFieldChanged(newValue, loggedTimestampFromTime));
        loggedTimestampUpToTime.focusedProperty().addListener((o, oldValue, newValue) -> timeFieldChanged(newValue, loggedTimestampUpToTime));
    }

    /**
     * Executes search with given formData and displays search result in resultTable
     */
    @FXML
    private void search() {
        resultTable.getItems().clear();
        resultTable.getColumns().clear();
        prepareFormData();
        TableView<ObservableList> returnedTable = appDataController.getLogRecordsTableByConditions(formData);
        if (returnedTable != null) {
            // replace columns and rows of resultTable, since replacing resultTable not working
            resultTable.getColumns().addAll(returnedTable.getColumns());
            resultTable.getItems().addAll(returnedTable.getItems());
        }
    }

    /**
     * Exports current
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

    /**
     * Fill user combobox if it is empty
     */
    private void fillUserComboBox() {
        if (createdUser.getItems().size() == 0) {
            createdUser.getItems().addAll(appDataController.getUserList());
        }
    }

    /**
     * Fill site combobox if it is empty
     */
    private void fillSiteComboBox() {

    }

    /**
     * Fill busline combobox if it is empty
     */    private void fillRecordTypeComboBox() {

    }

    /**
     * Fill busline combobox if it is empty
     */    private void fillBusLineComboBox() {

    }

    /**
     * Collects all entered form data in a hashmap and stores map in instance variable formData
     */
    private void prepareFormData() {
        formData = new HashMap<>();
        if (createdFrom.getValue() != null) {
            formData.put(createdFrom.getId(), DateUtil.getSystemTimezoneStartOfDayFromDate(createdFrom.getValue()));
        }
        if (createdUpTo.getValue() != null) {
            formData.put(createdUpTo.getId(), DateUtil.getSystemTimezoneEndOfDayFromDate(createdUpTo.getValue()));
        }
        if (loggedTimestampFrom.getValue() != null) {
            formData.put(loggedTimestampFrom.getId(), DateUtil.getSystemTimezoneEndOfDayFromDate(loggedTimestampFrom.getValue()));
        }
        if (loggedTimestampUpTo.getValue() != null) {
            formData.put(loggedTimestampUpTo.getId(), DateUtil.getSystemTimezoneEndOfDayFromDate(loggedTimestampUpTo.getValue()));
        }
        if (createdUser.getCheckModel().getCheckedItems().size() > 0) {
            ObservableList<User> userList = createdUser.getCheckModel().getCheckedItems();
            ArrayList<String> userNameList = new ArrayList<>(userList.stream()
                    .map(user -> user.getName())
                    .collect(Collectors.toList()));
            formData.put(createdUser.getId(), userNameList);
        }
        if (recordType.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(recordType.getId(), recordType.getCheckModel().getCheckedItems());
        }
        if (site.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(site.getId(), site.getCheckModel().getCheckedItems());
        }
        if (!message.getText().isEmpty()) {
            formData.put(message.getId(), message.getText());
        }
    }

    /**
     * Validates entered time and sets style class 'invalid' if entered
     * time has unexpected format
     * @param textField text field containing time
     * @param focus defines if focus is set. only when focus was removed (focus = false),
     *              time is validated
     */
    private void timeFieldChanged(boolean focus, TextField textField) {
        if (!focus) {
            if (!isTimeValid(textField.getText())) {
                textField.getStyleClass().add("invalid");
            }
            else {
                textField.getStyleClass().removeAll("invalid");
            }
        }
    }

    /**
     * Validates given time string
     * Regex:
     * hours: 0 or 1 + any digit from 0 to 9 OR 2 + any digit from 0 to 3 (00 - 23)
     * minutes: 0 to 5 + any digit from 0 to 9 (00 - 59)
     * seconds: see minutes
     * @param timeString time string to be validated
     * @return true if time string is valid and false if not
     */
    public boolean isTimeValid(String timeString) {
        return (timeString.matches("(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)"));
    }

    @Override
    public void setAppDataController(AppDataController appDataController) {
        this.appDataController = appDataController;
    }
}