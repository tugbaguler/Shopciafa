package com.example.shopciafa;

public class WishlistModel {

    private String product_id;
    private String product_image;
    private String product_title;
    private String product_rating;
    private long total_ratings;
    private String product_price;
    private String price_before_discount;
    private boolean payment_method;

    //constructor method
    public WishlistModel(String product_id, String product_image, String product_title, String rating, long total_ratings,
                         String product_price, String price_before_discount, Boolean payment_method) {
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_title = product_title;
        this.product_rating = rating;
        this.total_ratings = total_ratings;
        this.product_price = product_price;
        this.price_before_discount = price_before_discount;
        this.payment_method = payment_method;
    }

    //getter and setter method

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_rating() {
        return product_rating;
    }

    public void setProduct_Rating(String rating) {
        this.product_rating = rating;
    }

    public long getTotal_ratings() {
        return total_ratings;
    }

    public void setTotal_ratings(long total_ratings) {
        this.total_ratings = total_ratings;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getPrice_before_discount() {
        return price_before_discount;
    }

    public void setPrice_before_discount(String price_before_discount) {
        this.price_before_discount = price_before_discount;
    }

    public Boolean getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(Boolean payment_method) {
        this.payment_method = payment_method;
    }
}
