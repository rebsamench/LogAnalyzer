package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.User;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public interface UserDAO<T> {
    User getUserByName(String name) throws SQLException;
    ObservableList<User> getAllUsersList() throws SQLException;
    TableView<ObservableList> getAllUsersTable();
    void saveUser(User user);
    void updateUser(User user, String[] params);
}
