package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {
    @Override
    public User getUserByName(String name) throws SQLException {
        User user = null;
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user WHERE name = '?';");
        ResultSet rs =  pstmt.executeQuery();
        return extractUserFromResultSet(rs);
    }

    @Override
    public ObservableList<User> getAllUsersList() throws SQLException {
        ObservableList userList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user;");
        ResultSet rs =  pstmt.executeQuery();
        while(rs.next()) {
            userList.add(extractUserFromResultSet(rs));
        }
        return userList;
    }

    @Override
    public TableView<ObservableList> getAllUsersTable() {
        return null;
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public void updateUser(User user, String[] params) {

    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId( rs.getInt("id") );
        user.setName( rs.getString("name") );
        user.setCreated(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("pass"), MySQLConst.DATETIMEPATTERN));
        user.setCreatedUser(rs.getString("createduser"));
        user.setPassword( rs.getString("age"));
        user.setIsadmin(rs.getInt("isadmin"));
        return user;
    }
}
