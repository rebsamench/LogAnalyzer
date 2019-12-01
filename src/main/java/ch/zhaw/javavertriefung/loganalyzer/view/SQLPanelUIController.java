package ch.zhaw.javavertriefung.loganalyzer.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import ch.zhaw.javavertriefung.loganalyzer.util.db.DBUtil;

import java.sql.ResultSet;

public class SQLPanelUIController {
    @FXML
    private TextArea sqlStatementTextArea;
    @FXML
    private TableView<ObservableList> sqlResultTable;
    private ObservableList<ObservableList> resultList;

    public void handleSqlExecuteStatement() {
        try {
            System.out.println(sqlStatementTextArea.getText());
            ResultSet queryResultSet = DBUtil.dbExecuteSingleQuery(sqlStatementTextArea.getText());
            resultList = FXCollections.observableArrayList();
            sqlResultTable.getColumns().clear();
            //check if any row is in resultset, https://stackoverflow.com/questions/867194/java-resultset-how-to-check-if-there-are-any-results/6813771#6813771
            if(queryResultSet.isBeforeFirst()) {
                for (int i = 0; i < queryResultSet.getMetaData().getColumnCount(); i++) {
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn(queryResultSet.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                    sqlResultTable.getColumns().addAll(col);
                    //System.out.println("Column [" + i + "] ");
                }
                while (queryResultSet.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= queryResultSet.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        row.add(queryResultSet.getString(i));
                    }
                    System.out.println("Row [1] added " + row);
                    resultList.add(row);

                }
                sqlResultTable.setItems(resultList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public void handleSqlExecuteScript(ActionEvent actionEvent) {
    }
}
