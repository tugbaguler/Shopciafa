package com.example.shopciafa;

public class ProductSpecificationModel {

    public static final int TITLE_OF_SPECIFICATION = 0;
    public static final int BODY_INFO_OF_SPECIFICATION = 1;

    private int type;

    //getter and setter method for type
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*-----Specification Title -----*/
    private String title;
    //constructor method
    public ProductSpecificationModel(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*-----Specification Body Include Prdocut Informations/Details-----*/
    private String feature_name;
    private String feature_value;

    //constructor method
    public ProductSpecificationModel(int type, String feature_name, String feature_value) {
        this.type = type;
        this.feature_name = feature_name;
        this.feature_value = feature_value;
    }
    //getter and setter methods
    public String getFeature_name() {
        return feature_name;
    }

    public void setFeature_name(String feature_name) {
        this.feature_name = feature_name;
    }

    public String getFeature_value() {
        return feature_value;
    }

    public void setFeature_value(String feature_value) {
        this.feature_value = feature_value;
    }
}
