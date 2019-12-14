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

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
    }

    public String getCreatedUser() {
        return createdUser;
    }

    @Override
    public String toString() {
        return (getName() + ", " +
                getStreet() + ", " +
                getZipCode() + ", " +
                getCity());
    }

    public void setCreatedUser(String createduser) {
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setId(int id) {
    }

    public void setName(String name) {
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
