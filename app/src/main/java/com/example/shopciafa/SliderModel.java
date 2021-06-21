package com.example.shopciafa;

public class SliderModel {

    private String banner;
    private String background_color;

    //constructor method
    public SliderModel(String banner, String background_color) {
        this.banner = banner;
        this.background_color = background_color;
    }

    //getter and setter methods
    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }
}
