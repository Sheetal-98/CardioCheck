package com.example.cardiocheck2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyViewHolder2> {

    Loadecgimages2 loadecgimages2;
    ArrayList<DownModel2> downModel2s;

    public MyAdapter2(Loadecgimages2 loadecgimages2, ArrayList<DownModel2> downModel2s) {
        this.loadecgimages2 = loadecgimages2;
        this.downModel2s = downModel2s;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(loadecgimages2.getBaseContext());
        View view = layoutInflater.inflate(R.layout.elements2, null, false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        holder.mNameelements2.setText(downModel2s.get(position).getName2());
        holder.mLinkelements2.setText(downModel2s.get(position).getLink2());

    }

    @Override
    public int getItemCount() {
        return downModel2s.size();
    }
}
