package com.example.catovacka;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {


    public TextView meno;
    public ViewHolder(View itemView) {
        super(itemView);
        meno = itemView.findViewById(R.id.contact_layout_meno);
    }
    public void setMeno(String string) {
        meno.setText(string);
    }
}
