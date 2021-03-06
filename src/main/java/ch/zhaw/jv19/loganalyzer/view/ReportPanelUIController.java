package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.ui.UIUtil;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;

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
    private TreeMap<String, Object> formData;
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
    private CheckComboBox<EventType> eventType;
    @FXML
    private CheckComboBox<Source> source;
    @FXML
    private CheckComboBox<Site> site;
    @FXML
    private CheckComboBox<BusLine> busLine;
    @FXML
    private TextField address;
    @FXML
    private TextField message;
    @FXML
    private VBox furtherAnalysisBox;
    @FXML
    private TextField secondsBefore;
    @FXML
    private TextField secondsAfter;
    @FXML
    private ReportResultPanelUIController reportResultPanelUIController; // controller of included sub panel
    private static final String REPORT_PANEL_DATETIME_PATTERN = "yyyy-MM-ddHH:mm:ss";
    private AppDataController appDataController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        // fill comboboxes on first use
        createdUser.addEventHandler(ComboBox.ON_SHOWING, event -> fillUserComboBox());
        // Workaround for refresh issue with CheckComboBox: https://bitbucket.org/controlsfx/controlsfx/issues/537/add-setitems-method-to-checkcombobox
        Bindings.bindContent(createdUser.getItems(),appDataController.getUserList());
        Bindings.bindContent(site.getItems(),appDataController.getSiteList());
        Bindings.bindContent(busLine.getItems(),appDataController.getBusLineList());
        // fill comboboxes only if needed
        busLine.addEventHandler(ComboBox.ON_SHOWING, event -> fillBusLineComboBox());
        source.addEventHandler(ComboBox.ON_SHOWING, event -> fillSourceComboBox());
        eventType.addEventHandler(ComboBox.ON_SHOWING, event -> fillEventTypeComboBox());
        site.addEventHandler(ComboBox.ON_SHOWING, event -> fillSiteComboBox());
        // listener for focus change in order to validate entered time
        createdFromTime.focusedProperty().addListener((o, oldValue, newValue) -> timeFieldChanged(newValue, createdFromTime));
        createdUpToTime.focusedProperty().addListener((o, oldValue, newValue) -> timeFieldChanged(newValue, createdUpToTime));
        loggedTimestampFromTime.focusedProperty().addListener((o, oldValue, newValue) -> timeFieldChanged(newValue, loggedTimestampFromTime));
        loggedTimestampUpToTime.focusedProperty().addListener((o, oldValue, newValue) -> timeFieldChanged(newValue, loggedTimestampUpToTime));
        UIUtil.addLengthEventFilter(createdFromTime, 8);
        UIUtil.addLengthEventFilter(createdUpToTime, 8);
        UIUtil.addLengthEventFilter(loggedTimestampFromTime, 8);
        UIUtil.addLengthEventFilter(loggedTimestampUpToTime, 8);
        //disable further analysis box when no table row is selected
        furtherAnalysisBox.disableProperty().bind(Bindings.isEmpty(reportResultPanelUIController.getSelectedItems()));
        // allow numeric values only
        UIUtil.addNumericEventFilter(secondsBefore);
        UIUtil.addNumericEventFilter(secondsAfter);
        UIUtil.addNumericEventFilter(address);
    }

    /**
     * Executes search with given formData and displays search result in resultTable
     */
    @FXML
    private void search() {
        prepareFormData();
        reportResultPanelUIController.showTableData(appDataController.getLogRecordsListByConditions(formData), true);
    }

    /**
     * Fills user combobox if it is empty
     */
    private void fillUserComboBox() {
        if (createdUser.getItems().size() == 0) {
            for(User user : appDataController.getUserList()) {
                createdUser.getItems().add(user);
            }
        }
    }

    /**
     * Fills site combobox if it is empty
     */
    private void fillSiteComboBox() {
        if (site.getItems().size() == 0) {
            site.getItems().setAll(appDataController.getSiteList());
        }
    }

    /**
     * Fills record type combobox if it is empty
     */
    private void fillEventTypeComboBox() {
        if (eventType.getItems().size() == 0) {
            eventType.getItems().addAll(appDataController.getEventTypeList());
        }
    }

    /**
     * Fill busLine combobox if it is empty
     */
    private void fillBusLineComboBox() {
        if (busLine.getItems().size() == 0) {
            busLine.getItems().addAll(appDataController.getBusLineList());
        }
    }

    /**
     * Fills source combobox if it is empty
     */
    private void fillSourceComboBox() {
        if (source.getItems().size() == 0) {
            source.getItems().addAll(appDataController.getSourceList());
        }
    }

    /**
     * Collects all entered form data in a tree map and stores map in instance variable formData
     */
    private void prepareFormData() {
        formData = new TreeMap<>();
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
        if (eventType.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(eventType.getId(), eventType.getCheckModel().getCheckedItems());
        }
        if (site.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(site.getId(), site.getCheckModel().getCheckedItems());
        }
        if (busLine.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(busLine.getId(), busLine.getCheckModel().getCheckedItems());
        }
        if (source.getCheckModel().getCheckedItems().size() > 0) {
            formData.put(source.getId(), source.getCheckModel().getCheckedItems());
        }
        if (!address.getText().isEmpty()) {
            formData.put(address.getId(), Integer.parseInt(address.getText()));
        }
        if (!message.getText().isEmpty()) {
            formData.put(message.getId(), message.getText());
        }
    }

    /**
     * Validates entered time and sets style class 'invalid' if entered
     * time has unexpected format
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
    private boolean isTimeValid(String timeString) {
        return (timeString.matches("(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)"));
    }

    /**
     * Opens an inspection dialog for each selected log record (multiple selection allowed).
     */
    @FXML
    private void inspectSelectedRecords() {
        final Stage inspectionDialog = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportInspectionDialog.fxml"));
        // secondsBefore and secondsAfter allow numeric values only, therefore parse exception impossible
        int secondsBeforeInt = secondsBefore.getText().isEmpty() ? 0 : Integer.parseInt(secondsBefore.getText());
        int secondsAfterInt = secondsBefore.getText().isEmpty() ? 0 : Integer.parseInt(secondsAfter.getText());
        try {
            Parent root = fxmlLoader.load();
            Scene inspectionScene = new Scene(root);
            ReportInspectionDialogUIController uiPanelController = fxmlLoader.getController();
            for (LogRecord logRecord : reportResultPanelUIController.getSelectedItems()) {
                uiPanelController.showSurroundingLogRecordsTable(logRecord, secondsBeforeInt, secondsAfterInt);
            }
            inspectionDialog.setTitle("Log Record Inspector");
            inspectionDialog.initModality(Modality.WINDOW_MODAL);
            inspectionDialog.setScene(inspectionScene);
            inspectionDialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isAdminPanel() {
        return false;
    }
}