package com.example.shopciafa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        SystemClock.sleep(1000);
        Intent register_intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(register_intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser current_user = firebaseAuth.getCurrentUser();
        if (current_user == null){
            Intent register_intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(register_intent);
            finish();
        }else{
            Intent main_intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(main_intent);
            finish();

        }
    }
}