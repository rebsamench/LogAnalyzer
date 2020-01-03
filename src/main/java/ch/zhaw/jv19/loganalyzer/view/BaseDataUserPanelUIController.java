package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLUserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BaseDataUserPanelUIController implements Initializable {

    @FXML
    Button buttonSubmitNewUser;
    // Busline Tab Elements
    @FXML
    ComboBox comboBoxCreatedUserBusline;
    private ObservableList<UserWrapper> userTableData = FXCollections.observableArrayList();
    private AppDataController appDataController;
    private MySQLUserDAO mySQLUserDAO;
    private boolean doesNameExist;
    private ObservableList<SiteWrapper> siteTableData = FXCollections.observableArrayList();
    private ObservableList<BuslineWrapper> buslineTableData = FXCollections.observableArrayList();

    @FXML
    private TextField fieldPassword;
    // User Tab Elements
    @FXML
    private ComboBox comboBoxCreatedUserUser;
    @FXML
    private TextField fieldUserName;
    @FXML
    private ComboBox comboBoxIsadmin;
    @FXML
    private TableView<UserWrapper> baseDataUserTable;
    @FXML
    private TableColumn<UserWrapper, String> columCreatedUserUser;
    @FXML
    private TableColumn<UserWrapper, String> columnNameUser;
    @FXML
    private TableColumn<UserWrapper, String> columnPassword;
    @FXML
    private TableColumn<UserWrapper, Integer> columnIsadmin;
    // Site Tab Elements
    @FXML
    private ComboBox ComboBoxCreatedUserSite;
    @FXML
    private Button buttonSubmitNewSite;
    @FXML
    private TextField fieldSiteName;
    @FXML
    private TextField fieldStreetName;
    @FXML
    private TextField fieldZipCode;
    @FXML
    private TextField fieldCity;
    @FXML
    private TableView<SiteWrapper> baseDataSiteTable;
    @FXML
    private TableColumn<SiteWrapper, String> columnCreatedUserSite;
    @FXML
    private TableColumn<SiteWrapper, String> columnSiteName;
    @FXML
    private TableColumn<SiteWrapper, String> ColumnStreetname;
    @FXML
    private TableColumn<SiteWrapper, String> columnZipCode;
    @FXML
    private TableColumn<SiteWrapper, Integer> columnCity;
    @FXML
    private Button buttonSubmitNewBusline;

    @FXML
    private TextField fieldBuslineName;

    @FXML
    private TextField fieldBusType;

    @FXML
    private TableView<BuslineWrapper> baseDataBuslineTable;

    @FXML
    private TableColumn<BuslineWrapper, String> ColumnCreatedUserBusline;

    @FXML
    private TableColumn<BuslineWrapper, String> columnBuslineName;

    @FXML
    private TableColumn<BuslineWrapper, String> columnBusType;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        populate();
        initializeUserUserTab();
        initializeSiteTab();
        initializeBuslineTab();
    }

    private void populate() {
        for (User user : appDataController.getUserList()) {
            userTableData.add(new UserWrapper(user));
        }
        for (Site site : appDataController.getSiteList()) {
            siteTableData.add(new SiteWrapper(site));
        }
        for (Busline busline : appDataController.getBuslineList()) {
            buslineTableData.add(new BuslineWrapper(busline));
        }
    }

    private void initializeUserUserTab() {
        baseDataUserTable.setItems(userTableData);
        comboBoxCreatedUserUser.getItems().addAll(appDataController.getUserList());
        List<Integer> admin = new ArrayList();
        admin.add(0);
        admin.add(1);
        comboBoxIsadmin.getItems().addAll(admin);
        setupCreatedUserColumn();
        setupUserNameColumn();
        setupPasswordColumn();
        setupIsadminColumn();
        baseDataUserTable.setEditable(true);
        buttonSubmitNewUser.disableProperty().bind((fieldUserName.textProperty().isEmpty())
                .or(fieldPassword.textProperty().isEmpty())
                .or(comboBoxIsadmin.valueProperty().isNull()));
    }

    private void initializeSiteTab() {
        baseDataSiteTable.setItems(siteTableData);
        ComboBoxCreatedUserSite.getItems().addAll(appDataController.getSiteList());
        setupCreatedUserColumn();
        setupSiteNameColumn();
        setupStreetColumn();
        setupZipCodeColumn();
        setupCityColumn();
        baseDataSiteTable.setEditable(true);
        buttonSubmitNewSite.disableProperty().bind(
                (fieldSiteName.textProperty().isEmpty())
                        .or(fieldStreetName.textProperty().isEmpty())
                        .or(fieldZipCode.textProperty().isEmpty())
                        .or(fieldCity.textProperty().isEmpty()));
    }

    private void initializeBuslineTab() {
        baseDataBuslineTable.setItems(buslineTableData);
        comboBoxCreatedUserBusline.getItems().addAll(appDataController.getBuslineList());
        setupCreatedUserColumn();
        setupBuslineNameColumn();
        setupBustypeColumn();
        baseDataBuslineTable.setEditable(true);
        buttonSubmitNewBusline.disableProperty().bind(
                (fieldBuslineName.textProperty().isEmpty())
                        .or(fieldBusType.textProperty().isEmpty()));
    }

    // Setup Columns
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

    private void setupUserNameColumn() {
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

    private void setupSiteNameColumn() {
        TableColumn<SiteWrapper, String> columnSiteName = new TableColumn<>("Site Namen");
        columnSiteName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSiteName.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataSiteTable.getColumns().add(columnSiteName);
        columnSiteName.setOnEditCommit(
                (TableColumn.CellEditEvent<SiteWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue())
        );
    }

    private void setupStreetColumn() {
        TableColumn<SiteWrapper, String> columnStreetName = new TableColumn<>("Street");
        columnStreetName.setCellValueFactory(new PropertyValueFactory<>("street"));
        columnStreetName.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataSiteTable.getColumns().add(columnStreetName);
        columnStreetName.setOnEditCommit(
                (TableColumn.CellEditEvent<SiteWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setStreet(t.getNewValue())
        );
    }

    private void setupZipCodeColumn() {
        TableColumn<SiteWrapper, String> columnZipCode = new TableColumn<>("ZIP Code");
        columnZipCode.setCellValueFactory(new PropertyValueFactory<>("zipcode"));
        columnZipCode.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataSiteTable.getColumns().add(columnZipCode);
        columnZipCode.setOnEditCommit(
                (TableColumn.CellEditEvent<SiteWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setZipCode(t.getNewValue())
        );
    }

    private void setupCityColumn() {
        TableColumn<SiteWrapper, String> columnCity = new TableColumn<>("City");
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        columnCity.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataSiteTable.getColumns().add(columnCity);
        columnCity.setOnEditCommit(
                (TableColumn.CellEditEvent<SiteWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setCity(t.getNewValue())
        );
    }

    private void setupBuslineNameColumn() {
        TableColumn<BuslineWrapper, String> columnBuslineName = new TableColumn<>("Busline Name");
        columnBuslineName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnBuslineName.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataBuslineTable.getColumns().add(columnBuslineName);
        columnBuslineName.setOnEditCommit(
                (TableColumn.CellEditEvent<BuslineWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue())
        );
    }

    private void setupBustypeColumn() {
        TableColumn<BuslineWrapper, String> columnBusType = new TableColumn<>("Bustyp");
        columnBusType.setCellValueFactory(new PropertyValueFactory<>("bustype"));
        columnBusType.setCellFactory(TextFieldTableCell.forTableColumn());
        columnBusType.getColumns().add(columnBusType);
        columnBusType.setOnEditCommit(
                (TableColumn.CellEditEvent<BuslineWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setBustype(t.getNewValue())
        );
    }

    @FXML
    private void submitNewUser() {
        User createdUser = (User) comboBoxCreatedUserUser.getSelectionModel().getSelectedItem();
        String cu = createdUser.getName();
        String name = fieldUserName.getText();
        String password = fieldPassword.getText();
        int isAdmin = (int) comboBoxIsadmin.getSelectionModel().getSelectedItem();
        User newUser = new User(cu, name, password, isAdmin);
        UserWrapper newWrappedUser = new UserWrapper(newUser);
        checkUserDuplicates(newWrappedUser);
        fieldUserName.clear();
        fieldPassword.clear();
        if (doesNameExist) {
            appDataController.setMessage("User already exists!");
        } else {
            appDataController.addUserToUserList(newUser);
            userTableData.add(newWrappedUser);
            try {
                mySQLUserDAO = new MySQLUserDAO();
                mySQLUserDAO.saveUser(newUser);
                appDataController.setMessage("New User successfully submitted");
            } catch (Exception e) {
                e.printStackTrace();
                appDataController.setMessage("SQL Error");
            }
        }
    }

    @FXML
    private void submitNewSite() {
    }

    @FXML
    private void submitNewBusline() {
    }

    @FXML
    private void updateUserName() {
        mySQLUserDAO = new MySQLUserDAO();
        try {
            mySQLUserDAO.updateUserData(appDataController.getUserList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateSite() {
    }

    @FXML
    private void updateBusline() {
    }

    private void checkUserDuplicates(UserWrapper newWrappedUser) {
        List listOfNames = new ArrayList();
        for (UserWrapper user : userTableData) {
            listOfNames.add(user.getName());
        }
        if (listOfNames.contains(newWrappedUser.getName())) {
            doesNameExist = true;
        } else {
            doesNameExist = false;
        }
    }
}
