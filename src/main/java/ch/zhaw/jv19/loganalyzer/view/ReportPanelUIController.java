package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

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
    private DatePicker createdUpTo;
    @FXML
    private CheckComboBox<User> createdUser;
    @FXML
    private DatePicker loggedTimestampFrom;
    @FXML
    private DatePicker loggedTimestampUpTo;
    @FXML
    private CheckComboBox<String> recordType;
    @FXML
    private CheckComboBox<String> site;
    @FXML
    private TextField message;
    @FXML
    private TextField sqlStatement;
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
        createdUser.addEventHandler(ComboBox.ON_SHOWING, event -> {
            fillUserList();
        });
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
    }

    @FXML
    private void search() {
        resultTable.getItems().clear();
        prepareFormData();
        TableView<ObservableList> returnedTable = appDataController.getLogRecordsTableByConditions(formData);
        if(returnedTable != null) {
            // appDataController.setMessage(returnedTable.getItems().size() + " Einträge geladen.");
            resultTable.getColumns().addAll(returnedTable.getColumns());
            resultTable.getItems().addAll(returnedTable.getItems());
        }
    }

    @FXML
    private void exportResultTable() {
    }

    private void fillUserList() {
        if (createdUser.getItems().size() == 0) {
            createdUser.getItems().addAll(appDataController.getUserList());
        }
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
        if (createdUpTo.getValue() != null) {
            formData.put(createdUpTo.getId(), DateUtil.getUtcEndOfDayFromDate(createdUpTo.getValue()));
        }
        if (loggedTimestampFrom.getValue() != null) {
            formData.put(loggedTimestampFrom.getId(), DateUtil.getUtcEndOfDayFromDate(loggedTimestampFrom.getValue()));
        }
        if (loggedTimestampUpTo.getValue() != null) {
            formData.put(loggedTimestampUpTo.getId(), DateUtil.getUtcEndOfDayFromDate(loggedTimestampUpTo.getValue()));
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

    @Override
    public void setAppDataController(AppDataController appDataController) {
        this.appDataController = appDataController;
    }
}