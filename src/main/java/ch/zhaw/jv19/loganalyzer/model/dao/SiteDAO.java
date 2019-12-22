package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Site;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public interface SiteDAO {
    Site getSiteByName(String name) throws SQLException;
    ObservableList<Site> getAllSitesList() throws SQLException;
    TableView<Site> getAllSitesTable();
    int saveSites(Site site) throws SQLException;
}
