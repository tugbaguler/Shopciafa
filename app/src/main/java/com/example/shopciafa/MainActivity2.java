package com.example.shopciafa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity2 extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

    private static final int RESULT_UPLOAD_IMAGE = 1;
    private AppBarConfiguration mAppBarConfiguration;

    private static final int HOME_FRAGMENT = 0;
    private static final int CARD_FRAGMENT = 1;
    private static final int MY_ORDERS_FRAGMENT = 2;
    private static final int MY_WISHLIST_FRAGMENT = 3;
    private static final int MY_ACCOUNT_FRAGMENT = 4;

    public static Boolean show_card = false;

    private ConstraintLayout constraintLayout;
    private ImageView action_bar_logo;
    private int current_fragment = -1;
    private NavigationView navigationView;

    public static DrawerLayout drawer;

    private FirebaseUser firebaseUser;
    private CircleImageView profile_image;
    private TextView email;
    private ImageView plus_icon;

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser current_user = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        action_bar_logo = findViewById(R.id.action_bar_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);;
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.getMenu().getItem(0).setChecked(true);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        constraintLayout = findViewById(R.id.constraintLayout_content_main);

        profile_image = navigationView.getHeaderView(0).findViewById(R.id.main2_user_profile_picture);
        email = navigationView.getHeaderView(0).findViewById(R.id.main2_user_email_address);
        plus_icon = navigationView.getHeaderView(0).findViewById(R.id.main2_add_profile_photo_btn);

        plus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromGallery();
            }
        });

        if (show_card) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Card", new MyCardFragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        if(current_user == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);
        }
        else{
            FirebaseFirestore.getInstance().collection("USERS").document(current_user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DatabaseQueries.email = task.getResult().getString("email");
                        DatabaseQueries.profile = task.getResult().getString("profile");

                        email.setText(DatabaseQueries.email);
                        if (DatabaseQueries.profile.equals("")){
                            plus_icon.setVisibility(View.INVISIBLE);
                        }else {
                            plus_icon.setVisibility(View.INVISIBLE);
                            Glide.with(MainActivity2.this).load(DatabaseQueries.profile).apply(new RequestOptions().placeholder(R.mipmap.user_dark2)).into(profile_image);
                        }
                    }else{
                        String error_message = task.getException().getMessage();
                        Toast.makeText(MainActivity2.this,error_message,Toast.LENGTH_SHORT).show();
                    }
                }
            });

            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            if(current_fragment == HOME_FRAGMENT){
                current_fragment = -1;
                super.onBackPressed();
            }
            else{
                if(show_card){
                    show_card = false;
                    finish();
                }
                else {
                    action_bar_logo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (current_fragment == HOME_FRAGMENT){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main_activity2, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here.
        int id= item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id==R.id.main2_search_icon){
            //search area
            return true;
        }else if(id==R.id.main2_notification_icon){
            //notification area
            return true;
        }else if(id==R.id.main2_basket_icon){
            //basket - card area
            gotoFragment("My Card",new MyCardFragment(),CARD_FRAGMENT);

            /*
            * Buradaki asıl amaç Product Details içindeki sepete de tıklanıldığı zaman ödeme ile ilgili card fragmenta yönlendirmek.
            * Fakat 134. satırdaki gotoFragment("My Card",new MyCardFragment(),CARD_FRAGMENT); sadece anasayfadaki sepet butonuna yönlendirme yapıtyor.
            * Hepsini tek bir yerden nasıl kontrol edebiliriz.

            Intent card_intent = new Intent(this,ProductDetailsActivity.class);
            show_card = true;
            startActivity(card_intent);
             */
            return true;
        }else if(id == android.R.id.home){
            if (show_card){
                show_card = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String fragment_title, Fragment fragment, int fragment_no) {
        action_bar_logo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(fragment_title);
        invalidateOptionsMenu();
        setFragment(fragment,fragment_no);
        if(fragment_no == MY_ORDERS_FRAGMENT){
            navigationView.getMenu().getItem(1).setChecked(true);
        }
        else if(fragment_no == CARD_FRAGMENT){
            //In the side menu; 3 elements of array (with index 2)
            navigationView.getMenu().getItem(2).setChecked(true);
        }else if(fragment_no == MY_WISHLIST_FRAGMENT){
            //In the side menu; 4 elements of array (with index 3)
            navigationView.getMenu().getItem(3).setChecked(true);
        }else if(fragment_no == MY_ACCOUNT_FRAGMENT){
            //In the side menu; 5 elements of array (with index 4)
            navigationView.getMenu().getItem(4).setChecked(true);
        }else{
            //In the side menu; 5 elements of array (with index 5)
            navigationView.getMenu().getItem(5).setChecked(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(current_user != null) {
            //Handle navigation view item clicks here
            int id = item.getItemId();
            if (id == R.id.main2_shopciafa) {
                action_bar_logo.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
                setFragment(new HomeFragment(), HOME_FRAGMENT);
            } else if (id == R.id.main2_my_orders) {
                gotoFragment("My Orders", new MyOrdersFragment(), MY_ORDERS_FRAGMENT);
            } else if (id == R.id.main2_my_card) {
                gotoFragment("My Card", new MyCardFragment(), CARD_FRAGMENT);
            } else if (id == R.id.main2_my_wishlist) {
                gotoFragment("My Wishlist", new MyWishlistFragment(), MY_WISHLIST_FRAGMENT);
            } else if (id == R.id.main2_my_account) {
                gotoFragment("My Account", new MyAccountFragment(), MY_ACCOUNT_FRAGMENT);
            } else if (id == R.id.main2_sign_out) {
                FirebaseAuth.getInstance().signOut();
                DatabaseQueries.clearData();
                Intent register_intent = new Intent(MainActivity2.this,RegisterActivity.class);
                startActivity(register_intent);
                finish();
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment, int fragment_no){
        if(fragment_no != current_fragment){
            current_fragment = fragment_no;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.replace(constraintLayout.getId(),fragment);
            fragmentTransaction.commit();
        }
    }

    private void chooseImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_UPLOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_UPLOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri select_Image = data.getData();
            profile_image.setImageURI(select_Image);
        }
    }
}

