package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public interface UserDAO {
    User getUserByName(String name) throws SQLException;
    ObservableList<User> getAllUsersList() throws SQLException;
    TableView<User> getAllUsersTable();
    int saveUser(User user) throws SQLException;
}
