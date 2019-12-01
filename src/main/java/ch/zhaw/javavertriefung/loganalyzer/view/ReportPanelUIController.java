package ch.zhaw.javavertriefung.loganalyzer.view;

import ch.zhaw.javavertriefung.loganalyzer.util.datatypes.DateUtil;
import ch.zhaw.javavertriefung.loganalyzer.model.SearchReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import ch.zhaw.javavertriefung.loganalyzer.model.SearchCondition;
import org.controlsfx.control.CheckComboBox;
import ch.zhaw.javavertriefung.loganalyzer.util.db.MySQLKeyword;
import ch.zhaw.javavertriefung.loganalyzer.util.db.QueryBuilder;

public class ReportPanelUIController {
    private SearchReport searchReport;
    @FXML
    private DatePicker createdFrom;
    @FXML
    private DatePicker createdUpTo;
    @FXML
    private ComboBox<String> createdUser;
    @FXML
    private DatePicker replacedFrom;
    @FXML
    private DatePicker replacedUpTo;
    @FXML
    private ComboBox<String> replacedUser;
    @FXML
    private DatePicker loggedTimestampFrom;
    @FXML
    private DatePicker loggedTimestampUpTo;
    @FXML
    private CheckComboBox<String> recordType;
    @FXML
    private ComboBox<String> site;
    @FXML
    private TextField messageLike;
    @FXML
    private TextField sqlStatement;

    //TODO validate Date: https://stackoverflow.com/questions/28432576/javafx-datepicker-validation
    // separate UI from model, this fx controller must not know about SearchReport and QueryBuilder
    public ReportPanelUIController() {
        searchReport = new SearchReport();
    }

    public void updateSearchReport() {
        if (createdFrom.getValue() != null) {
            searchReport.addCondition(new SearchCondition(createdFrom.getId(), DateUtil.getUtcStartOfDayFromDate(createdFrom.getValue()), MySQLKeyword.AND, MySQLKeyword.EQUALS));
        }
        if (createdUpTo.getValue() != null) {
            searchReport.addCondition(new SearchCondition(createdUpTo.getId(), DateUtil.getUtcEndOfDayFromDate(createdUpTo.getValue()), MySQLKeyword.AND, MySQLKeyword.EQUALS));
        }
        if (loggedTimestampFrom.getValue() != null) {
            searchReport.addCondition(new SearchCondition(loggedTimestampFrom.getId(), DateUtil.getUtcEndOfDayFromDate(loggedTimestampFrom.getValue()), MySQLKeyword.AND, MySQLKeyword.EQUALS));
        }
        if (loggedTimestampUpTo.getValue() != null) {
            searchReport.addCondition(new SearchCondition(loggedTimestampUpTo.getId(), DateUtil.getUtcEndOfDayFromDate(loggedTimestampUpTo.getValue()), MySQLKeyword.AND, MySQLKeyword.EQUALS));
        }
        if (createdUser.getValue() != null) {
            searchReport.addCondition(new SearchCondition(createdUser.getId(), createdUser.getItems(), MySQLKeyword.AND, MySQLKeyword.EQUALS));
        }
        if (recordType.getItems().size() > 0) {
            searchReport.addCondition(new SearchCondition(recordType.getId(), recordType.getItems(), MySQLKeyword.AND, MySQLKeyword.EQUALS));
        }
        if (site.getSelectionModel().getSelectedItem() != null) {
            searchReport.addCondition(new SearchCondition(site.getId(), site.getSelectionModel().getSelectedItem(), MySQLKeyword.AND, MySQLKeyword.EQUALS));
        }
        if (!messageLike.getText().trim().isEmpty()) {
            searchReport.addCondition(new SearchCondition(messageLike.getId(), messageLike.getText(), MySQLKeyword.AND, MySQLKeyword.LIKE));
        }
    }


    public void searchWithReportSettings(ActionEvent actionEvent) {
        updateSearchReport();
        sqlStatement.setText("SELECT * FROM TABLE " + QueryBuilder.getSQLConditionsFromList(searchReport.getFormData()));
    }
}