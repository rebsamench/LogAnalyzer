package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

/**
 * Holds all Data for a single busLine and provides getter and setter methods.
 * A busLine can consist of multiple field devices, each represented by a LogFile.
 *
 * @author Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class BusLine {
    private int id;
    private ZonedDateTime created;
    private String createdUser;
    private String name;
    private String busType;

    public BusLine() {
    }

    public BusLine(String createdUser, String name, String busType) {
        this.createdUser = createdUser;
        this.name = name;
        this.busType = busType;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getBusType() {
        return busType;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    @Override
    public String toString() {
        return (getName() + ", " +
                getBusType());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + name.hashCode();
        result = 37 * result + busType.hashCode();
        return result;
    }

    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }

        if(!(object instanceof BusLine)) {
            return false;
        }

        BusLine other = (BusLine) object;
        return name.equals(other.name) && busType.equals(other.busType);
    }
}
