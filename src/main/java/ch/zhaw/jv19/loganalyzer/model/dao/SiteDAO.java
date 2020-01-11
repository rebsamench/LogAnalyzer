package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SiteDAO {
    Site getSiteByName(String name) throws  Exception;
    ObservableList<Site> getAllSitesList() throws Exception;
    int saveSite(Site site) throws Exception;
    Site extractSiteFromResultSet(ResultSet rs) throws SQLException;
    int[] updateSiteData(ObservableList<Site> siteList) throws SQLException;
}
