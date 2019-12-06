package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.QueryExecutor;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ReportPanelUIController implements Initializable {
    private HashMap<String, Object> formData;
    @FXML
    private DatePicker createdFrom;
    @FXML
    private DatePicker createdUpTo;
    @FXML
    private CheckComboBox<String> createdUser;
    @FXML
    private DatePicker loggedTimestampFrom;
    @FXML
    private DatePicker loggedTimestampUpTo;
    @FXML
    private CheckComboBox<String> recordType;
    @FXML
    private CheckComboBox<String> site;
    @FXML
    private TextField messageSubstring;
    @FXML
    private TextField sqlStatement;
    @FXML
    private TableView<ObservableList> resultTable;
    @FXML
    private Button exportButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportButton.disableProperty().bind(
                Bindings.isEmpty(resultTable.getItems())
        );
        getUserList();
        getSiteList();
        getBusList();
    }

    @FXML
    private void search() {
        resultTable.getItems().clear();
        prepareFormData();
        resultTable = QueryExecutor.getLogRecordsTable(formData);
    }

    @FXML
    private void exportResultTable() {
    }

    // TODO get User list from DB over AppData
    private void getUserList() {

    }

    // TODO get Site list from DB over AppData
    private void getSiteList() {

    }

    // TODO get bus list from DB over AppData
    private void getBusList() {

    }

    private void prepareFormData() {
        formData = new HashMap<>();
        if (createdFrom.getValue() != null) {
            formData.put(createdFrom.getId(), DateUtil.getUtcStartOfDayFromDate(createdFrom.getValue()));
        }
        if (createdFrom.getValue() != null) {
            formData.put(createdUpTo.getId(), DateUtil.getUtcEndOfDayFromDate(createdUpTo.getValue()));
        }
        if (createdFrom.getValue() != null) {
            formData.put(loggedTimestampFrom.getId(), DateUtil.getUtcEndOfDayFromDate(loggedTimestampFrom.getValue()));
        }
        if (createdFrom.getValue() != null) {
            formData.put(loggedTimestampUpTo.getId(), DateUtil.getUtcEndOfDayFromDate(loggedTimestampUpTo.getValue()));
        }
        if (createdFrom.getValue() != null) {
            formData.put(createdUser.getId(), createdUser.getCheckModel().getCheckedItems());
        }
        if (createdFrom.getValue() != null) {
            formData.put(createdUser.getId(), createdUser.getCheckModel().getCheckedItems());
        }
        if (createdFrom.getValue() != null) {
            formData.put(recordType.getId(), recordType.getCheckModel().getCheckedItems());
        }
        if (createdFrom.getValue() != null) {
            formData.put(site.getId(), site.getItems());
        }
        if (createdFrom.getValue() != null) {
            formData.put(messageSubstring.getId(), messageSubstring.getText());
        }
    }
}