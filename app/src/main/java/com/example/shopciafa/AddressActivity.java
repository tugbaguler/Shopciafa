package com.example.shopciafa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private EditText city;
    private EditText street;
    private EditText flat_no;
    private EditText pincode;
    private EditText buyer_name;
    private EditText phone_number;
    private EditText alternative_phone_number;
    private Spinner state_spinner;
    private Button btn_save;

    private String [] state_list;
    private String selected_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new address");

        city = findViewById(R.id.city_editText);
        street = findViewById(R.id.street_editText);
        flat_no = findViewById(R.id.flatno_buildingname_editText);
        pincode = findViewById(R.id.pincode_editText);
        state_spinner = findViewById(R.id.state_spinner);
        buyer_name = findViewById(R.id.buyer_name_editText);
        phone_number = findViewById(R.id.phone_editText);
        alternative_phone_number = findViewById(R.id.alternate_phone_editText);
        btn_save = findViewById(R.id.btn_save);

        //Spinner Area
        state_list = getResources().getStringArray(R.array.Turkey_states);
        ArrayAdapter spinner_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,state_list);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(spinner_adapter);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_state = state_list[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(city.getText())){
                    if (!TextUtils.isEmpty(street.getText())){
                        if (!TextUtils.isEmpty(flat_no.getText())){
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 5){ // pincode for Antalya 07000
                                if (!TextUtils.isEmpty(buyer_name.getText())){
                                    if (!TextUtils.isEmpty(phone_number.getText()) && phone_number.getText().length() == 10){

                                        Map<String, Object> add_user_addresses = new HashMap();
                                        String delivery_address = city.getText().toString() + " " + street.getText().toString() + " " + flat_no.getText().toString();
                                        // Data to shown in addresses_items.xml
                                        add_user_addresses.put("list_size", Long.parseLong(String.valueOf(DatabaseQueries.addressesModelList.size() + 1)));
                                        add_user_addresses.put("fullname_" + Long.parseLong(String.valueOf(DatabaseQueries.addressesModelList.size() + 1)), buyer_name.getText().toString());
                                        add_user_addresses.put("address_" + Long.parseLong(String.valueOf(DatabaseQueries.addressesModelList.size() + 1)), delivery_address);
                                        add_user_addresses.put("pincode_" + Long.parseLong(String.valueOf(DatabaseQueries.addressesModelList.size() + 1)), pincode.getText().toString());
                                        add_user_addresses.put("selected_" + Long.parseLong(String.valueOf(DatabaseQueries.addressesModelList.size() + 1)), true);
                                        if (DatabaseQueries.addressesModelList.size() > 0) {
                                            add_user_addresses.put("selected_" + (DatabaseQueries.selected_address + 1), false);
                                        }
                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESS")
                                                .update(add_user_addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    DatabaseQueries.addressesModelList.add(new AddressesModel(buyer_name.getText().toString()
                                                            ,city.getText().toString() + street.getText().toString() + flat_no.getText().toString()
                                                            , pincode.getText().toString()
                                                            ,true
                                                    ));
                                                    if (DatabaseQueries.addressesModelList.size() > 0) {
                                                        DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).setSelected(false);
                                                    }
                                                    if (getIntent().getStringExtra("INTENT").equals("delivery_intent")) {
                                                        Intent delivery_intent = new Intent(AddressActivity.this, DeliveryActivity.class);
                                                        startActivity(delivery_intent);
                                                    }else{
                                                        MyAddressesActivity.refreshingTheItem(DatabaseQueries.selected_address,DatabaseQueries.addressesModelList.size() - 1);
                                                    }
                                                    DatabaseQueries.selected_address = DatabaseQueries.addressesModelList.size() - 1;
                                                    finish();
                                                }else{
                                                    String error_message = task.getException().getMessage();
                                                    Toast.makeText(AddressActivity.this,error_message,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        phone_number.requestFocus();
                                    }
                                }else{
                                    buyer_name.requestFocus();
                                }
                            }else{
                                pincode.requestFocus();
                            }
                        }else{
                            flat_no.requestFocus();
                        }
                    }else{
                        street.requestFocus();
                    }
                }else{
                    city.requestFocus();
                }
            }
        });
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