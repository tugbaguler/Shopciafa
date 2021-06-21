package com.example.shopciafa;

public class MyOrdersItemsModel {

    private int product_image;
    private String product_title;
    private String delivery_information;
    private int rating;

    //constructor method
    public MyOrdersItemsModel(int product_image, String product_title, String delivery_information, int rating) {
        this.product_image = product_image;
        this.product_title = product_title;
        this.delivery_information = delivery_information;
        this.rating = rating;
    }

    //getter and setter methods
    public int getProduct_image() {
        return product_image;
    }

    public void setProduct_image(int product_image) {
        this.product_image = product_image;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getDelivery_information() {
        return delivery_information;
    }

    public void setDelivery_information(String delivery_information) {
        this.delivery_information = delivery_information;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
