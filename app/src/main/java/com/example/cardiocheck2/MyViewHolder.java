package com.example.cardiocheck2;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView mNameelements, mLinkelements;
    Button mDownloadelements;
    public MyViewHolder(@NonNull View itemView) {

        super(itemView);

        mNameelements=itemView.findViewById(R.id.nametextView);
        mLinkelements=itemView.findViewById(R.id.linktextView);
        mDownloadelements=itemView.findViewById(R.id.downloadElements);


    }
}
