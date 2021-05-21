package com.example.cardiocheck2;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder2 extends RecyclerView.ViewHolder {
    TextView mNameelements2, mLinkelements2;
    Button mDownloadelements2;
    public MyViewHolder2(@NonNull View itemView) {
        super(itemView);
        mNameelements2 = itemView.findViewById(R.id.textnewname);
        mLinkelements2 = itemView.findViewById(R.id.textnewlink);
        mDownloadelements2 = itemView.findViewById(R.id.textnewdownload);
    }
}
