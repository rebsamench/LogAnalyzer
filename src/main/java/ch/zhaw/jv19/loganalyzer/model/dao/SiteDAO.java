package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SiteDAO {
    Site getSiteByName(String name) throws SQLException, Exception;
    ObservableList<Site> getAllSitesList() throws SQLException, Exception;
    int saveSite(Site site) throws SQLException, Exception;
    Site extractSiteFromResultSet(ResultSet rs) throws SQLException;
    int[] updateSiteData(ObservableList<Site> siteList) throws SQLException;
}
