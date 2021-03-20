package com.example.cardiocheck2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Loadecgimages loadEcgImages;
    ArrayList<DownModel> downModels;

    public MyAdapter(Loadecgimages loadEcgImages, ArrayList<DownModel> downModels) {
        this.loadEcgImages = loadEcgImages;
        this.downModels = downModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(loadEcgImages.getBaseContext());
        View view = layoutInflater.inflate(R.layout.elements, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.mNameelements.setText(downModels.get(position).getName());
        holder.mLinkelements.setText(downModels.get(position).getLink());

    }

    @Override
    public int getItemCount() {
        return downModels.size();
    }
}
