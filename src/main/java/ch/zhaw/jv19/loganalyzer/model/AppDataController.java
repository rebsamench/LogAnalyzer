package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.MainApp;
import ch.zhaw.jv19.loganalyzer.model.dao.*;
import ch.zhaw.jv19.loganalyzer.util.export.ExcelExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides access to base data for loganalyzer app from UI. Every UI panel has access to AppDataController
 * {@link ch.zhaw.jv19.loganalyzer.view.MainAppUIController}
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class AppDataController {
    private final AppData appData;
    private static AppDataController instance;

    //Singleton: AppDataController can only be instantiated once
    private AppDataController() {
        appData = AppData.getInstance();
        initializeAppData();
    }

    /**
     * Gets instance of AppDataController. AppDataController is a Singleton.
     * @return Singleton instance of AppDataController
     */
    public static AppDataController getInstance() {
        if (AppDataController.instance == null) {
            AppDataController.instance = new AppDataController();
        }
        return AppDataController.instance;
    }

    /**
     * Sets current global message on AppData.
     * @param message String message to be set
     */
    public void setMessage(String message) {
        appData.setMessage(message);
    }

    /**
     * Returns message as String Property. UI Elements can be bound to this method
     * in order to observe global messages.
     * @return message as property
     */
    public SimpleStringProperty getMessage() {
        return appData.getMessage();
    }

    /**
     * Gets data from responsible DAOs and hands it to AppData
     */
    public void initializeAppData() {
        UserDAO userDao = new MySQLUserDAO();
        SiteDAO siteDAO = new MySQLSiteDAO();
        BusLineDAO busLineDAO = new MySQLBuslineDAO();
        try {
            appData.setUserList(userDao.getAllUsersList());
            appData.setSiteList(siteDAO.getAllSitesList());
            appData.setBusLineList(busLineDAO.getAllBuslinesList());
        } catch (Exception e) {
            setMessage(e.getMessage());
        }
    }

    /**
     * Gets a list of users.
     * @return ObservableList of users
     */
    public ObservableList<User> getUserList() {
        return appData.getUserList();
    }

    /**
     * Gets a list of sites.
     * @return ObservableList of sites
     */
    public ObservableList<Site> getSiteList() {
        return appData.getSiteList();
    }

    /**
     * Gets a list of bus lines.
     * @return ObservableList of bus lines
     */
    public ObservableList<Busline> getBuslineList() {
        return appData.getBusLineList();
    }

    /**
     * Gets a list of record types defined in enum as strings.
     * @return ObservableList of record types
     */
    public ObservableList<String> getRecordTypeList() {
        return appData.getRecordTypeList();
    }

    /**
     * Gets log records from responsible DAO as array list.
     * @param searchConditions: HashMap(columnName, conditionValue): conditionValues can either be simple Strings,
     *                          ZonedDateTime-Objects or ArrayLists of Strings for IN conditions etc. For Details see
     *                          methods in DAO
     * @return ArrayList of log records that meet search conditions
     */
    public ArrayList<LogRecord> getLogRecordsListByConditions(HashMap<String, Object> searchConditions) {
        LogRecordReadDAO logRecordReadDAO = new MySQLLogRecordReportDAO(this);
        ArrayList<LogRecord> resultList = logRecordReadDAO.getLogRecordsListByConditions(searchConditions);
        setMessage(logRecordReadDAO.getCurrentQuery());
        return resultList;
    }

    /**
     * Exports TableView as Excel file
     */
    public void exportToExcel(TableView<?> table, File file) {
        ExcelExporter exporter = new ExcelExporter();
        exporter.saveTable(table, file);
    }

    /**
     * Gets user from AppData by its id
     *
     * @param name unique user name
     * @return User which matches given id
     */
    public User getUserByName(String name) {
        return getUserList().stream()
                .filter(user -> (user.getName().equals(name)))
                .findAny()
                .orElse(null);
    }

    /**
     * Gets site from AppData by its id
     *
     * @param id site id
     * @return Site which matches given id
     */
    public Site getSiteById(int id) {
        return getSiteList().stream()
                .filter(site -> (site.getId() == id))
                .findAny()
                .orElse(null);
    }

    /**
     * Gets busline from AppData by its id
     *
     * @param id bus line id
     * @return Bus line which matches given id
     */
    public Busline getBuslineById(int id) {
        return getBuslineList().stream()
                .filter(busline -> (busline.getId() == id))
                .findAny()
                .orElse(null);
    }

    public void addUserToUserList(User user) {
        appData.addUser(user);
    }
}
