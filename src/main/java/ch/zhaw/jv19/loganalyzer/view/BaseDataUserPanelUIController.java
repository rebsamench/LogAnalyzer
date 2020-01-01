package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.AppDataController;
import ch.zhaw.jv19.loganalyzer.model.UserWrapper;
import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLUserDAO;
import javafx.beans.binding.BooleanBinding;
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
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BaseDataUserPanelUIController implements Initializable {

    private ObservableList<UserWrapper> tableData = FXCollections.observableArrayList();
    private AppDataController appDataController;
    private MySQLUserDAO mySQLUserDAO;
    private boolean doesNameExist;

    @FXML
    private TextField fieldCreatedUser;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldPassword;

    @FXML
    private TextField fieldIsadmin;

    @FXML
    private Button submit;

    @FXML
    private Button updateData;

    @FXML
    private TableView<UserWrapper> baseDataUserTable;

    @FXML
    private TableColumn<UserWrapper, String> createdUserColumn;

    @FXML
    private TableColumn<UserWrapper, String> nameColumn;

    @FXML
    private TableColumn<UserWrapper, String> passwordColumn;

    @FXML
    private TableColumn<UserWrapper, Integer> isadminColumn;

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
        BooleanBinding booleanBind = fieldCreatedUser.textProperty().isEmpty()
                .or(fieldName.textProperty().isEmpty())
                .or(fieldPassword.textProperty().isEmpty())
                        .or(fieldIsadmin.textProperty().isEmpty());

        submit.disableProperty().bind(booleanBind);
    }

    private void populate() {
        for (User user : appDataController.getUserList()) {
            tableData.add(new UserWrapper(user));
        }
    }

    private void setupCreatedUserColumn() {
        TableColumn<UserWrapper, String> createdUserColumn = new TableColumn<>("Created User");
        createdUserColumn.setCellValueFactory(new PropertyValueFactory<>("createdUser"));
        createdUserColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataUserTable.getColumns().add(createdUserColumn);
        createdUserColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<UserWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setCreatedUser(t.getNewValue())
        );
    }

    private void setupNameColumn() {
        TableColumn<UserWrapper, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        baseDataUserTable.getColumns().add(nameColumn);
    }

    private void setupPasswordColumn() {
        TableColumn<UserWrapper, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataUserTable.getColumns().add(passwordColumn);
        passwordColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<UserWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPassword(t.getNewValue())
        );
    }

    private void setupIsadminColumn() {
        TableColumn<UserWrapper, Integer> isadminColumn = new TableColumn<>("Admin");
        isadminColumn.setCellValueFactory(new PropertyValueFactory<>("isadmin"));
        isadminColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer integer) {
                return Integer.toString(integer);
            }

            @Override
            public Integer fromString(String s) {
                return Integer.parseInt(s);
            }
        }));
        baseDataUserTable.getColumns().add(isadminColumn);
        isadminColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<UserWrapper, Integer> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setIsadmin(t.getNewValue())
        );
    }

    @FXML
    private void submit() {
        String createdUser = fieldCreatedUser.getText();
        String name = fieldName.getText();
        String password = fieldPassword.getText();
        String isAdmin = fieldIsadmin.getText();
        User newUser = new User(createdUser, name, password, Integer.parseInt(isAdmin));
        UserWrapper newWrappedUser = new UserWrapper(newUser);
        checkUserDuplicates(newWrappedUser);
        fieldCreatedUser.clear();
        fieldName.clear();
        fieldPassword.clear();
        fieldIsadmin.clear();
        if (doesNameExist) {
            appDataController.setMessage("User already exists!");
        } else {
            appDataController.addUserToUserList(newUser);
            tableData.add(newWrappedUser);
            try {
                mySQLUserDAO = new MySQLUserDAO();
                mySQLUserDAO.saveUser(newUser);
            } catch (SQLException e) {
                e.printStackTrace();
                appDataController.setMessage("SQL Error");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void updateData() {
        mySQLUserDAO = new MySQLUserDAO();
        try {
            mySQLUserDAO.updateUserData(appDataController.getUserList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkUserDuplicates(UserWrapper newWrappedUser) {
        List listOfNames = new ArrayList();
        for (UserWrapper user : tableData) {
            listOfNames.add(user.getName());
        }
        if (listOfNames.contains(newWrappedUser.getName())) {
            doesNameExist = true;
        } else {
            doesNameExist = false;
        }
    }
}