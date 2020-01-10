package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.Busline;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface BusLineDAO {
    Busline getBuslineByName(String name) throws Exception;
    ObservableList<Busline> getAllBuslinesList() throws  Exception;
    int saveBusline(Busline busline) throws Exception;
    Busline extractBuslineFromResultSet(ResultSet rs) throws SQLException;
    int[] updateBuslineData(ObservableList<Busline> buslineList) throws SQLException;
}
