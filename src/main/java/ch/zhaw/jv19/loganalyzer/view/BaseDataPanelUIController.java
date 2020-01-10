package ch.zhaw.jv19.loganalyzer.view;

import ch.zhaw.jv19.loganalyzer.model.*;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLBuslineDAO;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLSiteDAO;
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

/**
 * Handles user interactions on the base data panel.
 * Allows the opening of new users, sites and buslines and changes to existing users, sites and buslines.
 * Users, sites and buslines need to be available prior to the import of new log files.
 *
 * @author: Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class BaseDataPanelUIController implements Initializable {

    private ObservableList<UserWrapper> userTableData = FXCollections.observableArrayList();
    private AppDataController appDataController;
    private MySQLUserDAO mySQLUserDAO;
    private BaseDataController baseDataController;
    @FXML
    Button buttonSubmitNewUser;
    @FXML
    ComboBox comboBoxCreatedUserBusline;
    private boolean doesNameExist;
    private ObservableList<SiteWrapper> siteTableData = FXCollections.observableArrayList();
    private ObservableList<BuslineWrapper> buslineTableData = FXCollections.observableArrayList();
    private MySQLSiteDAO mySQLSiteDAO;
    private MySQLBuslineDAO mySQLBuslineDAO;
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
    private TableColumn<SiteWrapper, String> ColumnStreetname;
    @FXML
    private TableColumn<SiteWrapper, String> columnZipCode;
    @FXML
    private TableColumn<SiteWrapper, Integer> columnCity;
    // Busline Tab Elements
    @FXML
    private Button buttonSubmitNewBusline;
    // Site Tab Elements
    @FXML
    private ComboBox comboBoxCreatedUserSite;
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
        if (appDataController.isDatabaseAccessible().get() == true) {
            populate();
            initializeUserUserTab();
            initializeSiteTab();
            initializeBuslineTab();
        } else {
            appDataController.setMessage("Could not load base data. Check database connection settings in properties file or on panel 'Settings'.");
        }
    }


    /**
     * Builds a lists of wrapped users, sites and buslines in order to populate the editable table views
     * on the panel
     */
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


    /**
     * Initializes the user tab
     */
    private void initializeUserUserTab() {
        baseDataUserTable.setItems(userTableData);
        comboBoxCreatedUserUser.getItems().addAll(appDataController.getUserList());
        List<Integer> admin = new ArrayList();
        admin.add(0);
        admin.add(1);
        comboBoxIsadmin.getItems().addAll(admin);
        setupCreatedUserColumnUser();
        setupUserNameColumn();
        setupPasswordColumn();
        setupIsadminColumn();
        baseDataUserTable.setEditable(true);
        buttonSubmitNewUser.disableProperty().bind((fieldUserName.textProperty().isEmpty())
                .or(fieldPassword.textProperty().isEmpty())
                .or(comboBoxIsadmin.valueProperty().isNull()));
    }


    /**
     * Initializes the site tab
     */
    private void initializeSiteTab() {
        baseDataSiteTable.setItems(siteTableData);
        comboBoxCreatedUserSite.getItems().addAll(appDataController.getUserList());
        setupCreatedUserColumnSite();
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


    /**
     * Initializes the busline tab
     */
    private void initializeBuslineTab() {
        baseDataBuslineTable.setItems(buslineTableData);
        comboBoxCreatedUserBusline.getItems().addAll(appDataController.getUserList());
        setupCreatedUserColumnBusline();
        setupBuslineNameColumn();
        setupBustypeColumn();
        baseDataBuslineTable.setEditable(true);
        buttonSubmitNewBusline.disableProperty().bind(
                (fieldBuslineName.textProperty().isEmpty())
                        .or(fieldBusType.textProperty().isEmpty()));
    }

    // Setup Columns
    private void setupCreatedUserColumnUser() {
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

    private void setupCreatedUserColumnSite() {
        TableColumn<SiteWrapper, String> columnCreatedUserSite = new TableColumn<>("Created User");
        columnCreatedUserSite.setCellValueFactory(new PropertyValueFactory<>("createdUser"));
        baseDataSiteTable.getColumns().add(columnCreatedUserSite);
    }

    private void setupCreatedUserColumnBusline() {
        TableColumn<BuslineWrapper, String> ColumnCreatedUserBusline = new TableColumn<>("Created User");
        ColumnCreatedUserBusline.setCellValueFactory(new PropertyValueFactory<>("createdUser"));
        baseDataBuslineTable.getColumns().add(ColumnCreatedUserBusline);
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
        baseDataBuslineTable.getColumns().add(columnBusType);
        columnBusType.setOnEditCommit(
                (TableColumn.CellEditEvent<BuslineWrapper, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setBustype(t.getNewValue())
        );
    }

    /**
     * Collects user data from the user tab and hands it over to the corresponding DAO in order to
     * create a new user.
     */
    @FXML
    private void submitNewUser() {
        User createdUser = (User) comboBoxCreatedUserUser.getSelectionModel().getSelectedItem();
        String cu = createdUser.getName();
        String name = fieldUserName.getText();
        String password = fieldPassword.getText();
        int isAdmin = (int) comboBoxIsadmin.getSelectionModel().getSelectedItem();
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
     * Collects site data from the site tab and hands it over to the corresponding DAO in order to
     * create a new site.
     */
    @FXML
    private void submitNewSite() {
        User createdUser = (User) comboBoxCreatedUserSite.getSelectionModel().getSelectedItem();
        String cu = createdUser.getName();
        String name = fieldSiteName.getText();
        String street = fieldStreetName.getText();
        String zipCode = fieldZipCode.getText();
        String city = fieldCity.getText();
        Site newSite = new Site(cu, name, street, zipCode, city);
        SiteWrapper newWrappedSite = new SiteWrapper(newSite);
        checkSiteDuplicates(newSite);
        fieldSiteName.clear();
        fieldStreetName.clear();
        fieldZipCode.clear();
        fieldCity.clear();
        if (doesNameExist) {
            appDataController.setMessage("Site already exists!");
        } else {
            appDataController.addSiteToSiteList(newSite);
            siteTableData.add(newWrappedSite);
            try {
                mySQLSiteDAO = new MySQLSiteDAO();
                mySQLSiteDAO.saveSite(newSite);
                appDataController.setMessage("New Site successfully submitted");
            } catch (Exception e) {
                e.printStackTrace();
                appDataController.setMessage("SQL Error");
            }
        }
    }

    /**
     * Collects busline data from the busline tab and hands it over to the corresponding DAO in order to
     * create a new busline.
     */
    @FXML
    private void submitNewBusline() {
        User createdUser = (User) comboBoxCreatedUserBusline.getSelectionModel().getSelectedItem();
        String cu = createdUser.getName();
        String name = fieldBuslineName.getText();
        String bustyp = fieldBusType.getText();
        Busline newBusline = new Busline(cu, name, bustyp);
        BuslineWrapper newWrappedBusline = new BuslineWrapper(newBusline);
        checkBuslineDuplicates(newBusline);
        fieldBuslineName.clear();
        fieldBusType.clear();
        if (doesNameExist) {
            appDataController.setMessage("Site already exists!");
        } else {
            appDataController.addBuslineToBuslineList(newBusline);
            buslineTableData.add(newWrappedBusline);
            try {
                mySQLBuslineDAO = new MySQLBuslineDAO();
                mySQLBuslineDAO.saveBusline(newBusline);
                appDataController.setMessage("New Busline successfully submitted");
            } catch (Exception e) {
                e.printStackTrace();
                appDataController.setMessage("SQL Error");
            }
        }
    }

    /**
     * Collects updated user data and hands it over to the corresponding DAO in order to update data in
     * the data base.
     */
    @FXML
    private void updateUserName() {
        mySQLUserDAO = new MySQLUserDAO();
        try {
            mySQLUserDAO.updateUserData(appDataController.getUserList());
            appDataController.setMessage("User Data successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
            appDataController.setMessage("SQL Error");
        }
    }

    /**
     * Collects updated site data and hands it over to the corresponding DAO in order to update data in
     * the data base.
     */
    @FXML
    private void updateSite() {
        mySQLSiteDAO = new MySQLSiteDAO();
        try {
            mySQLSiteDAO.updateSiteData(appDataController.getSiteList());
            appDataController.setMessage("Site Data successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
            appDataController.setMessage("SQL Error");
        }
    }

    /**
     * Collects updated busline data and hands it over to the corresponding DAO in order to update data in
     * the data base.
     */
    @FXML
    private void updateBusline() {
        mySQLBuslineDAO = new MySQLBuslineDAO();
        try {
            mySQLBuslineDAO.updateBuslineData(appDataController.getBuslineList());
            appDataController.setMessage("Busline Data successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
            appDataController.setMessage("SQL Error");
        }
    }

    /**
     * Checks if a user already exists.
     *
     * @param newUser : Wrapped user
     */
    private void checkUserDuplicates(User newUser) {
        if (appDataController.getUserList().contains(newUser)) {
            doesNameExist = true;
        } else {
            doesNameExist = false;
        }
    }

    /**
     * Checks if a site already exists.
     *
     * @param newSite : Wrapped site
     */
    private void checkSiteDuplicates(Site newSite) {
        if (appDataController.getSiteList().contains(newSite)) {
            doesNameExist = true;
        } else {
            doesNameExist = false;
        }
    }

    /**
     * Checks if a busline already exists.
     *
     * @param newBusline : Wrapped busline
     */
    private void checkBuslineDuplicates(Busline newBusline) {
        if (appDataController.getBuslineList().contains(newBusline)) {
            doesNameExist = true;
        } else {
            doesNameExist = false;
        }
    }
}
