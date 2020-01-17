package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Handles user interactions on the base data panel.
 * Allows the opening of new users, sites and busLines and changes to existing users, sites and busLines.
 * Users, sites and busLines need to be available prior to the import of new log files.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class BaseDataPanelUIController implements Initializable, UIPanelController {

    private AppDataController appDataController;
    private BaseDataController baseDataController;
    private final ObservableList<UserWrapper> userTableData = FXCollections.observableArrayList();
    private final ObservableList<SiteWrapper> siteTableData = FXCollections.observableArrayList();
    private final ObservableList<BusLineWrapper> busLineTableData = FXCollections.observableArrayList();
    private final ObservableList<String> userNames = FXCollections.observableArrayList();

    @FXML
    private Button buttonSubmitNewUser;
    @FXML
    private ComboBox<User> comboBoxCreatedUserBusLine;
    @FXML
    private ComboBox<User> comboBoxCreatedUserUser;
    @FXML
    private TextField fieldUserName;
    @FXML
    private ComboBox<Integer> comboBoxIsAdmin;
    @FXML
    private TableView<UserWrapper> baseDataUserTable;
    @FXML
    private TableColumn<UserWrapper, String> columnCreatedUserUser;
    @FXML
    private TableColumn<UserWrapper, String> columnNameUser;
    @FXML
    private TableColumn<UserWrapper, String> columnPassword;
    @FXML
    private TableColumn<UserWrapper, Integer> columnIsAdmin;
    // User Tab Elements
    @FXML
    private TextField fieldPassword;
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
    private TableColumn<SiteWrapper, String> columnStreetName;
    @FXML
    private TableColumn<SiteWrapper, String> columnZipCode;
    @FXML
    private TableColumn<SiteWrapper, Integer> columnCity;
    // BusLine Tab Elements
    @FXML
    private Button buttonSubmitNewBusLine;
    // Site Tab Elements
    @FXML
    private ComboBox<User> comboBoxCreatedUserSite;
    @FXML
    private TextField fieldBusLineName;
    @FXML
    private TextField fieldBusType;
    @FXML
    private TableView<BusLineWrapper> baseDataBusLineTable;
    @FXML
    private TableColumn<BusLineWrapper, String> columnCreatedUserBusLine;
    @FXML
    private TableColumn<BusLineWrapper, String> columnBusLineName;
    @FXML
    private TableColumn<BusLineWrapper, String> columnBusType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appDataController = AppDataController.getInstance();
        if (appDataController.isDatabaseAccessible().get()) {
            populate();
            initializeUserTab();
            initializeSiteTab();
            initializeBusLineTab();
        } else {
            appDataController.setMessage("Could not load base data. Check database connection settings in properties file or on panel 'Settings'.");
        }
    }


    /**
     * Builds a lists of wrapped users, sites and busLines in order to populate the editable table views
     * on the panel
     */
    private void populate() {
        for (User user : appDataController.getUserList()) {
            userTableData.add(new UserWrapper(user));
        }
        for (Site site : appDataController.getSiteList()) {
            siteTableData.add(new SiteWrapper(site));
        }
        for (BusLine busLine : appDataController.getBusLineList()) {
            busLineTableData.add(new BusLineWrapper(busLine));
        }
        for (UserWrapper user : userTableData) {
            userNames.add(user.getName());
        }
    }


    /**
     * Initializes the user tab
     */
    private void initializeUserTab() {
        // restrict lengths
        addLengthEventfilter(fieldUserName, 45);
        addLengthEventfilter(fieldPassword, 45);

        baseDataUserTable.setItems(userTableData);
        comboBoxCreatedUserUser.setItems(appDataController.getUserList());
        ObservableList<Integer> admin = FXCollections.observableArrayList();
        admin.add(0);
        admin.add(1);
        comboBoxIsAdmin.setItems(admin);
        setupCreatedUserColumnUser();
        setupUserNameColumn();
        setupPasswordColumn();
        setupIsadminColumn();
        baseDataUserTable.setEditable(true);
        buttonSubmitNewUser.disableProperty().bind(
                comboBoxCreatedUserUser.valueProperty().isNull()
                        .or(fieldUserName.textProperty().isEmpty())
                        .or(fieldPassword.textProperty().isEmpty())
                        .or(comboBoxIsAdmin.valueProperty().isNull()));
    }


    /**
     * Initializes the site tab
     */
    private void initializeSiteTab() {
        // restrict lengths
        addLengthEventfilter(fieldSiteName, 45);
        addLengthEventfilter(fieldStreetName, 45);
        addLengthEventfilter(fieldZipCode, 10);
        addLengthEventfilter(fieldCity, 45);

        baseDataSiteTable.setItems(siteTableData);
        comboBoxCreatedUserSite.setItems(appDataController.getUserList());
        setupCreatedUserColumnSite();
        setupSiteNameColumn();
        setupStreetColumn();
        setupZipCodeColumn();
        setupCityColumn();
        baseDataSiteTable.setEditable(true);
        buttonSubmitNewSite.disableProperty().bind(
                comboBoxCreatedUserSite.valueProperty().isNull()
                        .or(fieldSiteName.textProperty().isEmpty())
                        .or(fieldStreetName.textProperty().isEmpty())
                        .or(fieldZipCode.textProperty().isEmpty())
                        .or(fieldCity.textProperty().isEmpty()));
    }


    /**
     * Initializes the busLine tab
     */
    private void initializeBusLineTab() {
        // restrict lengths
        addLengthEventfilter(fieldBusLineName, 45);
        addLengthEventfilter(fieldBusType, 45);

        baseDataBusLineTable.setItems(busLineTableData);
        comboBoxCreatedUserBusLine.setItems(appDataController.getUserList());
        setupCreatedUserColumnBusLine();
        setupBusLineNameColumn();
        setupBustypeColumn();
        baseDataBusLineTable.setEditable(true);
        buttonSubmitNewBusLine.disableProperty().bind(
                comboBoxCreatedUserBusLine.valueProperty().isNull()
                        .or(fieldBusLineName.textProperty().isEmpty())
                        .or(fieldBusType.textProperty().isEmpty()));
    }

    private void setupCreatedUserColumnUser() {
        TableColumn<UserWrapper, String> columnCreatedUserUser = new TableColumn<>("Created by");
        columnCreatedUserUser.setCellValueFactory(new PropertyValueFactory<>("createdUser"));
        columnCreatedUserUser.setCellFactory(ComboBoxTableCell.forTableColumn(userNames));
        baseDataUserTable.getColumns().add(columnCreatedUserUser);
        columnCreatedUserUser.setOnEditCommit(
                (TableColumn.CellEditEvent<UserWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setCreatedUser(t.getNewValue()));
    }

    private void setupCreatedUserColumnSite() {
        TableColumn<SiteWrapper, String> columnCreatedUserSite = new TableColumn<>("Created by");
        columnCreatedUserSite.setCellValueFactory(new PropertyValueFactory<>("createdUser"));
        baseDataSiteTable.getColumns().add(columnCreatedUserSite);
    }

    private void setupCreatedUserColumnBusLine() {
        TableColumn<BusLineWrapper, String> ColumnCreatedUserBusLine = new TableColumn<>("Created by");
        ColumnCreatedUserBusLine.setCellValueFactory(new PropertyValueFactory<>("createdUser"));
        baseDataBusLineTable.getColumns().add(ColumnCreatedUserBusLine);
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
        isadminColumn.setCellFactory(ComboBoxTableCell.forTableColumn(0, 1));
        baseDataUserTable.getColumns().add(isadminColumn);
        isadminColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<UserWrapper, Integer> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setIsadmin(t.getNewValue()));
    }

    private void setupSiteNameColumn() {
        TableColumn<SiteWrapper, String> columnSiteName = new TableColumn<>("Site Name");
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
        columnZipCode.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
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

    private void setupBusLineNameColumn() {
        TableColumn<BusLineWrapper, String> columnBusLineName = new TableColumn<>("Bus Line Name");
        columnBusLineName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnBusLineName.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataBusLineTable.getColumns().add(columnBusLineName);
        columnBusLineName.setOnEditCommit(
                (TableColumn.CellEditEvent<BusLineWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue())
        );
    }

    private void setupBustypeColumn() {
        TableColumn<BusLineWrapper, String> columnBusType = new TableColumn<>("Bus Type");
        columnBusType.setCellValueFactory(new PropertyValueFactory<>("busType"));
        columnBusType.setCellFactory(TextFieldTableCell.forTableColumn());
        baseDataBusLineTable.getColumns().add(columnBusType);
        columnBusType.setOnEditCommit(
                (TableColumn.CellEditEvent<BusLineWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setBusType(t.getNewValue())
        );
    }

    /**
     * Collects user data from the user tab and hands it over to the BaseDataController in order to
     * create a new user.
     */
    @FXML
    private void submitNewUser() {
        User createdUser = comboBoxCreatedUserUser.getSelectionModel().getSelectedItem();
        String cu = createdUser.getName();
        String name = fieldUserName.getText();
        String password = fieldPassword.getText();
        int isAdmin = comboBoxIsAdmin.getSelectionModel().getSelectedItem();
        User newUser = new User(cu, name, password, isAdmin);
        UserWrapper newWrappedUser = new UserWrapper(newUser);
        fieldUserName.clear();
        fieldPassword.clear();
        if (appDataController.getUserList().contains(newUser)) {
            appDataController.setMessage("User already exists!");
        } else {
            userTableData.add(newWrappedUser);
            try {
                baseDataController = new BaseDataController();
                baseDataController.saveNewUser(newUser);
                appDataController.setMessage("New User successfully submitted");
            } catch (Exception e) {
                e.printStackTrace();
                appDataController.setMessage("SQL Error");
            }
        }
    }

    /**
     * Collects site data from the site tab and hands it over to the BaseDataController in order to
     * create a new site.
     */
    @FXML
    private void submitNewSite() {
        User createdUser = comboBoxCreatedUserSite.getSelectionModel().getSelectedItem();
        String cu = createdUser.getName();
        String name = fieldSiteName.getText();
        String street = fieldStreetName.getText();
        String zipCode = fieldZipCode.getText();
        String city = fieldCity.getText();
        Site newSite = new Site(cu, name, street, zipCode, city);
        SiteWrapper newWrappedSite = new SiteWrapper(newSite);
        fieldSiteName.clear();
        fieldStreetName.clear();
        fieldZipCode.clear();
        fieldCity.clear();
        if (appDataController.getSiteList().contains(newSite)) {
            appDataController.setMessage("Site already exists!");
        } else {
            siteTableData.add(newWrappedSite);
            try {
                baseDataController = new BaseDataController();
                baseDataController.saveNewSite(newSite);
                appDataController.setMessage("New Site successfully submitted");
            } catch (Exception e) {
                e.printStackTrace();
                appDataController.setMessage("SQL Error");
            }
        }
    }

    /**
     * Collects busLine data from the busLine tab and hands it over to the BaseDataController in order to
     * create a new busLine.
     */
    @FXML
    private void submitNewBusLine() {
        User createdUser = comboBoxCreatedUserBusLine.getSelectionModel().getSelectedItem();
        String cu = createdUser.getName();
        String name = fieldBusLineName.getText();
        String busType = fieldBusType.getText();
        BusLine newBusLine = new BusLine(cu, name, busType);
        BusLineWrapper newWrappedBusLine = new BusLineWrapper(newBusLine);
        fieldBusLineName.clear();
        fieldBusType.clear();
        if (appDataController.getBusLineList().contains(newBusLine)) {
            appDataController.setMessage("Bus line already exists!");
        } else {
            busLineTableData.add(newWrappedBusLine);
            try {
                baseDataController = new BaseDataController();
                baseDataController.saveNewBusLine(newBusLine);
                appDataController.setMessage("New bus line successfully submitted");
            } catch (Exception e) {
                e.printStackTrace();
                appDataController.setMessage("SQL Error");
            }
        }
    }

    /**
     * Collects updated user data from UI and calls BaseDataController in order to update the data base.
     */
    @FXML
    private void updateUser() {
        baseDataController = new BaseDataController();
        try {
            baseDataController.updateUserData();
            appDataController.setMessage("User Data successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
            appDataController.setMessage("SQL Error");
        }
    }

    /**
     * Collects updated Site data from UI and calls BaseDataController in order to update the data base.
     */
    @FXML
    private void updateSite() {
        baseDataController = new BaseDataController();
        try {
            baseDataController.updateSiteData();
            appDataController.setMessage("Site Data successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
            appDataController.setMessage("SQL Error");
        }
    }

    /**
     * Collects updated buslin data from UI and calls BaseDataController in order to update the data base.
     */
    @FXML
    private void updateBusLine() {
        baseDataController = new BaseDataController();
        try {
            baseDataController.updateBusLineData();
            appDataController.setMessage("BusLine Data successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
            appDataController.setMessage("SQL Error");
        }
    }

    @Override
    public boolean isAdminPanel() {
        return false;
    }

    /**
     * Restricts input values on to sepcific length
     *
     * @param textField TextField to be restricted in length
     * @param length    max length of field
     */
    private void addLengthEventfilter(TextField textField, int length) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (textField.getText().length() > length) {
                keyEvent.consume();
            }
        });
    }
}
