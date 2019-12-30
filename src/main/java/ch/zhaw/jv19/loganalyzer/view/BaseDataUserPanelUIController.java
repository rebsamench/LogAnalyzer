package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppData;
import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.model.BaseDataUserTableData;
import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLUserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BaseDataUserPanelUIController implements Initializable {

    private ObservableList<BaseDataUserTableData> tableData = FXCollections.observableArrayList();
    private AppDataController appDataController;
    private MySQLUserDAO mySQLUserDAO;

    @FXML
    private TextField fieldCreatedUser;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldPassword;

    @FXML
    private TextField fieldIsadmin;

    @FXML
    private Button submitt;

    @FXML
    private TableView<BaseDataUserTableData> baseDataUserTable;

    @FXML
    private TableColumn<BaseDataUserTableData, String> createdUserColumn;

    @FXML
    private TableColumn<BaseDataUserTableData, String> nameColumn;

    @FXML
    private TableColumn<BaseDataUserTableData, String> passwordColumn;

    @FXML
    private TableColumn<BaseDataUserTableData, Integer> isadminColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        populate();
        baseDataUserTable.setItems(tableData);
        setupCreatedUserColumn();
        setupNameColumn();
        setupPasswordColumn();
        setupIsadminColumn();
        baseDataUserTable.setEditable(true);
    }

    private void populate() {
        for (User user : appDataController.getUserList()) {
            tableData.add(new BaseDataUserTableData(user));
        }
    }

    private void setupCreatedUserColumn() {
        TableColumn<BaseDataUserTableData, String> createdUserColumn = new TableColumn<>("Created User");
        createdUserColumn.setCellValueFactory(new PropertyValueFactory<>("createdUser"));
        createdUserColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataUserTable.getColumns().add(createdUserColumn);
        createdUserColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<BaseDataUserTableData, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setCreatedUser(t.getNewValue())
        );
    }

    private void setupNameColumn() {
        TableColumn<BaseDataUserTableData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataUserTable.getColumns().add(nameColumn);
        nameColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<BaseDataUserTableData, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue())
        );
    }

    private void setupPasswordColumn() {
        TableColumn<BaseDataUserTableData, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataUserTable.getColumns().add(passwordColumn);
        passwordColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<BaseDataUserTableData, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPassword(t.getNewValue())
        );
    }

    private void setupIsadminColumn() {
        TableColumn<BaseDataUserTableData, Integer> isadminColumn = new TableColumn<>("Is Admin?");
        isadminColumn.setCellValueFactory(new PropertyValueFactory<>("isadmin"));
        //TODO not working properly
        //isadminColumn.setCellFactory(TextFieldTableCell.forTableColumn();
        baseDataUserTable.getColumns().add(isadminColumn);
        isadminColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<BaseDataUserTableData, Integer> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setIsadmin(t.getNewValue())
        );
    }

    // TODO not working
    @FXML
    private void submitt () {
        String createdUser = fieldCreatedUser.getText();
        String name = fieldName.getText();
        String password = fieldPassword.getText();
        String isAdmin = fieldIsadmin.getText();
        User newUser = new User(createdUser, name, password, Integer.parseInt(isAdmin));
        appDataController.addUserToUserList(newUser);
        tableData.add(new BaseDataUserTableData(newUser));
        try {
            mySQLUserDAO = new MySQLUserDAO();
            mySQLUserDAO.saveUser(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}