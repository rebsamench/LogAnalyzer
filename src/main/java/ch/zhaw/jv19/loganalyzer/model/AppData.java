package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.*;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

/**
 * Provides base data for loganalyzer app. Instance created (singleton) and data loaded from storage on app startup.
 * Access data only from {@link AppDataController}.
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class AppData {
    private ObservableList<User> userList;
    private ObservableList<Site> siteList;
    private ObservableList<Busline> busLineList;
    private ObservableList<EventType> eventTypeList;
    private ObservableList<Source> sourceList;
    private ObservableList<String> panelList;
    private final SimpleStringProperty message;
    private final SimpleBooleanProperty isDatabaseAccessible;
    private static AppData instance;

    //Singleton: AppData can only be instantiated once
    private AppData () {
        eventTypeList = EventType.getEventTypeList();
        sourceList = Source.getSourceList();
        message = new SimpleStringProperty();
        isDatabaseAccessible = new SimpleBooleanProperty();
        initializeAppData();
        // if app is started with invalid db settings and db settings are corrected, base data needs to be reloaded
        isDatabaseAccessible.addListener((obs, oldValue, newValue) -> {
            if(oldValue == false && newValue == true) {
                initializeAppData();
            }
        });
    }

    /**
     * Gets instance of AppData. AppData is a Singleton.
     * @return Singleton instance of AppData
     */
    public static AppData getInstance () {
        if (AppData.instance == null) {
            AppData.instance = new AppData();
        }
        return AppData.instance;
    }

    /**
     * Gets data from responsible DAOs
     */
    private void initializeAppData() {
        UserDAO userDao = new MySQLUserDAO();
        SiteDAO siteDAO = new MySQLSiteDAO();
        BusLineDAO busLineDAO = new MySQLBuslineDAO();
        try {
            this.userList = userDao.getAllUsersList();
            this.siteList = siteDAO.getAllSitesList();
            this.busLineList = busLineDAO.getAllBuslinesList();
        } catch (Exception e) {
            setMessage(e.getMessage());
        }
    }

    public SimpleStringProperty getMessage() {
        return message;
    }

    public SimpleBooleanProperty isDatabaseAccessible() {
        refreshIsDatabaseAccessible();
        return isDatabaseAccessible;
    }

    private void refreshIsDatabaseAccessible() {
        boolean isDBAccessible;
        try {
            DBUtil.getConnection();
            isDBAccessible = true;
        } catch (Exception e) {
            isDBAccessible = false;
        }
        this.isDatabaseAccessible.set(isDBAccessible);
    }

    public void setMessage(String message) {
        this.message.setValue(message);
    }

    public void setUserList(ObservableList<User> userList) {
        this.userList = userList;
    }

    public void setSiteList(ObservableList<Site> siteList) {
        this.siteList = siteList;
    }

    public void setBusLineList(ObservableList<Busline> busLineList) {
        this.busLineList = busLineList;
    }

    public ObservableList<User> getUserList() {
        return userList;
    }

    public ObservableList<Busline> getBusLineList() {
        return busLineList;
    }

    public ObservableList<Site> getSiteList() {
        return siteList;
    }

    public ObservableList<EventType> getEventTypeList() {
        return eventTypeList;
    }

    public ObservableList<Source> getSourceList() {
        return sourceList;
    }

    public ObservableList<String> getPanelList() {
        return panelList;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void addSite(Site site) {
        siteList.add(site);
    }

    public void addBusline(Busline busline) {
        busLineList.add(busline);
    }

    public void fillPanelList(ArrayList<AnchorPane> availablePanelList) {
        panelList = FXCollections.observableArrayList();
        for(AnchorPane panel : availablePanelList) {
            panelList.add(panel.getId());
        }
    }
}
