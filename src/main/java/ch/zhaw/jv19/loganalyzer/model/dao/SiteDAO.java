package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public interface SiteDAO {
    ObservableList<Site> getAllSitesList() throws Exception;
    int saveSite(Site site) throws Exception;
    int[] updateSiteData(ObservableList<Site> siteList) throws SQLException;
}
