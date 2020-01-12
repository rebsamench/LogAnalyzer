package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.BusLine;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface BusLineDAO {
    ObservableList<BusLine> getAllBusLinesList() throws  Exception;
    void saveBusLine(BusLine busLine) throws Exception;
    void updateBusLineData(ObservableList<BusLine> busLineList) throws SQLException;
}
