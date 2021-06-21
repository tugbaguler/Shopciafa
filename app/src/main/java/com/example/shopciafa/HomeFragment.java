package com.example.shopciafa;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.shopciafa.AgeGenderPredictionActivity.genderPredict;
import static com.example.shopciafa.DatabaseQueries.categoryModelList;
import static com.example.shopciafa.DatabaseQueries.female_lists;
import static com.example.shopciafa.DatabaseQueries.firebaseFirestore;
import static com.example.shopciafa.DatabaseQueries.lists;
import static com.example.shopciafa.DatabaseQueries.loadCategories;
import static com.example.shopciafa.DatabaseQueries.loadFemaleFragmentData;
import static com.example.shopciafa.DatabaseQueries.loadFragmentData;
import static com.example.shopciafa.DatabaseQueries.loadMaleFragmentData;
import static com.example.shopciafa.DatabaseQueries.loadedTheCategoryName;
import static com.example.shopciafa.DatabaseQueries.male_lists;
import static com.example.shopciafa.MainActivity2.current_user;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView home_page_recyclerView;
    private HomePageAdapter home_page_adapter;
    private ImageView no_internet_connection;
    private TextView check_internet_connection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //Layoutinflater takes XML file as input and creates View objects.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The inflate () method returns a view back. The first parameter of this method is the layout we want to be converted to java,
        // the second parameter is the ID of the layout element that we want to add this layout to, and the third parameter is the second parameter
        // and whether it will be added to the view, the parent of the inflated view, as true or false.
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        no_internet_connection = view.findViewById(R.id.no_internet_connection);
        check_internet_connection = view.findViewById(R.id.check_internet_connection);

        //internet connection check
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            //if there is internet connection
            //MainActivity2.drawer.setDrawerLockMode(0);
            no_internet_connection.setVisibility(View.GONE);
            check_internet_connection.setVisibility(View.GONE);

            categoryRecyclerView = view.findViewById(R.id.category_recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            categoryRecyclerView.setLayoutManager(layoutManager);

            categoryAdapter = new CategoryAdapter(categoryModelList);

            if (categoryModelList.size() == 0) {
                loadCategories(categoryAdapter, getContext());
            } else {
                //MainActivity2.drawer.setDrawerLockMode(1);
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);

            home_page_recyclerView = view.findViewById(R.id.home_page_recyclerView);
            LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
            testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            home_page_recyclerView.setLayoutManager(testingLayoutManager);


            // LISTING PRODUCTS BY GENDER ON THE HOME PAGE
            if (genderPredict != null && genderPredict.getText().toString() != "" && genderPredict.getText().toString().contains("Female")) {
                // Listing products by female
                if (female_lists.size() == 0) {
                    loadedTheCategoryName.add("HOME");
                    female_lists.add(new ArrayList<HomePageModel>());
                    home_page_adapter = new HomePageAdapter(female_lists.get(0));
                    loadFemaleFragmentData(home_page_adapter, getContext(), 0, "Home");
                    DatabaseQueries.pageIndex = 0;
                } else {
                    home_page_adapter = new HomePageAdapter(female_lists.get(0));
                    home_page_adapter.notifyDataSetChanged();
                }
                // Listing products by male
            } else if (genderPredict != null && genderPredict.getText().toString() != "" && genderPredict.getText().toString().contains("Male")) {
                if (male_lists.size() == 0) {
                    loadedTheCategoryName.add("HOME");
                    male_lists.add(new ArrayList<HomePageModel>());
                    home_page_adapter = new HomePageAdapter(male_lists.get(0));
                    loadMaleFragmentData(home_page_adapter, getContext(), 0, "Home");
                    DatabaseQueries.pageIndex = 0;
                } else {
                    home_page_adapter = new HomePageAdapter(male_lists.get(0));
                    home_page_adapter.notifyDataSetChanged();
                }
            } else {
                // Mixed products seen when users do not upload pictures to the application
                if (lists.size() == 0) {
                    //When you click the home icon, the name of the relevant page will appear on the application. This process will be valid for all categories.
                    loadedTheCategoryName.add("HOME");
                    lists.add(new ArrayList<HomePageModel>());
                    home_page_adapter = new HomePageAdapter(lists.get(0));
                    loadFragmentData(home_page_adapter, getContext(), 0, "Home");
                    DatabaseQueries.pageIndex = 0;
                } else {
                    home_page_adapter = new HomePageAdapter(lists.get(0));
                    home_page_adapter.notifyDataSetChanged();
                }
            }
            home_page_recyclerView.setAdapter(home_page_adapter);
        } else {
            // if there is not internet connection
            Glide.with(this).load(R.mipmap.no_internet_connection).into(no_internet_connection);
            no_internet_connection.setVisibility(View.VISIBLE);
            check_internet_connection.setVisibility(View.VISIBLE);
        }
        return view;
    }

}