package com.example.shopciafa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView doYouHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email;
    private EditText password;
    private EditText confirm_password;

    private ImageButton btn_close;
    private Button btn_sign_up;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);

        doYouHaveAnAccount = view.findViewById(R.id.sign_up_do_you_have_an_account);
        email = view.findViewById(R.id.sign_up_email);
        password = view.findViewById(R.id.sign_up_password);
        confirm_password = view.findViewById(R.id.sign_up_comfirm_password);

        btn_close = view.findViewById(R.id.sign_up_btn_cross);
        btn_sign_up = view.findViewById(R.id.btn_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doYouHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_intent();
            }
        });

        //EditText uses TextWatcher interface to watch change made over EditText. For doing this, EditText calls the addTextChangedListener() method.
        //Tracks changes made to user information
        email.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
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
        confirm_password.addTextChangedListener(new TextWatcher() {
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

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send users data to firebase
                checkEmailAndPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        //animation running while the user is directed to the sign in page if the user has a registered account
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left_side,R.anim.slideout_from_right_side);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs(){
        //controls the user registration information
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(password.getText()) && password.length() >= 6){
                if(!TextUtils.isEmpty(confirm_password.getText())){
                    btn_sign_up.setEnabled(true);
                }else{
                    btn_sign_up.setEnabled(false);
                }
            }else{
                btn_sign_up.setEnabled(false);
            }
        }else{
            btn_sign_up.setEnabled(false);
        }
    }

    private void checkEmailAndPassword(){
        if(email.getText().toString().matches(EmailPattern)){
            if (password.getText().toString().equals(confirm_password.getText().toString())){

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    Map<String,Object> user_data = new HashMap<>();
                                    user_data.put("email",email.getText().toString());
                                    user_data.put("profile","");

                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(user_data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){

                                                        CollectionReference user_data_reference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                        // Maps
                                                        Map<String,Object> wishlist_map = new HashMap<>();
                                                        wishlist_map.put("list_size", (long) 0);

                                                        Map<String,Object> rating_map = new HashMap<>();
                                                        rating_map.put("list_size", (long) 0);

                                                        Map<String,Object> card_map = new HashMap<>();
                                                        card_map.put("list_size", (long) 0);

                                                        Map<String,Object> address_map = new HashMap<>();
                                                        address_map.put("list_size", (long) 0);

                                                        //Map<String,Object> profile_map = new HashMap<>();
                                                        //profile_map.put("list_size", (long) 0);


                                                        final List<String> document_names = new ArrayList<>();
                                                        document_names.add("MY_WISHLIST");
                                                        document_names.add("MY_RATINGS");
                                                        document_names.add("MY_CARD");
                                                        document_names.add("MY_ADDRESS");
                                                        //document_names.add("MY_PROFILE");

                                                        List<Map<String,Object>> document_fields = new ArrayList<>();
                                                        document_fields.add(wishlist_map);
                                                        document_fields.add(rating_map);
                                                        document_fields.add(card_map);
                                                        document_fields.add(address_map);
                                                        //document_fields.add(profile_map);

                                                        for (int x = 0 ; x < document_names.size() ; x++){
                                                            final int finalX = x;
                                                            user_data_reference.document(document_names.get(x)).set(document_fields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()){
                                                                        if(finalX == document_names.size()-1) {
                                                                            main_intent();
                                                                        }
                                                                    }
                                                                    else{
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }else{
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                confirm_password.setError("Password doesn't matched!");
            }
        }else{
            email.setError("Invalid e-mail address! ");
        }
    }

    private void main_intent(){
        Intent main_intent = new Intent(getActivity(),MainActivity2.class);
        startActivity(main_intent);
        getActivity().finish();
    }

}