package com.example.shopciafa;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyCardFragment() {
        // Required empty public constructor
    }

    private RecyclerView card_items_recyclerView;
    private Button btn_continue;
    private TextView total_amount;
    public static CardAdapter cardAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCardFragment newInstance(String param1, String param2) {
        MyCardFragment fragment = new MyCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_card, container, false);
        card_items_recyclerView = view.findViewById(R.id.card_items_recyclerView);
        total_amount = view.findViewById(R.id.total_card_amount_textView);
        btn_continue = view.findViewById(R.id.btn_card_continue);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        card_items_recyclerView.setLayoutManager(layoutManager);

        if(DatabaseQueries.cardItemsModelList.size() == 0){
            DatabaseQueries.cardList.clear();
            DatabaseQueries.loadCardList(getContext(), true);
        }
        /*
        List<CardItemsModel> cardItemsModelList = new ArrayList<>();
        cardItemsModelList.add(new CardItemsModel(1,"Price (3 items)","250$","Free","250$"));
        cardItemsModelList.add(new CardItemsModel(0,R.mipmap.deal_product_1,"US Polo Assn Sneakers","59.90$","75.50$",1));
        cardItemsModelList.add(new CardItemsModel(0,R.mipmap.deal_product_1,"US Polo Assn Sneakers","59.90$","75.50$",3));
        cardItemsModelList.add(new CardItemsModel(0,R.mipmap.deal_product_1,"US Polo Assn Sneakers","59.90$","75.50$",1));
        cardItemsModelList.add(new CardItemsModel(1,"Price (3 items)","250$","Free","250$"));
        */

        cardAdapter = new CardAdapter(DatabaseQueries.cardItemsModelList,total_amount,true);
        card_items_recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseQueries.loadAddress(getContext());
            }
        });
        return view;
    }
}