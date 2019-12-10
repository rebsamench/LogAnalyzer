package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

public class Busline {
    int id;
    ZonedDateTime created;
    String createdUser;
    String name;
    String bustype;

    public String getName() {
        return name;
    }

    public String getBustype() {
        return bustype;
    }

    @Override
    public String toString() {
        return (getName() + ", " +
                getBustype());
    }
}
