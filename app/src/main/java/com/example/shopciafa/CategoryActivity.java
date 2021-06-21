package com.example.shopciafa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.shopciafa.AgeGenderPredictionActivity.genderPredict;
import static com.example.shopciafa.DatabaseQueries.female_lists;
import static com.example.shopciafa.DatabaseQueries.lists;
import static com.example.shopciafa.DatabaseQueries.loadFemaleFragmentData;
import static com.example.shopciafa.DatabaseQueries.loadFragmentData;
import static com.example.shopciafa.DatabaseQueries.loadMaleFragmentData;
import static com.example.shopciafa.DatabaseQueries.loadedTheCategoryName;
import static com.example.shopciafa.DatabaseQueries.male_lists;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView category_recyclerView;
    private HomePageAdapter home_page_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // The name of each category will appear in the toolbar.
        String title = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(title);
        // Returning to a Sub-Activity on Android Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        category_recyclerView = findViewById(R.id.category_recyclerView);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        category_recyclerView.setLayoutManager(testingLayoutManager);

        int position_of_the_list = 0;
        for(int i = 0; i < loadedTheCategoryName.size(); i++){
            // When the word Fashion is used with the toUpperCase method, it is written as FASHION and a Turkish character problem occurs.
            // Since it is used as FASHION in the database, this code was written to remove the error.
            String target = title.toUpperCase();
            Character harf = 'İ';
            if(target.contains("İ")) {
                target = target.replace(harf, 'I');
            }
            if(loadedTheCategoryName.get(i).equals(target)){
                position_of_the_list = i;
            }
        }
        if (position_of_the_list == 0){
            String target = title.toUpperCase();
            Character harf = 'İ';
            if(target.contains("İ")) {
                target = target.replace(harf, 'I');
            }
            loadedTheCategoryName.add(target);
            lists.add(new ArrayList<HomePageModel>());
            home_page_adapter = new HomePageAdapter(lists.get(loadedTheCategoryName.size() - 1));
            loadFragmentData(home_page_adapter,this,loadedTheCategoryName.size() - 1,title);
            DatabaseQueries.pageIndex = 0;
        }
        else {
            home_page_adapter = new HomePageAdapter(lists.get(position_of_the_list));
            loadFragmentData(home_page_adapter,this,position_of_the_list,title);
            DatabaseQueries.pageIndex = position_of_the_list;
        }

        if (genderPredict != null && genderPredict.getText().toString() != "") {
            if (position_of_the_list == 0) {
                String target = title.toUpperCase();
                Character harf = 'İ';
                if (target.contains("İ")) {
                    target = target.replace(harf, 'I');
                }
                if (genderPredict != null && genderPredict.getText().toString() != "" && genderPredict.getText().toString().contains("Female")) {
                    loadedTheCategoryName.add(target);
                    female_lists.add(new ArrayList<HomePageModel>());
                    home_page_adapter = new HomePageAdapter(female_lists.get(loadedTheCategoryName.size() - 1));
                    loadFemaleFragmentData(home_page_adapter, this, loadedTheCategoryName.size() - 1, title);
                    DatabaseQueries.pageIndex = 0;
                } else {
                    home_page_adapter = new HomePageAdapter(female_lists.get(position_of_the_list));
                    loadFemaleFragmentData(home_page_adapter, this, position_of_the_list, title);
                    DatabaseQueries.pageIndex = position_of_the_list;
                }
                if (genderPredict != null && genderPredict.getText().toString() != "" && genderPredict.getText().toString().contains("Male")) {
                    loadedTheCategoryName.add(target);
                    male_lists.add(new ArrayList<HomePageModel>());
                    home_page_adapter = new HomePageAdapter(male_lists.get(loadedTheCategoryName.size() - 1));
                    loadMaleFragmentData(home_page_adapter, this, loadedTheCategoryName.size() - 1, title);
                    DatabaseQueries.pageIndex = 0;
                } else {
                    home_page_adapter = new HomePageAdapter(male_lists.get(position_of_the_list));
                    loadMaleFragmentData(home_page_adapter, this, position_of_the_list, title);
                    DatabaseQueries.pageIndex = position_of_the_list;
                }
            }
        }
        category_recyclerView.setAdapter(home_page_adapter);
        home_page_adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // To place menu items in the Action Bar, your Activity's callback method named onCreateOptionsMenu () is used.
        // With this method, the menu source given in the Menu object becomes ready for display (inflate).
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // When the user clicks on the action buttons or other items in the overflow actions pane, the system calls your Activity's callback method named onOptionsItemSelected ().
        // The getItemId () method is called over the MenuItem provided by the method, and thus the item the user pressed is determined.
        int id= item.getItemId();

        if(id==R.id.main2_search_icon){
            //search area
            return true;
        }else if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}