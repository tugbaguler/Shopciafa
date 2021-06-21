package com.example.shopciafa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager product_images_viewPager;
    private TabLayout viewPager_tabLayout_indicator;

    private TextView product_title;
    private TextView avarage_ratings;
    private TextView total_ratings;
    private TextView product_price;
    private TextView price_before_discount;
    private ImageView fast_paymet_COD_imageView;
    private TextView fast_paymet_COD_textView;

    private ConstraintLayout product_details_only_container;
    private ConstraintLayout product_detais_tabs_container;
    private ViewPager product_details_viewPager;
    private TabLayout product_details_tabLayout;
    private TextView product_only_description_body;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    private String product_description;
    private String product_details;

    public static int initial_rating;
    public static LinearLayout giving_star_to_the_product;
    private TextView total_product_ratings;
    private TextView product_avarage_rating_value;
    private LinearLayout ratings_progressbar_container;

    private Button btn_buy_now;
    private LinearLayout btn_add_to_card;

    public static boolean PRODUCT_ALREADY_ADDED_TO_WHISHLIST = false;
    public static boolean PRODUCT_ALREADY_ADDED_TO_CARD = false;
    public static FloatingActionButton btn_add_to_wishlist;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser current_user;
    public static String product_id;

    private String tag = "tugba";

    private DocumentSnapshot documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product_title = findViewById(R.id.product_title);
        avarage_ratings = findViewById(R.id.rating_of_product_textView);
        total_ratings = findViewById(R.id.total_ratings_textView);
        product_price = findViewById(R.id.product_price);
        price_before_discount = findViewById(R.id.price_before_discount);
        fast_paymet_COD_imageView = findViewById(R.id.payment_imageView);
        fast_paymet_COD_textView = findViewById(R.id.fast_paymet_textView);
        product_images_viewPager = findViewById(R.id.viewPager_product_images);
        viewPager_tabLayout_indicator = findViewById(R.id.viewPager_tabLayout_indicator);
        btn_add_to_wishlist = findViewById(R.id.btn_add_to_wishlist);
        product_details_viewPager = findViewById(R.id.product_details_viewPager);
        product_details_tabLayout = findViewById(R.id.product_details_tabLayout);
        product_details_only_container = findViewById(R.id.product_details_container);
        product_detais_tabs_container = findViewById(R.id.constraintLayout_product_details_tabs);
        product_only_description_body = findViewById(R.id.product_details_information_body);
        total_product_ratings = findViewById(R.id.product_total_ratings);
        product_avarage_rating_value = findViewById(R.id.product_avarage_rating_value);
        ratings_progressbar_container = findViewById(R.id.ratings_progressbar_container);
        btn_buy_now = findViewById(R.id.btn_buy_now);
        btn_add_to_card = findViewById(R.id.btn_add_to_card);

        initial_rating = -1;

        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();
        final String product_id = getIntent().getStringExtra("PRODUCT_ID").trim();
        //Log.d(tag, "Product id => " + product_id);

         //firebaseFirestore.collection("PRODUCTS").document("NOWohrgqvg3pgKf9vvwt").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         // If the user clicks on the product to get detailed information about the product; The id of the product should be taken automatically. As above, each document path should not be entered one by one.
         firebaseFirestore.collection("PRODUCTS").document(product_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    //Other images of a product for the user's review
                    final long no_of_product_images = Long.parseLong(String.valueOf(documentSnapshot.get("no_of_product_images")));
                    //Log.d(tag, String.valueOf(no_of_product_images));
                    for (long tg = 1; tg <= no_of_product_images; tg++) {
                        productImages.add(documentSnapshot.get("product_image_" + tg).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    product_images_viewPager.setAdapter(productImagesAdapter);

                    product_title.setText(documentSnapshot.get("product_title").toString());
                    avarage_ratings.setText(documentSnapshot.get("avarage_rating").toString());
                    total_ratings.setText("(" + (long) documentSnapshot.get("total_ratings") + ") ratings".toString());
                    product_price.setText(documentSnapshot.get("product_price").toString());
                    price_before_discount.setText(documentSnapshot.get("product_no_discount_price").toString());
                    if ((boolean) documentSnapshot.get("COD")) {
                        fast_paymet_COD_imageView.setVisibility(View.VISIBLE);
                        fast_paymet_COD_textView.setVisibility(View.VISIBLE);
                    } else {
                        fast_paymet_COD_imageView.setVisibility(View.INVISIBLE);
                        fast_paymet_COD_textView.setVisibility(View.INVISIBLE);
                    }

                    if ((boolean) documentSnapshot.get("tab_layout")) {
                        product_detais_tabs_container.setVisibility(View.VISIBLE);
                        // product_details_only_container.setVisibility(View.GONE);
                        product_description = documentSnapshot.get("product_description").toString();
                        //ProductSpecificationFragment.productSpecificationModelList = new ArrayList<>();
                        product_details = documentSnapshot.get("product_details").toString();

                        for (long t = 1; t <= (long) documentSnapshot.get("total_specification_table"); t++) {
                            productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("specification_title_" + t).toString()));
                            for (long g = 1; g <= (long) documentSnapshot.get("specification_title_" + t + "_total_fields"); g++) {
                                productSpecificationModelList.add(new ProductSpecificationModel(1,
                                        documentSnapshot.get("specification_title_" + t + "_field_" + g + "_name").toString(),
                                        documentSnapshot.get("specification_title_" + t + "_field_" + g + "_value").toString()));
                            }
                        }
                    } else {
                        product_detais_tabs_container.setVisibility(View.GONE);
                        product_details_only_container.setVisibility(View.VISIBLE);
                        product_only_description_body.setText(documentSnapshot.get("product_details").toString());
                    }
                    product_avarage_rating_value.setText(documentSnapshot.get("avarage_rating").toString());
                    total_product_ratings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                    for (int s = 0; s < 5; s++) {
                        ProgressBar progressBar = (ProgressBar) ratings_progressbar_container.getChildAt(s);
                        int maximum_progress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maximum_progress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - s) + "_star"))));
                    }
                    product_details_viewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), product_details_tabLayout.getTabCount(), product_description, product_details, productSpecificationModelList));

                    if (current_user != null) {
                        if (DatabaseQueries.user_rating.size() == 0){
                            DatabaseQueries.loadRatingList(ProductDetailsActivity.this);
                        }
                        if (DatabaseQueries.wishList.size() == 0) {
                            DatabaseQueries.loadWishList(ProductDetailsActivity.this, false);
                        }
                        if (DatabaseQueries.cardList.size() == 0) {
                            DatabaseQueries.loadCardList(ProductDetailsActivity.this, false);
                        }
                    }

                    if(DatabaseQueries.user_rated_id.contains(product_id)){
                        int index = DatabaseQueries.user_rated_id.indexOf(product_id);
                        initial_rating = Integer.parseInt(String.valueOf(DatabaseQueries.user_rating.get(index)))-1;
                        setRating(initial_rating);
                    }
                    if (DatabaseQueries.wishList.contains(product_id)) {
                        PRODUCT_ALREADY_ADDED_TO_WHISHLIST = true;
                        btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#fe6d73")));
                    } else {
                        PRODUCT_ALREADY_ADDED_TO_WHISHLIST = false;
                    }
                    if (DatabaseQueries.cardList.contains(product_id)) {
                        PRODUCT_ALREADY_ADDED_TO_CARD = true;
                    } else {
                        PRODUCT_ALREADY_ADDED_TO_CARD = false;
                    }
                } else {
                    String error_message = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error_message, Toast.LENGTH_SHORT).show();
                }
            }
        });


        viewPager_tabLayout_indicator.setupWithViewPager(product_images_viewPager,true);

        btn_add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //the button changes color when the product is added to favorites
                if(current_user != null) {
                    if (PRODUCT_ALREADY_ADDED_TO_WHISHLIST) {
                        int index = DatabaseQueries.wishList.indexOf(product_id);
                        DatabaseQueries.removeProductFromWishlist(index,ProductDetailsActivity.this);
                        btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#fe6d73")));
                    } else {
                        btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                        Map<String,Object> add_product = new HashMap<>();
                        add_product.put("product_ID_" + String.valueOf(DatabaseQueries.wishList.size()), product_id);

                        firebaseFirestore.collection("USERS").document(current_user.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                .update(add_product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    Map<String,Object> update_list_size = new HashMap<>();
                                    update_list_size.put("list_size", (long) DatabaseQueries.wishList.size() + 1);

                                    firebaseFirestore.collection("USERS").document(current_user.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                            .update(update_list_size).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                if(DatabaseQueries.wishlistModelList.size() != 0){
                                                    DatabaseQueries.wishlistModelList.add(new WishlistModel(product_id
                                                            ,documentSnapshot.get("product_image_1").toString()
                                                            ,documentSnapshot.get("product_title").toString()
                                                            ,documentSnapshot.get("avarage_rating").toString()
                                                            ,(long)documentSnapshot.get("total_ratings")
                                                            ,documentSnapshot.get("product_price").toString()
                                                            ,documentSnapshot.get("product_no_discount_price").toString()
                                                            ,(boolean)documentSnapshot.get("COD")
                                                    ));
                                                }

                                                PRODUCT_ALREADY_ADDED_TO_WHISHLIST = true;
                                                btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#fe6d73")));
                                                DatabaseQueries.wishList.add(product_id);
                                                Toast.makeText(ProductDetailsActivity.this,"Product Added to Wishlis Successfully",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                                                String error_message = task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                            }
                                            btn_add_to_wishlist.setEnabled(true);
                                        }
                                    });

                                }
                                else{
                                    btn_add_to_wishlist.setEnabled(true);
                                    String error_message = task.getException().getMessage();
                                    Toast.makeText(ProductDetailsActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        product_details_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(product_details_tabLayout));
        product_details_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                product_details_viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /*-----Product Rating Area-----*/
        giving_star_to_the_product = findViewById(R.id.your_ratings_stars_container);
        for (int x = 0; x < giving_star_to_the_product.getChildCount(); x++){
            final int starPosition = x;
            giving_star_to_the_product.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        if (current_user != null) {
                            setRating(starPosition);
                            if (DatabaseQueries.user_rated_id.contains(product_id)) {

                            } else {
                                Map<String, Object> product_rating = new HashMap<>();
                                product_rating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                product_rating.put("avarage_rating", AverageRatingCalculation(starPosition + 1));
                                product_rating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                firebaseFirestore.collection("PRODUCTS").document(product_id).update(product_rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> rating = new HashMap<>();
                                            rating.put("list_size",(long) DatabaseQueries.user_rated_id.size() + 1);
                                            rating.put("product_ID_" + DatabaseQueries.user_rated_id.size(), product_id);
                                            rating.put("rating_" + DatabaseQueries.user_rated_id.size(), (long) starPosition + 1);

                                            firebaseFirestore.collection("USERS").document(current_user.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        DatabaseQueries.user_rated_id.add(product_id);
                                                        DatabaseQueries.user_rating.add((long) starPosition + 1);

                                                        total_ratings.setText("(" + ((long)documentSnapshot.get("total_ratings") + 1) + ") ratings");
                                                        total_product_ratings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                        avarage_ratings.setText(String.valueOf(AverageRatingCalculation(starPosition + 1)));
                                                        product_avarage_rating_value.setText(String.valueOf(AverageRatingCalculation(starPosition + 1)));

                                                        for (int s = 0; s < 5; s++) {
                                                            ProgressBar progressBar = (ProgressBar) ratings_progressbar_container.getChildAt(s);
                                                            int maximum_progress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings") + 1 ));
                                                            progressBar.setMax(maximum_progress);
                                                            progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - s) + "_star"))));
                                                        }
                                                        Toast.makeText(ProductDetailsActivity.this, "Thank you for rating", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        setRating(initial_rating);
                                                        String error_message = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        } else {
                                            setRating(initial_rating);
                                            String error_message = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
            });
        }

        btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current_user != null) {
                    Intent delivey_intent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(delivey_intent);
                }
            }
        });

        btn_add_to_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current_user != null) {
                    if (PRODUCT_ALREADY_ADDED_TO_CARD) {
                        Toast.makeText(ProductDetailsActivity.this,"Already added to card",Toast.LENGTH_SHORT).show();
                        } else {
                        Map<String,Object> add_product = new HashMap<>();
                        add_product.put("product_ID_" + String.valueOf(DatabaseQueries.cardList.size()), product_id);
                        add_product.put("list_size", (long) (DatabaseQueries.cardList.size() + 1));

                        firebaseFirestore.collection("USERS").document(current_user.getUid()).collection("USER_DATA").document("MY_CARD")
                                .update(add_product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    if(DatabaseQueries.cardItemsModelList.size() != 0){
                                        DatabaseQueries.cardItemsModelList.add(new CardItemsModel(CardItemsModel.CART_ITEM, product_id
                                                , documentSnapshot.get("product_image_1").toString()
                                                , documentSnapshot.get("product_title").toString()
                                                , documentSnapshot.get("product_price").toString()
                                                , documentSnapshot.get("product_no_discount_price").toString()
                                                , (long) 1
                                        ));
                                    }
                                    PRODUCT_ALREADY_ADDED_TO_CARD = true;
                                    DatabaseQueries.cardList.add(product_id);
                                    Toast.makeText(ProductDetailsActivity.this,"Product Added to Card Successfully",Toast.LENGTH_SHORT).show();
                                    btn_add_to_card.setEnabled(false);
                                }
                                else{
                                    btn_add_to_card.setEnabled(true);
                                    String error_message = task.getException().getMessage();
                                    Toast.makeText(ProductDetailsActivity.this, error_message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        current_user = FirebaseAuth.getInstance().getCurrentUser();

        if(current_user != null) {
            if (DatabaseQueries.user_rating.size() == 0){
                DatabaseQueries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DatabaseQueries.wishList.size() == 0) {
                DatabaseQueries.loadWishList(ProductDetailsActivity.this,false);
            }
            if (DatabaseQueries.cardList.size() == 0) {
                DatabaseQueries.loadCardList(ProductDetailsActivity.this,false);
            }
        }

        if (DatabaseQueries.user_rated_id.contains(product_id)){
            int index = DatabaseQueries.user_rated_id.indexOf(product_id);
            initial_rating = Integer.parseInt(String.valueOf(DatabaseQueries.user_rating.get(index))) - 1;
            setRating(initial_rating);
        }

        if (DatabaseQueries.wishList.contains(product_id)){
            PRODUCT_ALREADY_ADDED_TO_WHISHLIST = true;
            btn_add_to_wishlist.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#fe6d73")));
        }
        else{
            PRODUCT_ALREADY_ADDED_TO_WHISHLIST = false;
        }

        if (DatabaseQueries.cardList.contains(product_id)) {
            PRODUCT_ALREADY_ADDED_TO_CARD = true;
        } else {
            PRODUCT_ALREADY_ADDED_TO_CARD = false;
        }

    }

    public static void setRating(int starPosition) {
        for (int x = 0; x < giving_star_to_the_product.getChildCount(); x++) {
            ImageView btn_star = (ImageView) giving_star_to_the_product.getChildAt(x);
            btn_star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
            if (x <= starPosition) {
                btn_star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffc107")));
            }
        }
    }

    private float AverageRatingCalculation(long current_user_rating){
        long total_stars = 0;
        for (int x = 1 ; x <= 5 ; x++){
            total_stars = total_stars + (long) documentSnapshot.get(x + "_star") * x;
        }
        total_stars = total_stars + current_user_rating;
        long calculate_average_rating = total_stars / ((long) documentSnapshot.get("total_ratings") + 1);
        return calculate_average_rating;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_basket_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // Handle action bar item clicks here.
        if(id==android.R.id.home){
            finish();
            return true;
        }else if(id==R.id.main2_search_icon){
            //search area
            return true;
        }else if(id==R.id.main2_basket_icon){
            //basket - card area
            //TODO: Intagrating card fragment code from MainActivity2
            return true;
            //gotoFragment("My Card",new MyCardFragment(),CARD_FRAGMENT);
        }
        return super.onOptionsItemSelected(item);
    }

}