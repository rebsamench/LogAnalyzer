package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

/**
 * Holds all Data for a single busline and provides getter and setter methods.
 * A busline can consist of multiple field devices, each represented by a LogFile.
 *
 * @author: Christoph Rebsamen, rebsach1@students.zhaw.ch
 */
public class Busline {
    private int id;
    private ZonedDateTime created;
    private String createdUser;
    private String name;
    private String bustype;

    public Busline() {
    }

    public Busline(String createdUser, String name, String bustype) {
        this.createdUser = createdUser;
        this.name = name;
        this.bustype = bustype;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getBustype() {
        return bustype;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    @Override
    public String toString() {
        return (getName() + ", " +
                getBustype());
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

    public void setBustype(String bustype) {
        this.bustype = bustype;
    }

    public int hashCode() {
        int ergebnis = 17;
        ergebnis = 37 * ergebnis + name.hashCode();
        ergebnis = 37 * ergebnis + bustype.hashCode();
        return ergebnis;
    }

    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }

        if(!(object instanceof Busline)) {
            return false;
        }

        Busline anderer = (Busline) object;
        return name.equals(anderer.name) && bustype == anderer.bustype;
    }
}
