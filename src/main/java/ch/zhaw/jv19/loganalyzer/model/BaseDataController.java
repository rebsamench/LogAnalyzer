package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.*;
import javafx.fxml.FXML;

import java.sql.SQLException;

/**
 * This class acts as a interface between the BaseDataUIController and the corresponding DAOs.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class BaseDataController {

    public BaseDataController() {
        appDataController = AppDataController.getInstance();
    }

    private final AppDataController appDataController;
    private UserDAO mySQLUserDAO;
    private SiteDAO mySQLSiteDAO;
    private BusLineDAO mySQLBusLineDAO;

    /**
     * Receives a new user from BaseDataPanelUIController and hands it over to the corresponding DAO in order to
     * create a new user.
     */
    @FXML
    public void saveNewUser(User user) throws Exception {
        appDataController.addUserToUserList(user);
        mySQLUserDAO = new MySQLUserDAO();
        mySQLUserDAO.saveUser(user);
        appDataController.setMessage("New User successfully submitted");
    }

    /**
     * Receives a new site from BaseDataPanelUIController and hands it over to the corresponding DAO in order to
     * create a new site.
     */
    @FXML
    public void saveNewSite(Site site) throws Exception {
        appDataController.addSiteToSiteList(site);
        mySQLSiteDAO = new MySQLSiteDAO();
        mySQLSiteDAO.saveSite(site);
        appDataController.setMessage("New Site successfully submitted");
    }

    /**
     * Receives a new busLine from BaseDataPanelUIController and hands it over to the corresponding DAO in order to
     * create a new busLine.
     */
    @FXML
    public void saveNewBusLine(BusLine busLine) throws Exception {
        appDataController.addBusLineToBusLineList(busLine);
        mySQLBusLineDAO = new MySQLBusLineDAO();
        mySQLBusLineDAO.saveBusLine(busLine);
        appDataController.setMessage("New bus line successfully submitted.");
    }

    /**
     * Is called by the BaseDataPanelUIController and will hand over the updated userList to the corresponding
     * DAO in Order to update the data base.
     */
    @FXML
    public void updateUserData() throws SQLException {
        mySQLUserDAO = new MySQLUserDAO();
        mySQLUserDAO.updateUserData(appDataController.getUserList());
    }

    /**
     * Is called by the BaseDataPanelUIController and will hand over the updated siteList to the corresponding
     * DAO in Order to update the data base.
     */
    @FXML
    public void updateSiteData() throws SQLException {
        mySQLSiteDAO = new MySQLSiteDAO();
        mySQLSiteDAO.updateSiteData(appDataController.getSiteList());
    }

    /**
     * Is called by the BaseDataPanelUIController and will hand over the updated busLineList to the corresponding
     * DAO in Order to update the data base.
     */
    @FXML
    public void updateBusLineData() throws SQLException {
        mySQLBusLineDAO = new MySQLBusLineDAO();
        mySQLBusLineDAO.updateBusLineData(appDataController.getBusLineList());
    }
}
