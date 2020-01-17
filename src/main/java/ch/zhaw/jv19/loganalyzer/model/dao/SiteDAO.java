package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public interface SiteDAO {
    ObservableList<Site> getAllSitesList() throws Exception;
    void saveSite(Site site) throws Exception;
    void updateSiteData(ObservableList<Site> siteList) throws SQLException;
}
