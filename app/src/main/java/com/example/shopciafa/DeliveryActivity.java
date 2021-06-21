package com.example.shopciafa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView delivery_recyclerView;
    private Button btn_add_or_change_new_address;
    private TextView total_amount;
    private TextView full_name;
    private TextView delivery_address;
    private TextView pincode;
    public static final int SELECT_DELIVERY_ADDRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        delivery_recyclerView = findViewById(R.id.delivery_recyclerView);
        btn_add_or_change_new_address = findViewById(R.id.btn_change_or_add_address);
        total_amount = findViewById(R.id.total_card_amount_textView);
        full_name = findViewById(R.id.full_name);
        delivery_address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        delivery_recyclerView.setLayoutManager(layoutManager);

        //List<CardItemsModel> cardItemsModelList = new ArrayList<>();
        //cardItemsModelList.add(new CardItemsModel(0,R.mipmap.deal_product_1,"US Polo Assn Sneakers","59.90$","75.50$",1));
        //cardItemsModelList.add(new CardItemsModel(0,R.mipmap.deal_product_1,"US Polo Assn Sneakers","59.90$","75.50$",3));
        //cardItemsModelList.add(new CardItemsModel(0,R.mipmap.deal_product_1,"US Polo Assn Sneakers","59.90$","75.50$",1));
        //cardItemsModelList.add(new CardItemsModel(1,"Price (3 items)","250$","Free","250$"));

        CardAdapter cardAdapter = new CardAdapter(DatabaseQueries.cardItemsModelList,total_amount,false);
        delivery_recyclerView.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();

        btn_add_or_change_new_address.setVisibility(View.VISIBLE);
        btn_add_or_change_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adresses_intent = new Intent(DeliveryActivity.this,MyAddressesActivity.class);
                adresses_intent.putExtra("MODE",SELECT_DELIVERY_ADDRESS);
                startActivity(adresses_intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        full_name.setText(DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).getFullname());
        delivery_address.setText(DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).getAddress());
        pincode.setText(DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).getPincode());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}