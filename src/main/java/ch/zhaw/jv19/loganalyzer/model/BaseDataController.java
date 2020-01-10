package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.MySQLBuslineDAO;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLSiteDAO;
import ch.zhaw.jv19.loganalyzer.model.dao.MySQLUserDAO;
import javafx.fxml.FXML;

import java.sql.SQLException;

public class BaseDataController {

    private boolean doesNameExist;
    private AppDataController appDataController;
    private MySQLUserDAO mySQLUserDAO;
    private MySQLSiteDAO mySQLSiteDAO;
    private MySQLBuslineDAO mySQLBuslineDAO;

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
     * Receives a new busline from BaseDataPanelUIController and hands it over to the corresponding DAO in order to
     * create a new busline.
     */
    @FXML
    public void saveNewBusline(Busline busline) throws Exception {
        appDataController.addBuslineToBuslineList(busline);
        mySQLBuslineDAO = new MySQLBuslineDAO();
        mySQLBuslineDAO.saveBusline(busline);
        appDataController.setMessage("New Busline successfully submitted");
    }

    /**
     * Collects updated user data and hands it over to the corresponding DAO in order to update data in
     * the data base.
     */
    @FXML
    public void updateUserdata() {
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
    public void updateSitedata() {
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
    public void updateBuslinedata() {
        mySQLBuslineDAO = new MySQLBuslineDAO();
        try {
            mySQLBuslineDAO.updateBuslineData(appDataController.getBuslineList());
            appDataController.setMessage("Busline Data successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
            appDataController.setMessage("SQL Error");
        }
    }
}
