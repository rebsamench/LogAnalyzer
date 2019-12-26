package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.BaseDataUserTableData;
import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLUserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BaseDataUserPanelUIController implements Initializable {

    private ObservableList<BaseDataUserTableData> data = FXCollections.observableArrayList();
    private MySQLUserDAO mySQLUserDAO;

    @FXML
    private TableView<BaseDataUserTableData> baseDataUserTable;

    @FXML
    private TextField createdUser;

    @FXML
    private TextField name;

    @FXML
    private TextField password;

    @FXML
    private TextField isadmin;

    @FXML
    private Button submitt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateTableData();
        baseDataUserTable.setItems(data);
    }

    private void generateTableData() {
        try {
            for (User user : mySQLUserDAO.getAllUsersList()) {
                data.add(new BaseDataUserTableData(user));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
