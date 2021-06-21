package com.example.shopciafa;

import android.widget.ImageView;

public class AddressesModel {

    private String fullname;
    private String address;
    private String pincode;
    private Boolean selected;

    //constructor method
    public AddressesModel(String fullname, String address, String pincode, Boolean selected) {
        this.fullname = fullname;
        this.address = address;
        this.pincode = pincode;
        this.selected = selected;
    }

    //getter and setter methods
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
