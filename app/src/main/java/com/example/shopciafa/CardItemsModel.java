package com.example.shopciafa;

import android.widget.TextView;

public class CardItemsModel {

    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*----- Card Items ----*/
    private String product_id;
    private String product_image;
    private String product_title;
    private String product_price;
    private String product_price_without_discount;
    private Long product_quantity;

    //construct method
    public CardItemsModel(int type, String product_id, String product_image, String product_title, String product_price, String product_price_without_discount, long product_quantity) {
        this.type = type;
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_title = product_title;
        this.product_price = product_price;
        this.product_price_without_discount = product_price_without_discount;
        this.product_quantity = product_quantity;
    }

    //getter and setter methods
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

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_price_without_discount() {
        return product_price_without_discount;
    }

    public void setProduct_price_without_discount(String product_price_without_discount) {
        this.product_price_without_discount = product_price_without_discount;
    }

    public long getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(long product_quantity) {
        this.product_quantity = product_quantity;
    }

    /*----- Card Total Amount -----*/


    //constructor method

    public CardItemsModel(int type) {
        this.type = type;
    }
}
