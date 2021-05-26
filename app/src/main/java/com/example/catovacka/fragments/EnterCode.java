package com.example.catovacka.fragments;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.catovacka.MainActivity;
import com.example.catovacka.Register;
import com.example.catovacka.constants;
import com.example.catovacka.R;
import com.example.catovacka.databinding.FragmentEnterCodeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.markers.KMutableMap;

public class EnterCode extends Fragment {
    private FragmentEnterCodeBinding binding;
    private static String phonenumber;
    public static String code;
    public static String id;
    private FirebaseAuth auth;

    public EnterCode(String s, String s1) {
        this.phonenumber = s;
        this.id = s1;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.codeEntered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = binding.codeEntered.getText().toString();
                if (code.length() == 6) {
                    kontrola();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void kontrola() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, code);
        auth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> info_to_databse = new HashMap<>();
                    info_to_databse.put("ID", auth.getCurrentUser().getUid());
                    info_to_databse.put("NUMBER", phonenumber);
                    info_to_databse.put("USER", "undefined");
                    constants.link_into_data.child("phones").child(phonenumber).setValue(auth.getCurrentUser().getUid())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    constants.link_into_data.child("users").child(auth.getCurrentUser().getUid()).updateChildren(info_to_databse)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    if (task.isSuccessful()) {
                                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "porblem", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEnterCodeBinding.inflate(getLayoutInflater());
        binding.codetext.setText("Čatovač send you SMS with code on " + constants.cislo);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }
}