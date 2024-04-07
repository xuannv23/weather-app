package com.example.myappweather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myappweather.Model.Future;
import com.example.myappweather.R;

import java.util.ArrayList;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.viewHolder> {

    ArrayList<Future> items;
    Context context;

    public FutureAdapter(ArrayList<Future> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public FutureAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View listItem =layoutInflater.inflate(R.layout.item_future, parent, false);
        return new FutureAdapter.viewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureAdapter.viewHolder holder, int position) {
        holder.dayTxt.setText(items.get(position).getDay());
        holder.status.setText(items.get(position).getStatus());
        holder.hightTxt.setText(items.get(position).getHightTemp()+"°C");
        holder.lowTxt.setText(items.get(position).getLowTemp()+"°C");
        int drawableResourceId = holder.itemView.getResources().getIdentifier(items.get(position).getPicPath(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context).load(drawableResourceId).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder  extends RecyclerView.ViewHolder{
        TextView dayTxt, status, hightTxt, lowTxt;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dayTxt = itemView.findViewById(R.id.dayTxt);
            status = itemView.findViewById(R.id.statusTxt);
            hightTxt = itemView.findViewById(R.id.hightTxt);
            lowTxt = itemView.findViewById(R.id.lowTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
