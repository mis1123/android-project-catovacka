package com.example.catovacka;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class constants {
    public static String meno = "";
    public static String cislo = "";
    public static String icon = "";
    public static DatabaseReference link_into_data;
    public static StorageReference link_into_storage;
    public static List<constructorOfContacts> list = new ArrayList<>();
}
