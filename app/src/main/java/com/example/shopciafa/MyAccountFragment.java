package com.example.shopciafa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton btn_settings;
    private CircleImageView profile_image;
    private TextView email;
    private ImageView plus_icon;
    private Button btn_view_all_address_information;
    private Button btn_sign_out;
    private TextView buyer_name;
    private TextView address;
    private TextView pincode;

    public static final int MANAGE_ADDRESS = 1;
    //private static final int RESULT_UPLOAD_IMAGE = 1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        profile_image = view.findViewById(R.id.profile_image);
        email = view.findViewById(R.id.user_email_address);
        email.setText(DatabaseQueries.email);
        if (!DatabaseQueries.profile.equals("")){
            Glide.with(getContext()).load(DatabaseQueries.profile).apply(new RequestOptions().placeholder(R.mipmap.user)).into(profile_image);
        }

        // TODO:
        //  Adding product delivery stages to the database

        btn_settings = view.findViewById(R.id.floatingActionButton_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update_user_information_intent = new Intent(getContext(),UpdateUserInformationActivity.class);
                update_user_information_intent.putExtra("Email",email.getText());
                update_user_information_intent.putExtra("Profile",DatabaseQueries.profile);
                startActivity(update_user_information_intent);
            }
        });

        btn_view_all_address_information = view.findViewById(R.id.btn_view_all_address_information);
        btn_view_all_address_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adresses_intent = new Intent(getContext(),MyAddressesActivity.class);
                adresses_intent.putExtra("MODE",MANAGE_ADDRESS);
                startActivity(adresses_intent);
            }
        });

        buyer_name = view.findViewById(R.id.address_buyer_name_surname);
        address = view.findViewById(R.id.address_delivery_address);
        pincode = view.findViewById(R.id.address_pincode);
        if (DatabaseQueries.addressesModelList.size() != 0){
            buyer_name.setText(DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).getFullname());
            address.setText(DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).getAddress());
            pincode.setText(DatabaseQueries.addressesModelList.get(DatabaseQueries.selected_address).getPincode());
        }

        btn_sign_out = view.findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                DatabaseQueries.clearData();
                Intent sign_out_intent = new Intent(getContext(),RegisterActivity.class);
                startActivity(sign_out_intent);
                getActivity().finish();
            }
        });

        return view;
    }

}