package ch.zhaw.jv19.loganalyzer.model.dao;

import ch.zhaw.jv19.loganalyzer.model.BusLine;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface BusLineDAO {
    ObservableList<BusLine> getAllBusLinesList() throws  Exception;
    int saveBusLine(BusLine busLine) throws Exception;
    int[] updateBusLineData(ObservableList<BusLine> busLineList) throws SQLException;
}
