package com.example.doodletask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;

   List<MyListData> data;


    public MyListAdapter(List<MyListData> data,Context context) {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewHolder myHolder= (ViewHolder) holder;

        myHolder.rank.setText(data.get(position).getRank());
        myHolder.name.setText(data.get(position).getName());
        myHolder.price.setText(data.get(position).getPriceUsd());
        myHolder.change.setText(data.get(position).getChangePercent24Hr());

        String Url = data.get(position).getSymbol();

        Log.e("---------------------image url---------------",""+Url);


        Glide.with(context)
                .load(Url)
                .thumbnail(Glide.with(context).load(R.drawable.placeholder))
                .into(myHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView rank,name,price,change;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.image_symbol);
            rank =  itemView.findViewById(R.id.txt_rank);
            name =  itemView.findViewById(R.id.txt_name);
            price =  itemView.findViewById(R.id.txt_price);
            change =  itemView.findViewById(R.id.txt_change);

        }
    }
}