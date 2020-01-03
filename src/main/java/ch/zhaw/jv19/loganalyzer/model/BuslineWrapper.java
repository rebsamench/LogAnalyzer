package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;

public class BuslineWrapper {

    private Busline busline;
    private SimpleStringProperty createdUser;
    private SimpleStringProperty name;
    private SimpleStringProperty bustype;

    public BuslineWrapper (Busline busline) {
        this.createdUser = new SimpleStringProperty(busline.getCreatedUser());
        this.name = new SimpleStringProperty(busline.getName());
        this.bustype = new SimpleStringProperty(busline.getBustype());
        this.busline = busline;
    }

    public BuslineWrapper (String createdUser, String name, String bustype) {
        this.createdUser = new SimpleStringProperty(createdUser);
        this.name = new SimpleStringProperty(name);
        this.bustype = new SimpleStringProperty(bustype);
    }

    public String getCreatedUser() {
        return createdUser.get();
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser.set(createdUser);
        busline.setCreatedUser(createdUser);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        busline.setName(name);
    }

    public String getBustype() {
        return bustype.get();
    }

    public void setBustype(String bustype) {
        this.bustype.set(bustype);
        this.busline.setBustype(bustype);
    }
}
