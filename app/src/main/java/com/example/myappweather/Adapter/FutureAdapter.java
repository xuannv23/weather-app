package com.example.myappweather.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myappweather.Activities.FutureActivity;
import com.example.myappweather.Activities.MainActivity;
import com.example.myappweather.Model.Future;
import com.example.myappweather.R;
import com.squareup.picasso.Picasso;

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
        Picasso.get().load(items.get(position).getPicPath()).into(holder.pic);
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder  extends RecyclerView.ViewHolder{
        TextView dayTxt, status, hightTxt, lowTxt;
        ImageView pic;
        LinearLayout click;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dayTxt = itemView.findViewById(R.id.dayTxt);
            status = itemView.findViewById(R.id.statusTxt);
            hightTxt = itemView.findViewById(R.id.hightTxt);
            lowTxt = itemView.findViewById(R.id.lowTxt);
            pic = itemView.findViewById(R.id.pic);
            click =itemView.findViewById(R.id.click);
        }
    }
}
