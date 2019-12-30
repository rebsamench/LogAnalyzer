package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
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
    public Site getSiteByName(String name) throws Exception {
        Site site = null;
        Connection con = DBUtil.getConnection();
        if (con != null) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM site WHERE name = '?';");
            ResultSet rs = pstmt.executeQuery();
            site = extractSiteFromResultSet(rs);
        }
        return site;
    }

    @Override
    public ObservableList<Site> getAllSitesList() throws Exception {
        ObservableList<Site> siteList = FXCollections.observableArrayList();
        Connection con = DBUtil.getConnection();
        if (con != null) {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM site;");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                siteList.add(extractSiteFromResultSet(rs));
            }
        }
        return siteList;
    }

    @Override
    public int saveSite(Site site) throws Exception {
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
        site.setStreet(rs.getString("street"));
        site.setCity(rs.getString("city"));
        site.setTimezone(rs.getString("timezone"));
        site.setZipCode(rs.getString("zipcode"));
        return site;
    }
}
