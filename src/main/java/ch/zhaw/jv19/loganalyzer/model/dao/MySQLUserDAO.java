package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides interactions with MySQL database to get and write user data to database
 * @author Simon Rizzi, rizzisim@students.zhaw.ch
 */
public class MySQLUserDAO implements UserDAO {

    /**
     * Gets user by name (primary key) from user table.
     * @param name user name
     * @return user object
     * @throws SQLException database exception
     */
    @Override
    public User getUserByName(String name) throws SQLException {
        User user = null;
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user WHERE name = '?';");
        ResultSet rs = pstmt.executeQuery();
        return extractUserFromResultSet(rs);
    }

    /**
     * Gets all users from user table.
     * @return observable list of users
     * @throws SQLException database exception
     */
    @Override
    public ObservableList<User> getAllUsersList() throws SQLException {
        ObservableList<User> userList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user;");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            userList.add(extractUserFromResultSet(rs));
        }
        return userList;
    }

    /**
     * Saves user in data base
     * @param  user to be saved to data base
     * @return number of rows affected. if > 0 -> user has been saved.
     * @throws SQLException database exception
     */
    @Override
    public int saveUser(User user) throws SQLException {
        String[] values = {
                StringUtil.addQuotes.apply(user.getCreatedUser()),
                StringUtil.addQuotes.apply(user.getName()),
                StringUtil.addQuotes.apply(user.getPassword()),
                StringUtil.addQuotes.apply(String.valueOf(user.getIsadmin()))};
        String statementTemplate =
                "INSERT INTO user (createdUser, name, password, isadmin) " +
                        "VALUES (" +
                        values[0] + "," +
                        values[1] + "," +
                        values[2] + "," +
                        values[3] +
                        ") ON DUPLICATE KEY UPDATE " +
                        " createduser" + MySQLConst.EQUALS + values[0] +
                        ", name " + MySQLConst.EQUALS + values[1] +
                        ", password " + MySQLConst.EQUALS + values[2] +
                        ", isadmin " + MySQLConst.EQUALS + values[3] +
                        MySQLConst.ENDQUERY;
        return DBUtil.executeUpdate(statementTemplate);
    }

    /**
     * Extracts user from Resultset.
     * @param rs result set
     * @return user object
     * @throws SQLException database exception
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setCreated(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("created"), MySQLConst.DATETIMEPATTERN));
        user.setCreatedUser(rs.getString("createduser"));
        user.setPassword(rs.getString("password"));
        user.setIsadmin(rs.getInt("isadmin"));
        return user;
    }
}
