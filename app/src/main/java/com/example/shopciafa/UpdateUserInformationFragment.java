package com.example.shopciafa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateUserInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateUserInformationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateUserInformationFragment() {
        // Required empty public constructor
    }

    private CircleImageView circleImageView;
    private Button btn_change_profile_photo;
    private Button btn_remove_profile_photo;
    private Button btn_update;
    private EditText email_address_area;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateUserInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateUserInformationFragment newInstance(String param1, String param2) {
        UpdateUserInformationFragment fragment = new UpdateUserInformationFragment();
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
        View view = inflater.inflate(R.layout.fragment_update_user_information, container, false);
        circleImageView = view.findViewById(R.id.circle_user_profile_image);
        btn_change_profile_photo = view.findViewById(R.id.btn_change_user_profile_photo);
        btn_remove_profile_photo = view.findViewById(R.id.btn_remove_user_profile_photo);
        btn_update = view.findViewById(R.id.btn_update_user_information);
        email_address_area = view.findViewById(R.id.editTextTextEmailAddress);

        String email = getArguments().getString("Email");
        String profile_photo = getArguments().getString("Photo");
        email_address_area.setText(email);
        Glide.with(getContext()).load(profile_photo).into(circleImageView);

        btn_change_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent predict_users_age_gender_intent = new Intent(getContext(),AgeGenderPredictionActivity.class);
                startActivity(predict_users_age_gender_intent);
                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent choose_photo_from_gallery_intent = new Intent(Intent.ACTION_PICK);
                        choose_photo_from_gallery_intent.setType("image/*");
                        startActivityForResult(choose_photo_from_gallery_intent, 1);
                    }else{
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                    }
                }else{
                    Intent choose_photo_from_gallery_intent = new Intent(Intent.ACTION_PICK);
                    choose_photo_from_gallery_intent.setType("image/*");
                    startActivityForResult(choose_photo_from_gallery_intent, 1);
                }
                 */
            }
        });

        btn_remove_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getContext()).load(R.mipmap.user_dark2).into(circleImageView);
            }
        });

        email_address_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmail();
            }
        });


        return view;
    }

    private void checkInputs(){
        //controls the user registration information
        if (!TextUtils.isEmpty(email_address_area.getText())){
            if(!TextUtils.isEmpty(email_address_area.getText())){
                btn_update.setEnabled(true);
            }else{
                btn_update.setEnabled(false);
            }
        }else{
            btn_update.setEnabled(false);
        }
    }

    private void checkEmail(){
        String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if(email_address_area.getText().toString().matches(EmailPattern)){
            //TODO
            // update email
        }else{
            email_address_area.setError("Invalid e-mail address! ");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == getActivity().RESULT_OK){
                if (data != null){
                    Uri uri = data.getData();
                    Glide.with(getContext()).load(uri).into(circleImageView);
                }else{
                    Toast.makeText(getContext(), "Image not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent choose_photo_from_gallery_intent = new Intent(Intent.ACTION_PICK);
                choose_photo_from_gallery_intent.setType("image/*");
                startActivityForResult(choose_photo_from_gallery_intent, 1);
            }else{
                Toast.makeText(getContext(),"Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}