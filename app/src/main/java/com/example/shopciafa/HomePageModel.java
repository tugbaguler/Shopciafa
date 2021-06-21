package com.example.shopciafa;

import java.util.List;

public class HomePageModel {

    public static final int BANNER_SLIDER = 0;
    public static final int STRIP_AD_BANNER = 1;
    public static final int LISTING_PRODUCT_HORIZOTALLY= 2;
    public static final int GRID_LISTING_PRODUCT= 3;
    private int type;
    private String backgroundColor;


    /*----- Banner Slider-----*/
    private List<SliderModel> sliderModelList;

    //constructor method
    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }

    //Getter and Setter methods
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    /*----- Strip Ad Banner-----*/
    private String resource;

    //constructor method
    public HomePageModel(int type, String resource, String backgroundColor) {
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
    }

    //Getter and Setter Methods
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /*----- Listing Product Horizontally -----*/
    private String title;
    private List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList;
    private List<WishlistModel> viewAllProductList;

    //constructor method
    public HomePageModel(int type, String title, String backgroundColor ,List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList,List<WishlistModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalScrollListingOfProductModelList = horizontalScrollListingOfProductModelList;
        this.viewAllProductList = viewAllProductList;
    }

    //wishlist getter and setter methods
    public List<WishlistModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishlistModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }

    /*----- Grid Listing Product -----*/
    public HomePageModel(int type, String title, String backgroundColor , List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalScrollListingOfProductModelList = horizontalScrollListingOfProductModelList;
    }

    //Getter and Setter Methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalScrollListingOfProductModel> getHorizontalScrollListingOfProductModelList() {
        return horizontalScrollListingOfProductModelList;
    }

    public void setHorizontalScrollListingOfProductModelList(List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList) {
        this.horizontalScrollListingOfProductModelList = horizontalScrollListingOfProductModelList;
    }

}
