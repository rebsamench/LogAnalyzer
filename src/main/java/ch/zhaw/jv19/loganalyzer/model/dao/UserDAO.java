package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.User;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public interface UserDAO {
    User getUserByName(String name) throws Exception;
    ObservableList<User> getAllUsersList() throws Exception;
    void saveUser(User user) throws Exception;
    void updateUserData (ObservableList<User> userList) throws SQLException;
}
