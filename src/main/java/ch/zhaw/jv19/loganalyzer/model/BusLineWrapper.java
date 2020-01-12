package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Wraps a busLine object in order to provide property fields.
 * This is necessary for editable table views.
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class BusLineWrapper {

    private final BusLine busLine;
    private final SimpleStringProperty createdUser;
    private final SimpleStringProperty name;
    private final SimpleStringProperty busType;

    public BusLineWrapper(BusLine busLine) {
        this.createdUser = new SimpleStringProperty(busLine.getCreatedUser());
        this.name = new SimpleStringProperty(busLine.getName());
        this.busType = new SimpleStringProperty(busLine.getBusType());
        this.busLine = busLine;
    }

    public String getCreatedUser() {
        return createdUser.get();
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser.set(createdUser);
        busLine.setCreatedUser(createdUser);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        busLine.setName(name);
    }

    public String getBusType() {
        return busType.get();
    }

    public void setBusType(String busType) {
        this.busType.set(busType);
        this.busLine.setBusType(busType);
    }
}
