package com.example.catovacka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.catovacka.R;
import com.example.catovacka.constants;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class settings extends Fragment{


    private static final int RESULT_CANCELED = 0 ;
    private View view;
    private FirebaseAuth auth;
    CircleImageView crcl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        TextView txt2 = view.findViewById(R.id.menozapisane);
        txt2.setText(constants.meno);
        auth = FirebaseAuth.getInstance();
        TextView txt = view.findViewById(R.id.telefone_cislo_zapisane);
        txt.setText(constants.cislo);
        CircleImageView crcl = view.findViewById(R.id.profile_image);
        return view;
    }



}