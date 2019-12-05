package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.QueryExecutor;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class SQLPanelUIController implements Initializable {
    @FXML
    private TextArea sqlStatementTextArea;
    @FXML
    private TableView<ObservableList> resultTable;
    @FXML
    private Button exportButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exportButton.disableProperty().bind(
                Bindings.isEmpty(resultTable.getItems())
        );
    }
    //TODO not working. somehow table is not being replaced
    public void executeQuery() {
        resultTable = QueryExecutor.getQueryResultTable(sqlStatementTextArea.getText());

    }

}
