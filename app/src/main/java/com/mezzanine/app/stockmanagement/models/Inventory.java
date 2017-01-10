package com.mezzanine.app.stockmanagement.models;

/**
 * Created by ramogiochola on 1/7/17.
 */

public class Inventory {
    private String id;
    private String drugName;
    private String drugItems;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugItems() {
        return drugItems;
    }

    public void setDrugItems(String drugItems) {
        this.drugItems = drugItems;
    }
}
