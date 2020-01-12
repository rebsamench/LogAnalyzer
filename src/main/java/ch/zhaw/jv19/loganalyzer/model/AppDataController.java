package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.*;
import ch.zhaw.jv19.loganalyzer.util.export.ExcelExporter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

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
    public ObservableList<BusLine> getBusLineList() {
        return appData.getBusLineList();
    }

    /**
     * Gets a list of record types defined in enum as strings.
     * @return ObservableList of record types
     */
    public ObservableList<EventType> getEventTypeList() {
        return appData.getEventTypeList();
    }

    /**
     * Gets a list of sources defined in enum as strings.
     * @return ObservableList of sources
     */
    public ObservableList<Source> getSourceList() {
        return appData.getSourceList();
    }

    /**
     * Gets log records from responsible DAO as array list.
     * @param searchConditions: TreeMap(columnName, conditionValue): conditionValues can either be simple Strings,
     *                          ZonedDateTime-Objects or ArrayLists of Strings for IN conditions etc. For Details see
     *                          methods in DAO
     * @return ArrayList of log records that meet search conditions
     */
    public ArrayList<LogRecord> getLogRecordsListByConditions(TreeMap<String, Object> searchConditions) {
        LogRecordReadDAO logRecordReadDAO = new MySQLLogRecordReadDAO();
        ArrayList<LogRecord> resultList = logRecordReadDAO.getLogRecordsListByConditions(searchConditions);
        setMessage(logRecordReadDAO.getCurrentQuery());
        return resultList;
    }

    /**
     * Exports TableView as Excel file
     */
    public void exportToExcel (TableView<?> table, File file) {
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
     * Gets busLine from AppData by its id
     *
     * @param id bus line id
     * @return Bus line which matches given id
     */
    public BusLine getBusLineById(int id) {
        return getBusLineList().stream()
                .filter(busLine -> (busLine.getId() == id))
                .findAny()
                .orElse(null);
    }

    /**
     * Adds given user to user list.
     * @param user to be added
     */
    public void addUserToUserList(User user) {
        appData.addUser(user);
    }

    /**
     * Adds given site to site list.
     * @param site to be added
     */
    public void addSiteToSiteList(Site site) {
        appData.addSite(site);
    }

    /**
     * Adds given bus line to bus line list.
     * @param busLine to be added
     */
    public void addBusLineToBusLineList(BusLine busLine) {
        appData.addBusLine(busLine);
    }

    /**
     * Fills panel list with available UI panels
     * @param panelList list of UI panels (AnchorPane)
     */
    public void fillPanelList(ArrayList<AnchorPane> panelList) {
        appData.fillPanelList(panelList);
    }

    /**
     * Gets list of available UI panels.
     * @return ObservableList of UI panels (String)
     */
    public ObservableList<String> getPanelList() {
        return appData.getPanelList();
    }

    /**
     * Checks if database can be connected.
     * @return SimpleBooleanProperty with refresehed connection state
     */
    public SimpleBooleanProperty isDatabaseAccessible() {
        return appData.isDatabaseAccessible();
    }

}
