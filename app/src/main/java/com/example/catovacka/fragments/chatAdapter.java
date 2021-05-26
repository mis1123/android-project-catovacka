package com.example.catovacka.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catovacka.R;

import org.jetbrains.annotations.NotNull;

import com.example.catovacka.constructorChat;

import java.util.ArrayList;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.Holder> {
    static List<constructorChat> messages = new ArrayList<>();
    static List<constructorChat> listochek = new ArrayList<>();
    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message, parent, false);
        return new Holder(view);
    }
    public void setList()
    {
        messages = new ArrayList<>();
        messages = listochek;
        notifyDataSetChanged();
    }
    public void setList11(List<constructorChat> list)
    {
        listochek = new ArrayList<>();
        listochek = list;
        setList();
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        if(messages.get(position).getText().equals(Chatwithperson.auth.getUid()))
        {
            holder.usermessage.setVisibility(View.VISIBLE);
            holder.opponentmessage.setVisibility(View.GONE);
            holder.chatusermesage.setText(messages.get(position).getFrom());
        }
        else {
            holder.usermessage.setVisibility(View.GONE);
            holder.opponentmessage.setVisibility(View.VISIBLE);
            holder.chatopponnentmesage.setText(messages.get(position).getFrom());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ConstraintLayout usermessage;
        TextView chatusermesage;
        ConstraintLayout opponentmessage;
        TextView chatopponnentmesage;

        public Holder(View view) {
            super(view);
            //user
            usermessage = view.findViewById(R.id.userMessage);
            chatusermesage = view.findViewById(R.id.messageuserofphone);
            //opponent
            opponentmessage = view.findViewById(R.id.opponntMessage);
            chatopponnentmesage = view.findViewById(R.id.messageuseropponent);
        }
    }

}
