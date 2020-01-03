package ch.zhaw.jv19.loganalyzer.model;

import javafx.beans.property.SimpleStringProperty;

public class SiteWrapper {

    private Site site;
    private SimpleStringProperty createdUser;
    private SimpleStringProperty name;
    private SimpleStringProperty street;
    private SimpleStringProperty zipCode;
    private SimpleStringProperty city;

    public SiteWrapper (Site site) {
        this.createdUser = new SimpleStringProperty(site.getCreatedUser());
        this.name = new SimpleStringProperty(site.getName());
        this.street = new SimpleStringProperty(site.getStreet());
        this.zipCode = new SimpleStringProperty(site.getZipCode());
        this.city = new SimpleStringProperty(site.getCity());
        this.site = site;
    }

    public SiteWrapper (String createdUser, String name, String street, String zipCode, String city) {
        this.createdUser = new SimpleStringProperty(createdUser);
        this.name = new SimpleStringProperty(name);
        this.street = new SimpleStringProperty(street);
        this.zipCode = new SimpleStringProperty(zipCode);
        this.city = new SimpleStringProperty(city);
    }

    public String getCreatedUser() {
        return createdUser.get();
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser.set(createdUser);
        site.setCreatedUser(createdUser);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        site.setName(name);
    }

    public String getStreet() {
        return street.get();
    }

    public void setStreet(String street) {
        this.street.set(street);
        site.setStreet(street);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
        site.setZipCode(zipCode);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
        site.setCity(city);
    }
}
