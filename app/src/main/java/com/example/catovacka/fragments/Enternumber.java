package com.example.catovacka.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.catovacka.MainActivity;
import com.example.catovacka.R;
import com.example.catovacka.Register;
import com.example.catovacka.constants;
import com.example.catovacka.countries;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;


public class Enternumber extends Fragment {
    private View view;
    private EditText txt;
    private String p;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth auth;
    private FragmentTransaction ft;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_enter_number, container, false);
        txt = view.findViewById(R.id.entermobil);
        countries.pridanie();
        Button button = view.findViewById(R.id.dalej);
        button.setOnClickListener(v -> sendcode());
        spiner();
        return view;
    }

    private void sendcode() {
        if (txt.getText().toString().isEmpty() || txt.getText().toString().equals(p)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Warning");
            builder.setMessage("Enter correct number");
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            constants.cislo = txt.getText().toString();
            ft = getParentFragmentManager().beginTransaction();
            authuser();
        }
    }

    private void authuser() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(constants.cislo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void spiner() {
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, countries.list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                p = "";
                for (int i = item.lastIndexOf("+"); i < item.length(); i++) {
                    p = p + item.charAt(i);
                }
                txt.setText(p);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                  ft.replace(R.id.mainRegister, new EnterCode(constants.cislo, s), "NewFragmentTag");
                ft.addToBackStack(null);
                ft.commit();
            }
        };
    }
}