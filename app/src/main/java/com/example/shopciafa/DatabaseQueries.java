package com.example.shopciafa;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.example.shopciafa.AgeGenderPredictionActivity.genderPredict;

public class DatabaseQueries {

    public static final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static String email, profile;
    public static List<CategoryModel> categoryModelList = new ArrayList<>();
    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<List<HomePageModel>> female_lists = new ArrayList<>();
    public static List<List<HomePageModel>> male_lists = new ArrayList<>();
    public static List<String> loadedTheCategoryName = new ArrayList<>();
    public static List<String> wishList = new ArrayList<>();
    public static List<WishlistModel>  wishlistModelList = new ArrayList<>();
    public static List<String> user_rated_id = new ArrayList<>();
    public static List<Long> user_rating = new ArrayList<>();
    public static List<String> cardList = new ArrayList<>();
    public static List<CardItemsModel>  cardItemsModelList = new ArrayList<>();
    public static int selected_address = -1;
    public static List<AddressesModel> addressesModelList = new ArrayList<>();

    public static int pageIndex = -1;

    private static long x = 0;

    public static void loadCategories(final CategoryAdapter categoryAdapter, final Context context){
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // Contains data read from a document in your Firestore database.
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),documentSnapshot.get("category_name").toString()));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
                else {
                    String error_message = task.getException().getMessage();
                    Toast.makeText(context,error_message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadFragmentData(final HomePageAdapter homePageAdapter, final Context context, final int index, String categoryName) {
        // There is an i letter in categories such as fashion, appliances, cosmetics. When the ToUpperCase method is applied, it occurs and this causes an error in the program.
        // Here the letter İ has been changed to I.
        String target = categoryName.toUpperCase();
        Character harf = 'İ';
        if (target.contains("İ")) {
            target = target.replace(harf, 'I');
        }

        firebaseFirestore.collection("CATEGORIES").document(target).collection("TOP_DEALS").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    lists.get(index).clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        // type 0 is slider banners

                        if (Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 0) {
                            List<SliderModel> sliderModelList = new ArrayList<>();
                            long no_of_banners = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_banners")));
                            for (long x = 1; x < no_of_banners; x++) {
                                sliderModelList.add(new SliderModel(documentSnapshot.get("banner_img_" + x).toString()
                                        , documentSnapshot.get("banner_img_" + x + "_background").toString()));
                            }

                            lists.get(index).add(new HomePageModel(0, sliderModelList));

                        }

                        // type 1 is ad banner. (It will be used if you want to advertise in the application.)
                        else if (Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 1) {
                            //lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString(),documentSnapshot.get("strip_ad_banner_background").toString()));
                        }

                        // type 2 is listing deals of the day! (horizontal view)
                        else if (Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 2) {
                            List<WishlistModel> viewAllProducts = new ArrayList<>();
                            List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList = new ArrayList<>();
                            long no_of_products = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_products")));
                            for (long x = 1; x < no_of_products; x++) {
                                horizontalScrollListingOfProductModelList.add(new HorizontalScrollListingOfProductModel(documentSnapshot.get("product_ID_" + x).toString()
                                        , documentSnapshot.get("product_image_" + x).toString()
                                        , documentSnapshot.get("product_title_" + x).toString()
                                        , documentSnapshot.get("product_subtitle_" + x).toString()
                                        , documentSnapshot.get("product_price_" + x).toString()));

                                viewAllProducts.add(new WishlistModel(documentSnapshot.get("product_ID_" + x).toString()
                                        , documentSnapshot.get("product_image_" + x).toString()
                                        , documentSnapshot.get("product_full_title_" + x).toString()
                                        , documentSnapshot.get("average_rating_" + x).toString()
                                        , (long) documentSnapshot.get("total_ratings_" + x)
                                        , documentSnapshot.get("product_price_" + x).toString()
                                        , documentSnapshot.get("product_no_discount_price_" + x).toString()
                                        , (boolean) documentSnapshot.get("COD_" + x)));
                            }
                            lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalScrollListingOfProductModelList, viewAllProducts));
                        }

                        // type 3 is listing new season products. (grid view)
                        else if (Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 3) {
                            List<HorizontalScrollListingOfProductModel> GridLayoutModelList = new ArrayList<>();
                            long no_of_products = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_products")));
                            for (long x = 1; x < no_of_products; x++) {
                                GridLayoutModelList.add(new HorizontalScrollListingOfProductModel(documentSnapshot.get("product_ID_" + x).toString()
                                        , documentSnapshot.get("product_image_" + x).toString()
                                        , documentSnapshot.get("product_title_" + x).toString()
                                        , documentSnapshot.get("product_subtitle_" + x).toString()
                                        , documentSnapshot.get("product_price_" + x).toString()));
                            }
                            lists.get(index).add(new HomePageModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), GridLayoutModelList));
                        }
                    }
                    homePageAdapter.notifyDataSetChanged();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadFemaleFragmentData(final HomePageAdapter homePageAdapter, final Context context, final int index, String categoryName) {
        // There is an i letter in categories such as fashion, appliances, cosmetics. When the ToUpperCase method is applied, it occurs and this causes an error in the program.
        // Here the letter İ has been changed to I.
        String target = categoryName.toUpperCase();
        Character harf = 'İ';
        if (target.contains("İ")) {
            target = target.replace(harf, 'I');
        }
        firebaseFirestore.collection("CATEGORIES").document("HOME").collection("TOP_DEALS").whereEqualTo("gender", 1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    female_lists.get(index).clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        // type 0 is slider banners
                        if (Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 0) {
                            List<SliderModel> sliderModelList = new ArrayList<>();
                            long no_of_banners = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_banners")));
                            for (long x = 1; x < no_of_banners; x++) {
                                sliderModelList.add(new SliderModel(documentSnapshot.get("banner_img_" + x).toString()
                                        , documentSnapshot.get("banner_img_" + x + "_background").toString()));
                            }
                            female_lists.get(index).add(new HomePageModel(0, sliderModelList));
                        }
                        // type 1 is ad banner. (It will be used if you want to advertise in the application.)
                        else if (Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 1) {
                            //lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString(),documentSnapshot.get("strip_ad_banner_background").toString()));
                        }
                        // type 2 is listing deals of the day! (horizontal view)
                        else if (Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 2) {
                            List<WishlistModel> viewAllProducts = new ArrayList<>();
                            List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList = new ArrayList<>();
                            long no_of_products = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_products")));
                            for (long x = 1; x < no_of_products; x++) {
                                horizontalScrollListingOfProductModelList.add(new HorizontalScrollListingOfProductModel(documentSnapshot.get("product_ID_" + x).toString()
                                        , documentSnapshot.get("product_image_" + x).toString()
                                        , documentSnapshot.get("product_title_" + x).toString()
                                        , documentSnapshot.get("product_subtitle_" + x).toString()
                                        , documentSnapshot.get("product_price_" + x).toString()));

                                viewAllProducts.add(new WishlistModel(documentSnapshot.get("product_ID_" + x).toString()
                                        , documentSnapshot.get("product_image_" + x).toString()
                                        , documentSnapshot.get("product_full_title_" + x).toString()
                                        , documentSnapshot.get("average_rating_" + x).toString()
                                        , (long) documentSnapshot.get("total_ratings_" + x)
                                        , documentSnapshot.get("product_price_" + x).toString()
                                        , documentSnapshot.get("product_no_discount_price_" + x).toString()
                                        , (boolean) documentSnapshot.get("COD_" + x)));
                            }
                            female_lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalScrollListingOfProductModelList, viewAllProducts));
                        }
                        // type 3 is listing new season products. (grid view)
                        else if (Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 3) {
                            List<HorizontalScrollListingOfProductModel> GridLayoutModelList = new ArrayList<>();
                            long no_of_products = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_products")));
                            for (long x = 1; x < no_of_products; x++) {
                                GridLayoutModelList.add(new HorizontalScrollListingOfProductModel(documentSnapshot.get("product_ID_" + x).toString()
                                        , documentSnapshot.get("product_image_" + x).toString()
                                        , documentSnapshot.get("product_title_" + x).toString()
                                        , documentSnapshot.get("product_subtitle_" + x).toString()
                                        , documentSnapshot.get("product_price_" + x).toString()));
                            }
                            female_lists.get(index).add(new HomePageModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), GridLayoutModelList));
                        }
                    }
                    homePageAdapter.notifyDataSetChanged();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadMaleFragmentData(final HomePageAdapter homePageAdapter, final Context context, final int index, String categoryName) {
        // There is an i letter in categories such as fashion, appliances, cosmetics. When the ToUpperCase method is applied, it occurs and this causes an error in the program.
        // Here the letter İ has been changed to I.
        String target = categoryName.toUpperCase();
        Character harf = 'İ';
        if (target.contains("İ")) {
            target = target.replace(harf, 'I');
        }

        firebaseFirestore.collection("CATEGORIES").document("HOME").collection("TOP_DEALS").whereEqualTo("gender", 2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if(task.isSuccessful()){

                male_lists.get(index).clear();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    // type 0 is slider banners

                    if(Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 0){
                        List<SliderModel> sliderModelList = new ArrayList<>();
                        long no_of_banners = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_banners")));
                        for(long x = 1; x < no_of_banners; x++){
                            sliderModelList.add(new SliderModel(documentSnapshot.get("banner_img_"+x).toString()
                                    ,documentSnapshot.get("banner_img_"+x+"_background").toString()));
                        }

                        male_lists.get(index).add(new HomePageModel(0, sliderModelList));

                    }

                    // type 1 is ad banner. (It will be used if you want to advertise in the application.)
                    else if(Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 1){
                        //lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString(),documentSnapshot.get("strip_ad_banner_background").toString()));
                    }

                    // type 2 is listing deals of the day! (horizontal view)
                    else if(Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 2){
                        List<WishlistModel> viewAllProducts = new ArrayList<>();
                        List<HorizontalScrollListingOfProductModel> horizontalScrollListingOfProductModelList = new ArrayList<>();
                        long no_of_products = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_products")));
                        for(long x = 1; x < no_of_products; x++){
                            horizontalScrollListingOfProductModelList.add(new HorizontalScrollListingOfProductModel(documentSnapshot.get("product_ID_"+x).toString()
                                    ,documentSnapshot.get("product_image_"+x).toString()
                                    ,documentSnapshot.get("product_title_"+x).toString()
                                    ,documentSnapshot.get("product_subtitle_"+x).toString()
                                    ,documentSnapshot.get("product_price_"+x).toString()));

                            viewAllProducts.add(new WishlistModel(documentSnapshot.get("product_ID_"+x).toString()
                                    ,documentSnapshot.get("product_image_"+x).toString()
                                    ,documentSnapshot.get("product_full_title_"+x).toString()
                                    ,documentSnapshot.get("average_rating_"+x).toString()
                                    ,(long)documentSnapshot.get("total_ratings_"+x)
                                    ,documentSnapshot.get("product_price_"+x).toString()
                                    ,documentSnapshot.get("product_no_discount_price_"+x).toString()
                                    ,(boolean)documentSnapshot.get("COD_"+x)));
                        }
                        male_lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalScrollListingOfProductModelList,viewAllProducts));
                    }

                    // type 3 is listing new season products. (grid view)
                    else if(Long.parseLong(String.valueOf(documentSnapshot.get("view_type"))) == 3){
                        List<HorizontalScrollListingOfProductModel> GridLayoutModelList = new ArrayList<>();
                        long no_of_products = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_products")));
                        for(long x = 1; x < no_of_products; x++){
                            GridLayoutModelList.add(new HorizontalScrollListingOfProductModel(documentSnapshot.get("product_ID_"+x).toString()
                                    ,documentSnapshot.get("product_image_"+x).toString()
                                    ,documentSnapshot.get("product_title_"+x).toString()
                                    ,documentSnapshot.get("product_subtitle_"+x).toString()
                                    ,documentSnapshot.get("product_price_"+x).toString()));
                        }
                        male_lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), GridLayoutModelList));
                    }
                }
                homePageAdapter.notifyDataSetChanged();

            }
            else{
                String error = task.getException().getMessage();
                Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
            }
        }
    });
}

    public static void loadWishList(final Context context, final  boolean loadProductData){
        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (x = 0; x < Long.parseLong(String.valueOf(task.getResult().get("list_size"))); x++){
                        if(task.getResult().get("product_ID_" + x ) == null) { continue; }

                        wishList.add(task.getResult().get("product_ID_" + x ).toString());

                        if (DatabaseQueries.wishList.contains(ProductDetailsActivity.product_id)){
                            ProductDetailsActivity.PRODUCT_ALREADY_ADDED_TO_WHISHLIST = true;
                            if(ProductDetailsActivity.btn_add_to_wishlist != null) {
                                ProductDetailsActivity.btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#fe6d73")));
                            }
                        }
                        else{
                            if(ProductDetailsActivity.btn_add_to_wishlist != null) {
                                ProductDetailsActivity.btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                            }
                            ProductDetailsActivity.PRODUCT_ALREADY_ADDED_TO_WHISHLIST = false;
                        }

                        if(loadProductData) {
                            wishlistModelList.clear();
                            final String product_id = task.getResult().get("product_ID_"+x).toString().trim();
                            firebaseFirestore.collection("PRODUCTS").document(product_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        wishlistModelList.add(new WishlistModel(product_id
                                                , task.getResult().get("product_image_1").toString()
                                                , task.getResult().get("product_title").toString()
                                                , task.getResult().get("avarage_rating").toString()
                                                , Long.parseLong(String.valueOf(task.getResult().get("total_ratings")))
                                                , task.getResult().get("product_price").toString()
                                                , task.getResult().get("product_no_discount_price").toString()
                                                , (boolean) task.getResult().get("COD")
                                        ));
                                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                                    } else {
                                        String error_message = task.getException().getMessage();
                                        Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
                else{
                    String error_message = task.getException().getMessage();
                    Toast.makeText(context,error_message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void removeProductFromWishlist(final int index, final Context context){
        //final String product_id = ("PRODUCT_ID_"+ x).trim();
        // if(wishList.size() <= 0) { return; }
        final String removed_product_id = wishList.get(index);
        wishList.remove(index);
        Map<String,Object> updateWishlist = new HashMap<>();

        for (int x = 0 ; x < wishList.size() ; x++){
            updateWishlist.put("product_ID_" + x, wishList.get(x));
        }
        updateWishlist.put("list_size", (long) wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (wishlistModelList.size() != 0){
                        wishlistModelList.remove(index);
                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    ProductDetailsActivity.PRODUCT_ALREADY_ADDED_TO_WHISHLIST = false;
                    ProductDetailsActivity.btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                    Toast.makeText(context,"Product Remove Successfully!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(ProductDetailsActivity.btn_add_to_wishlist != null){
                        ProductDetailsActivity.btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                    }
                    wishList.add(index,removed_product_id);
                    String error_message = task.getException().getMessage();
                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                }
                if(ProductDetailsActivity.btn_add_to_wishlist != null) {
                    ProductDetailsActivity.btn_add_to_wishlist.setEnabled(true);
                }
            }
        });

    }

    public static void loadRatingList(final Context context){
        user_rated_id.clear();
        user_rating.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long x = 0 ; x < (long)task.getResult().get("list_size") ; x++){
                        user_rated_id.add(task.getResult().get("product_ID_" + x).toString());
                        user_rating.add((long)task.getResult().get("rating_" + x));
                        if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailsActivity.product_id) && ProductDetailsActivity.giving_star_to_the_product != null){
                            ProductDetailsActivity.initial_rating = Integer.parseInt(String.valueOf((long)task.getResult().get("rating_" + x)))-1;
                            ProductDetailsActivity.setRating(ProductDetailsActivity.initial_rating);
                        }
                    }
                }
                else{
                    String error_message = task.getException().getMessage();
                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadCardList(final Context context, final boolean loadProductData){
        cardList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CARD")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (x = 0; x < Long.parseLong(String.valueOf(task.getResult().get("list_size"))); x++){
                        if(task.getResult().get("product_ID_" + x ) == null) { continue; }
                        cardList.add(task.getResult().get("product_ID_" + x ).toString());

                        if (DatabaseQueries.cardList.contains(ProductDetailsActivity.product_id)){
                            ProductDetailsActivity.PRODUCT_ALREADY_ADDED_TO_CARD = true;
                        }
                        else{
                            ProductDetailsActivity.PRODUCT_ALREADY_ADDED_TO_CARD = false;
                        }

                        if(loadProductData) {
                            cardItemsModelList.clear();
                            final String product_id = task.getResult().get("product_ID_"+x).toString().trim();
                            firebaseFirestore.collection("PRODUCTS").document(product_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int index = 0;
                                        if (cardList.size() >= 2 ){
                                            index = cardList.size() - 2;
                                        }
                                        cardItemsModelList.add(index, new CardItemsModel(CardItemsModel.CART_ITEM, product_id
                                                , task.getResult().get("product_image_1").toString()
                                                , task.getResult().get("product_title").toString()
                                                , task.getResult().get("product_price").toString()
                                                , task.getResult().get("product_no_discount_price").toString()
                                                , (long) 1
                                        ));
                                        if (cardList.size() == 1){
                                            cardItemsModelList.add(new CardItemsModel(CardItemsModel.TOTAL_AMOUNT));
                                        }

                                        if (cardList.size() == 0){
                                            cardItemsModelList.clear();
                                        }

                                        MyCardFragment.cardAdapter.notifyDataSetChanged();
                                    } else {
                                        String error_message = task.getException().getMessage();
                                        Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
                else{
                    String error_message = task.getException().getMessage();
                    Toast.makeText(context,error_message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void removeProductFromCard(final int index, final Context context){
        //final String product_id = ("PRODUCT_ID_"+ x).trim();
        // if(wishList.size() <= 0) { return; }
        final String removed_product_id = cardList.get(index);
        cardList.remove(index);
        Map<String,Object> updateCardList = new HashMap<>();

        for (int x = 0 ; x < cardList.size() ; x++){
            updateCardList.put("product_ID_" + x, cardList.get(x));
        }
        updateCardList.put("list_size", (long) cardList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CARD")
                .set(updateCardList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (cardItemsModelList.size() != 0){
                        cardItemsModelList.remove(index);
                        MyCardFragment.cardAdapter.notifyDataSetChanged();
                    }
                    if (cardList.size() == 0){
                        cardItemsModelList.clear();
                    }
                    Toast.makeText(context,"Product Remove Successfully!",Toast.LENGTH_SHORT).show();
                }
                else{
                    cardList.add(index,removed_product_id);
                    String error_message = task.getException().getMessage();
                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static void loadAddress(final Context context){
        addressesModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESS")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Intent delivery_intent;
                    if(Long.parseLong(String.valueOf(task.getResult().get("list_size"))) == 0){
                        delivery_intent = new Intent(context,AddressActivity.class); // add address
                        delivery_intent.putExtra("INTENT","delivery_intent");
                    }
                    else{
                        for (x = 1; x < (Long.parseLong(String.valueOf(task.getResult().get("list_size")))+1); x++){
                            addressesModelList.add(new AddressesModel(task.getResult().get("fullname_" + x).toString()
                                    ,task.getResult().get("address_" + x).toString()
                                    ,task.getResult().get("pincode_" + x).toString()
                                    ,(boolean)task.getResult().get("selected_" + x)
                            ));
                            if ((boolean)task.getResult().get("selected_" + x)){
                                selected_address = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        delivery_intent = new Intent(context,DeliveryActivity.class);
                    }
                    context.startActivity(delivery_intent);
                }else{
                    String error_message = task.getException().getMessage();
                    Toast.makeText(context,error_message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void clearData(){
        categoryModelList.clear();
        lists.clear();
        loadedTheCategoryName.clear();
        wishList.clear();
        wishlistModelList.clear();
        cardList.clear();
        cardItemsModelList.clear();
        addressesModelList.clear();
    }
}
