package com.example.catovacka.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catovacka.R;
import com.example.catovacka.constants;
import com.example.catovacka.constructorChat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Chatwithperson extends Fragment {

    String phoneNumber;
    String meno;
    private View view;
    public static FirebaseAuth auth;
    private chatAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference link_into_message;
    private List<constructorChat> messages_list = new ArrayList<>();
    private ValueEventListener MessagesListener;

    public Chatwithperson(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String uid = "";
    private String result = "";

    private String getUID() {
        constants.link_into_data.child("phones").child(phoneNumber).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getKey().equals(phoneNumber)) {
                    uid = task.getResult().getValue().toString();
                    initRecView();
                }
            }
        });
        return uid;
    }

    private void foundmeno() {
        for (int i = 0; i < constants.list.size(); i++) {
            if (constants.list.get(i).getPhoneNumber().equals(phoneNumber)) {
                meno = constants.list.get(i).getUsername();
                break;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chatwithperson, container, false);
        ImageView img = view.findViewById(R.id.Send_message);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
            }
        });
        auth = FirebaseAuth.getInstance();
        return view;
    }

    private void sendmessage() {
        EditText edit = view.findViewById(R.id.Enter_message);
        String message = edit.getText().toString();
        if (!message.isEmpty()) {
            sendmessage1(message, phoneNumber);
            edit.setText("");
        }
    }

    private void sendmessage1(String message, String phoneNumber) {
        String messageKey = constants.link_into_data.child("message").child(auth.getCurrentUser().getUid()).child(uid).push().getKey();
        Map<String, Object> message_map = new HashMap<>();
        message_map.put("from", auth.getCurrentUser().getUid());
        message_map.put("text", message);
        constants.link_into_data.child("message").child(auth.getCurrentUser().getUid()).child(uid).child(messageKey).setValue(message_map);
        constants.link_into_data.child("message").child(uid).child(auth.getCurrentUser().getUid()).child(messageKey).setValue(message_map);
    }

    @Override
    public void onResume() {
        super.onResume();

        foundmeno();
        getActivity().findViewById(R.id.toolbar_info_chat).setVisibility(View.VISIBLE);
        TextView txt = getActivity().findViewById(R.id.phonenumber);
        txt.setText(meno);
        getUID();
    }

    private void initRecView() {
        recyclerView = view.findViewById(R.id.chat_view);
        adapter = new chatAdapter();
        recyclerView.setAdapter(adapter);
        messages_list = new ArrayList<>();
        constants.link_into_data.child("message").child(auth.getUid()).child(uid).addValueEventListener(new ValueEventListener() {
            int i = 0;
            String[] p = new String[2];
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messages_list = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        if (i < 2) {
                            if (i == 0) {
                            p[i]= ds1.getValue().toString();


                            }
                            if (i == 1) {
                                p[i] = ds1.getValue().toString();
                            }
                            i++;
                            if (i == 2) {
                                messages_list.add(new constructorChat(p[0],p[1]));
                                i = 0;
                            }
                        }
                    }
                }
                adapter.setList11(messages_list);
                recyclerView.smoothScrollToPosition(adapter.getItemCount());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.toolbar_info_chat).setVisibility(View.GONE);

    }
}