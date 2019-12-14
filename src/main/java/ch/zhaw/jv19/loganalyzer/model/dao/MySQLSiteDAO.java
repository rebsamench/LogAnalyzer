package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
import ch.zhaw.jv19.loganalyzer.model.User;
import ch.zhaw.jv19.loganalyzer.util.datatype.DateUtil;
import ch.zhaw.jv19.loganalyzer.util.datatype.StringUtil;
import ch.zhaw.jv19.loganalyzer.util.db.DBUtil;
import ch.zhaw.jv19.loganalyzer.util.db.MySQLConst;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLSiteDAO implements SiteDAO {

    @Override
    public Site getSiteByName(String name) throws SQLException {
        Site site = null;
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM site WHERE name = '?';");
        ResultSet rs = pstmt.executeQuery();
        return extractSiteFromResultSet(rs);
    }

    @Override
    public ObservableList<Site> getAllSitesList() throws SQLException {
        ObservableList userList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user;");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            userList.add(extractSiteFromResultSet(rs));
        }
        return userList;
    }

    @Override
    public TableView<ObservableList> getAllSitesTable() {
        return null;
    }

    @Override
    public int saveSites(Site site) throws SQLException {
        String[] values = {
                StringUtil.addQuotes.apply(site.getCreatedUser()),
                };
        String statementTemplate =
                "INSERT INTO user (createdUser, password, isadmin) " +
                        "VALUES (" +
                        values[0] + "," +
                        values[1] + "," +
                        values[2] +
                        ") ON DUPLICATE KEY UPDATE " +
                        " createdUser" + MySQLConst.EQUALS + values[0] +
                        " password " + MySQLConst.EQUALS + values[1] +
                        " isadmin " + MySQLConst.EQUALS + values[2] +
                        MySQLConst.ENDQUERY;
        return DBUtil.executeUpdate(statementTemplate);
    }

    private Site extractSiteFromResultSet(ResultSet rs) throws SQLException {
        Site site = new Site();
        site.setId(rs.getInt("id"));
        site.setCreated(DateUtil.getZonedDateTimeFromDateTimeString(rs.getString("created"), MySQLConst.DATETIMEPATTERN));
        site.setCreatedUser(rs.getString("createduser"));
        site.setName(rs.getString("name"));
        return site;
    }
}
