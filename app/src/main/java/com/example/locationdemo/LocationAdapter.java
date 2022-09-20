package com.example.locationdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{

    private ArrayList<HashMap<String,String>> locationArrayList;

    public LocationAdapter(ArrayList<HashMap<String, String>> locationArrayList) {
        this.locationArrayList = locationArrayList;
    }

    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        String location = locationArrayList.get(position).get(MainActivity.LATITUDE)+","+locationArrayList.get(position).get(MainActivity.LONGITUDE);
        holder.tvLocation.setText(location);
    }

    @Override
    public int getItemCount() {
        return locationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}
