package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Busline;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.SQLException;

public interface BusLineDAO {
    Busline getBuslineByName(String name) throws SQLException, Exception;
    ObservableList<Busline> getAllBuslinesList() throws SQLException, Exception;
    int saveBusline(Busline busline) throws SQLException, Exception;
}
