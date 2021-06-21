package com.example.shopciafa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

public class UpdateUserInformationActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private UpdateUserInformationFragment updateUserInformationFragment;
    private UpdatePasswordFragment updatePasswordFragment;
    private String email;
    private String profile_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_information);

        tabLayout = findViewById(R.id.user_information_tab_layout);
        frameLayout = findViewById(R.id.user_information_frame_layout);

        updateUserInformationFragment = new UpdateUserInformationFragment();
        updatePasswordFragment = new UpdatePasswordFragment();

        email = getIntent().getStringExtra("Email");
        profile_photo = getIntent().getStringExtra("Photo");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    setUpdateUserInformationFragment(updateUserInformationFragment,true);
                }
                if (tab.getPosition() == 1){
                    setUpdateUserInformationFragment(updatePasswordFragment,false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).select();
        setUpdateUserInformationFragment(updateUserInformationFragment,true);

    }

    private void setUpdateUserInformationFragment(Fragment user_info_fragment, boolean setBundle){
        // A FragmentManager manages Fragments in Android, specifically it handles transactions between fragments. A transaction is a way to add, replace, or remove fragments.
        // A FragmentTransaction gives an interface for interacting with fragments.
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        if (setBundle) {
            Bundle bundle = new Bundle();
            bundle.putString("Email", email);
            bundle.putString("Photo", profile_photo);
            user_info_fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(frameLayout.getId(),user_info_fragment);
        fragmentTransaction.commit();
    }
}