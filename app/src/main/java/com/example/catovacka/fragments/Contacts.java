package com.example.catovacka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catovacka.MainActivity;
import com.example.catovacka.ViewHolder;
import com.example.catovacka.constants;
import com.example.catovacka.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.example.catovacka.constructorOfContacts;

public class Contacts extends Fragment {
    private View view;
    private static FirebaseAuth auth;

    private RecyclerView Recview;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference ref;
    private FragmentTransaction ft;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
        auth = FirebaseAuth.getInstance();
        kontrolaDb();
        ft = getParentFragmentManager().beginTransaction();
        return view;
    }
    public static void kontrolaDb()
    {
        constants.link_into_data.child("phones").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    for (int i = 0; i < constants.list.size(); i++) {
                        constructorOfContacts cs = constants.list.get(i);
                        if(cs.getPhoneNumber().equals(child.getKey()))
                        {
                            constants.link_into_data.child("phones_contacts").child(auth.getUid()).child(child.getValue().toString()).child("phoneNumber")
                                    .setValue(child.getKey().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    System.out.println("zapisane");
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Chats");
        initRecView();
    }

    private void initRecView() {
        Recview = view.findViewById(R.id.contacts_view);
        ref = constants.link_into_data.child("phones_contacts").child(auth.getUid());
        FirebaseRecyclerOptions<constructorOfContacts> options =
                new FirebaseRecyclerOptions.Builder<constructorOfContacts>()
                        .setQuery(ref, constructorOfContacts.class)
                        .build();
       adapter = new FirebaseRecyclerAdapter<constructorOfContacts,ViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull constructorOfContacts model) {
                holder.setMeno(model.getPhoneNumber());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ft.replace(R.id.maininfo, new Chatwithperson(model.getPhoneNumber()), "NewFragmentTag");
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
           }


           @NonNull
           @Override
           public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view1 = getLayoutInflater().from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
               return new ViewHolder(view1);
           }
       };
        Recview.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();
    }
}