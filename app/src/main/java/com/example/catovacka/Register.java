package com.example.catovacka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.catovacka.databinding.ActivityRegisterBinding;
import com.example.catovacka.fragments.EnterCode;
import com.example.catovacka.fragments.Enternumber;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        constants.link_into_storage = FirebaseStorage.getInstance().getReference();
        constants.link_into_data = FirebaseDatabase.getInstance().getReference();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainRegister, new Enternumber()).commit();
    }
}