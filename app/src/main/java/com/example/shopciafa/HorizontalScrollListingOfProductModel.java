package com.example.shopciafa;

public class HorizontalScrollListingOfProductModel {

    private String product_ID;
    private String product_image;
    private String product_brand;
    private String product_short_description;
    private String product_price;

    //generate constructor method
    public HorizontalScrollListingOfProductModel(String product_ID, String product_image, String product_brand, String product_short_description, String product_price) {
        this.product_ID = product_ID;
        this.product_image = product_image;
        this.product_brand = product_brand;
        this.product_short_description = product_short_description;
        this.product_price = product_price;
    }

    //generate getter and setter method
    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public String getProduct_short_description() {
        return product_short_description;
    }

    public void setProduct_short_description(String product_short_description) {
        this.product_short_description = product_short_description;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
