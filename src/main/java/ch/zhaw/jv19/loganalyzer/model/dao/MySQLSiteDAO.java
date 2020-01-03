package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
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
                StringUtil.addQuotes.apply(site.getName()),
                StringUtil.addQuotes.apply(site.getStreet()),
                StringUtil.addQuotes.apply(site.getZipCode()),
                StringUtil.addQuotes.apply(site.getCity()),
                StringUtil.addQuotes.apply(site.getTimezone())};
        String statementTemplate =
                "INSERT INTO site (createduser, name, street, zipcode, city, timezone) " +
                        "VALUES (" +
                        values[0] + "," +
                        values[1] + "," +
                        values[2] + "," +
                        values[3] + "," +
                        values[4] + "," +
                        values[5] +
                        ") ON DUPLICATE KEY UPDATE " +
                        " createduser" + MySQLConst.EQUALS + values[0] +
                        ", name " + MySQLConst.EQUALS + values[1] +
                        ", street " + MySQLConst.EQUALS + values[2] +
                        ", zipcode " + MySQLConst.EQUALS + values[3] +
                        ", city " + MySQLConst.EQUALS + values[4] +
                        ", timezone " + MySQLConst.EQUALS + values[5] +
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

    public int[] updateSiteData(ObservableList<Site> siteList) throws SQLException {
        Connection connection = DBUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE SITE SET createduser = ?, name = ?, street = ?, zipcode = ?, city = ?, timezone = ? WHERE id = ?");
            for (Site site : siteList) {
                ps.setString(1, site.getCreatedUser());
                ps.setString(2, site.getName());
                ps.setString(3, site.getStreet());
                ps.setString(4, site.getZipCode());
                ps.setString(5, site.getCity());
                ps.setString(6, site.getTimezone());
                ps.setInt(7, site.getId());
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
