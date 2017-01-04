package com.mezzanine.app.stockmanagement.models;

/**
 * Created by ramogiochola on 1/4/17.
 */

public class Clinic {
    String id;
    String name;
    String country;
    String city;
    Integer nevirapine;
    Integer stavudine;
    Integer zidotabine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNevirapine() {
        return nevirapine;
    }

    public void setNevirapine(Integer nevirapine) {
        this.nevirapine = nevirapine;
    }

    public Integer getStavudine() {
        return stavudine;
    }

    public void setStavudine(Integer stavudine) {
        this.stavudine = stavudine;
    }

    public Integer getZidotabine() {
        return zidotabine;
    }

    public void setZidotabine(Integer zidotabine) {
        this.zidotabine = zidotabine;
    }
}
