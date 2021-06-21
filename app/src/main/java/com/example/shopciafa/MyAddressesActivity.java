package com.example.shopciafa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shopciafa.DeliveryActivity.SELECT_DELIVERY_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView addressesRecyclerView;
    private Button btn_delivery_here;
    private static AddressesAdapter addressesAdapter;
    private LinearLayout btn_add_new_address;
    private TextView number_of_saved_address;
    private int other_address_information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");

        addressesRecyclerView = findViewById(R.id.addresses_recyclerView);
        btn_delivery_here = findViewById(R.id.btn_deliver_here);
        btn_add_new_address = findViewById(R.id.btn_add_new_adress);
        number_of_saved_address = findViewById(R.id.how_many_address_saved_textView);
        other_address_information = DatabaseQueries.selected_address;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addressesRecyclerView.setLayoutManager(linearLayoutManager);
/*
        List<AddressesModel> addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Tuğba Güler","Akdeniz University Computer Engineering Depertmant","20150807012",true));
        addressesModelList.add(new AddressesModel("Mine Güler","Akdeniz University Computer Engineering Depertmant","20150904052",false));
        addressesModelList.add(new AddressesModel("Nadir Güler","Akdeniz University Computer Engineering Depertmant","20190802364",false));
        addressesModelList.add(new AddressesModel("Tuğçe Harmanda","Akdeniz University Computer Engineering Depertmant","20150915465",false));
*/
        int mode = getIntent().getIntExtra("MODE",-1);

        //Editing my addresses managing address
        if(mode == SELECT_DELIVERY_ADDRESS){
            btn_delivery_here.setVisibility(View.VISIBLE);
        }
        else{
            btn_delivery_here.setVisibility(View.GONE);
        }

        btn_delivery_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DatabaseQueries.selected_address != other_address_information){
                    final int previous_address_index = other_address_information;

                    Map<String, Object> update_selected_address = new HashMap<>();
                    update_selected_address.put("selected_" + String.valueOf(other_address_information + 1),false);
                    update_selected_address.put("selected_" + String.valueOf(DatabaseQueries.selected_address + 1),true);

                    other_address_information = DatabaseQueries.selected_address;

                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                            .document("MY_ADDRESS").update(update_selected_address).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                finish();
                            }else{
                                other_address_information = previous_address_index;
                                String error_message = task.getException().getMessage();
                                Toast.makeText(MyAddressesActivity.this,error_message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    finish();
                }
            }
        });

        addressesAdapter = new AddressesAdapter(DatabaseQueries.addressesModelList,mode);
        addressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)addressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();

        btn_add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_address_again_intent = new Intent(MyAddressesActivity.this,AddressActivity.class);
                add_address_again_intent.putExtra("INTENT","null");
                startActivity(add_address_again_intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        number_of_saved_address.setText(String.valueOf("Number of saved addresses : " + DatabaseQueries.addressesModelList.size()));
        /*
        int integer_number_of_saved_address = Integer.parseInt(String.valueOf(number_of_saved_address));
        if (integer_number_of_saved_address == 1){
            number_of_saved_address.setText(String.valueOf(DatabaseQueries.addressesModelList.size()) + "address is saved");
        }else{
            number_of_saved_address.setText(String.valueOf(DatabaseQueries.addressesModelList.size()) + "addresses are saved");
        }
        */
    }

    public static void refreshingTheItem(int deselected, int selected){
        addressesAdapter.notifyItemChanged(deselected);
        addressesAdapter.notifyItemChanged(selected);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if (DatabaseQueries.selected_address != other_address_information){
                DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).setSelected(false);
                DatabaseQueries.addressesModelList.get(other_address_information).setSelected(true);
                DatabaseQueries.selected_address = other_address_information;
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (DatabaseQueries.selected_address != other_address_information){
            DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).setSelected(false);
            DatabaseQueries.addressesModelList.get(other_address_information).setSelected(true);
            DatabaseQueries.selected_address = other_address_information;
        }
        super.onBackPressed();
    }
}