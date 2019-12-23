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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controls ui interactions of report panel. Provides form to select criteria for log record
 * search and displaying search results.
 * resultPanelUIController is the controller of the included subpanel (fx:include) and
 * is created by FXML loader. Therefore the id attribute on fx:include tag is resultPanelUI
 *
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
// TODO DatePicker cannot be nulled after date is set
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
    private CheckComboBox<String> type;
    @FXML
    private CheckComboBox<Site> site;
    @FXML
    private CheckComboBox<Busline> busLine;
    @FXML
    private TextField message;
    @FXML
    private TextArea sqlStatement;
    @FXML
    private ResultPanelUIController resultPanelUIController; // controller of included sub panel
    private static final String REPORT_PANEL_DATETIME_PATTERN = "yyyy-MM-ddHH:mm:ss";
    private static final String REPORT_PANEL_DATE_PATTERN = "yyyy-MM-dd";
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // fill comboboxes on first use, since appdatacontroller is not yet set when initialize method is called
        createdUser.addEventHandler(ComboBox.ON_SHOWING, event -> fillUserComboBox());
        type.addEventHandler(ComboBox.ON_SHOWING, event -> fillRecordTypeComboBox());
        site.addEventHandler(ComboBox.ON_SHOWING, event -> fillSiteComboBox());
        busLine.addEventHandler(ComboBox.ON_SHOWING, event -> fillBusLineComboBox());
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
        prepareFormData();
        resultPanelUIController.showTableData(appDataController.getLogRecordsListByConditions(formData));
    }

    /**
     * Fills user combobox if it is empty
     */
    private void fillUserComboBox() {
        if (createdUser.getItems().size() == 0) {
            createdUser.getItems().addAll(appDataController.getUserList());
        }
    }

    /**
     * Fills site combobox if it is empty
     */
    private void fillSiteComboBox() {
        if (site.getItems().size() == 0) {
            site.getItems().addAll(appDataController.getSiteList());
        }
    }

    /**
     * Fills record type combobox if it is empty
     */
    private void fillRecordTypeComboBox() {
        if (type.getItems().size() == 0) {
            type.getItems().addAll(appDataController.getRecordTypeList());
        }
    }

    /**
     * Fill busline combobox if it is empty
     */
    private void fillBusLineComboBox() {
        if (busLine.getItems().size() == 0) {
            busLine.getItems().addAll(appDataController.getBuslineList());
        }
    }

    /**
     * Collects all entered form data in a hashmap and stores map in instance variable formData
     */
    private void prepareFormData() {
        formData = new HashMap<>();
        if (createdFrom.getValue() != null) {
            formData.put(createdFrom.getId(), createdFromTime.getText().trim().isEmpty() ?
                    DateUtil.getSystemTimezoneStartOfDayFromDate(createdFrom.getValue()) :
                    DateUtil.getZonedDateTimeFromDateTimeString(createdFrom.getValue() +
                            createdFromTime.getText(), REPORT_PANEL_DATETIME_PATTERN));
        }
        if (createdUpTo.getValue() != null) {
            formData.put(createdUpTo.getId(),
                    createdUpToTime.getText().trim().isEmpty() ?
                            DateUtil.getSystemTimezoneEndOfDayFromDate(createdUpTo.getValue()) :
                            DateUtil.getZonedDateTimeFromDateTimeString(createdUpTo.getValue() +
                                    createdUpToTime.getText(), REPORT_PANEL_DATETIME_PATTERN));
        }
        if (loggedTimestampFrom.getValue() != null) {
            formData.put(loggedTimestampFrom.getId(),
                    loggedTimestampFromTime.getText().trim().isEmpty() ?
                            DateUtil.getSystemTimezoneStartOfDayFromDate(loggedTimestampFrom.getValue()) :
                            DateUtil.getZonedDateTimeFromDateTimeString(loggedTimestampFrom.getValue() +
                                    loggedTimestampFromTime.getText(), REPORT_PANEL_DATETIME_PATTERN));
        }
        if (loggedTimestampUpTo.getValue() != null) {
            formData.put(loggedTimestampUpTo.getId(), loggedTimestampUpToTime.getText().trim().isEmpty() ?
                    DateUtil.getSystemTimezoneEndOfDayFromDate(loggedTimestampUpTo.getValue()) :
                    DateUtil.getZonedDateTimeFromDateTimeString(loggedTimestampUpTo.getValue() +
                            loggedTimestampUpToTime.getText(), REPORT_PANEL_DATETIME_PATTERN));
        }
        if (createdUser.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(createdUser.getId(), createdUser.getCheckModel().getCheckedItems());
        }
        if (type.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(type.getId(), type.getCheckModel().getCheckedItems());
        }
        if (site.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(site.getId(), site.getCheckModel().getCheckedItems());
        }
        if (busLine.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(busLine.getId(), busLine.getCheckModel().getCheckedItems());
        }
        if (!message.getText().isEmpty()) {
            formData.put(message.getId(), message.getText());
        }
    }

    /**
     * Validates entered time and sets style class 'invalid' if entered
     * time has unexpected format
     *
     * @param textField text field containing time
     * @param focus     defines if focus is set. only when focus was removed (focus = false),
     *                  time is validated
     */
    private void timeFieldChanged(boolean focus, TextField textField) {
        if (!focus) {
            // remove in case attribute is already set in case of previous validation (clearing field after entering invalid time)
            textField.getStyleClass().removeAll("invalid");
            if (!textField.getText().isEmpty() && !isTimeValid(textField.getText())) {
                textField.getStyleClass().add("invalid");
            }
        }
    }

    /**
     * Validates given time string
     * Regex:
     * hours: 0 or 1 + any digit from 0 to 9 OR 2 + any digit from 0 to 3 (00 - 23)
     * minutes: 0 to 5 + any digit from 0 to 9 (00 - 59)
     * seconds: see minutes
     *
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