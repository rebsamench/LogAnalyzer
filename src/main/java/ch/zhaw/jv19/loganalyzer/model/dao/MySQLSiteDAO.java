package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
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
 * Provides functionality for extraction, update and save of site data from and to data base.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class MySQLSiteDAO implements SiteDAO {

    /**
     * Returns a list of all sites in the data base.
     *
     * @return list of sites
     * @throws Exception if query fails
     */
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

    /**
     * Saves a single site instance to the data base.
     *
     * @param site Site instance
     * @return int represents the row count.
     * @throws Exception if saving fails
     */
    @Override
    public void saveSite(Site site) throws Exception {
        String[] values = {
                StringUtil.wrapQuotes.apply(site.getCreatedUser()),
                StringUtil.wrapQuotes.apply(site.getName()),
                StringUtil.wrapQuotes.apply(site.getStreet()),
                StringUtil.wrapQuotes.apply(site.getZipCode()),
                StringUtil.wrapQuotes.apply(site.getCity()),
                StringUtil.wrapQuotes.apply(site.getTimezone())};
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
        DBUtil.executeUpdate(statementTemplate);
    }

    /**
     * Extracts a single site from a result set.
     *
     * @param rs result set
     * @return extracted site
     * @throws SQLException if extraction fails
     */
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

    /**
     * Updates site date in the data base provided in a list.
     *
     * @param siteList list for sites
     * @return int[] representing the updated rows
     * @throws SQLException if updating fails
     */
    public void updateSiteData(ObservableList<Site> siteList) throws SQLException {
        Connection connection = DBUtil.getConnection();
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
            ps.executeBatch();
    }
}
