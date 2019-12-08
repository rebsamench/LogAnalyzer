package ch.zhaw.jv19.loganalyzer.model;

import ch.zhaw.jv19.loganalyzer.model.dao.MySQLUserDAO;
import ch.zhaw.jv19.loganalyzer.model.dao.UserDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class AppDataController {
    AppData appData;

    public AppDataController() {
        appData = new AppData();
        initializeAppData();
    }

    public void setMessage(String message) {
        appData.setMessage(message);
    }

    public SimpleStringProperty getMessage() {
        return appData.getMessage();
    }

    /**
     * Loads data from responsible DAOs and hands it to AppData
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
     */
    public ObservableList<User> getUserList() {
        return appData.getUserList();
    }

}
