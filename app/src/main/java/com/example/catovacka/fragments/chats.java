package com.example.catovacka.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.catovacka.R;
import com.example.catovacka.databinding.ActivityMainBinding;
import com.example.catovacka.databinding.FragmentChats2Binding;

public class chats extends Fragment {

    private FragmentChats2Binding aBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        aBinding = FragmentChats2Binding.inflate(getLayoutInflater());
        return aBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}