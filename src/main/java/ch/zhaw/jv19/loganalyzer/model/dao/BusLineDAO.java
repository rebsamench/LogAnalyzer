package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Busline;
import ch.zhaw.jv19.loganalyzer.model.Site;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public interface BusLineDAO {
    Busline getBuslineByName(String name) throws SQLException;
    ObservableList<Busline> getAllBuslinesList() throws SQLException;
    TableView<ObservableList> getAllBuslinesTable();
    int saveBusline(Busline busline) throws SQLException;
}
