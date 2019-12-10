package ch.zhaw.jv19.loganalyzer.model;

import java.time.ZonedDateTime;

public class Site {
    private int id;
        private ZonedDateTime created;
    private String createdUser;
    private String name;
    private String street;
    private String zipCode;
    private String city;
    private String timezone;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return (getName() + ", " +
                getStreet() + ", " +
                getZipCode() + ", " +
                getCity());
    }
}
