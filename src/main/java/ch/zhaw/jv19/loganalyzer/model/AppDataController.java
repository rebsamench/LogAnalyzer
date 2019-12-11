package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.LogRecordReadDAO;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLLogRecordReportDAO;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLUserDAO;
import ch.zhaw.jv19.loganalyzer.model.dao.UserDAO;
import ch.zhaw.jv19.loganalyzer.util.export.ExcelExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

public class AppDataController {
    private AppData appData;
    private static AppDataController instance;

    //Singleton: AppDataController can only be instantiated once
    private AppDataController () {
        appData = AppData.getInstance();
        initializeAppData();
    }

    public static AppDataController getInstance () {
        if (AppDataController.instance == null) {
            AppDataController.instance = new AppDataController();
        }
        return AppDataController.instance;
    }

    /**
     * Sets current global message on AppData.
     * @param message
     */
    public void setMessage(String message) {
        appData.setMessage(message);
    }

    /**
     * Returns message as String Property. UI Elements can be bound to this method
     * in order to observe global messages.
     * @return
     */
    public SimpleStringProperty getMessage() {
        return appData.getMessage();
    }

    /**
     * Gets data from responsible DAOs and hands it to AppData
     */
    public void initializeAppData() {
        UserDAO userDao = new MySQLUserDAO();
        try {
            appData.setUserList(userDao.getAllUsersList());
        } catch (SQLException e) {
            setMessage(e.getMessage());
        }
    }

    /**
     * Gets all users in an Observable List
     * @return ObservableList of Users
     */
    public ObservableList<User> getUserList() {
        return appData.getUserList();
    }

    /**
     * Gets all sites in an Observable List
     * @return ObservableList of Sites
     */
    public ObservableList<Site> getSiteList() {
        return appData.getSiteList();
    }

    /**
     * Gets log records from responsible DAO as table view.
     * @param searchConditions: HashMap(columnName, conditionValue): conditionValues can either be simple Strings,
     * ZonedDateTime-Objects or ArrayLists of Strings for IN conditions etc. For Details see
     * methods in DAO
     * @return TableView with log records that met search conditions
     */
    public TableView<ObservableList> getLogRecordsTableByConditions(HashMap<String, Object> searchConditions) {
        LogRecordReadDAO logRecordReadDAO = new MySQLLogRecordReportDAO(this);
        TableView<ObservableList> resultTable = logRecordReadDAO.getLogRecordsTableByConditions(searchConditions);
        setMessage(logRecordReadDAO.getCurrentQuery());
        return resultTable;
    }

    /**
     * Exports TableView as Excel file
     */
    public void exportToExcel(TableView<ObservableList> table, File file) {
        ExcelExporter exporter = new ExcelExporter();
        exporter.saveTable(table, file);
    }

    /**
     * Gets user from AppData by its user id
     * @param id user id
     * @return User which matches given id
     */
    public User getUserById(int id) {
        return (User) getUserList().stream().filter(user -> (user.getId() == id));
    }

    /**
     * Gets site from AppData by its user id
     * @param id site id
     * @return Site which matches given id
     */
    public Site getSiteById(int id) {
        return (Site) getSiteList().stream().filter(site -> (site.getId() == id));
    }
}
