package com.example.quickgrub.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickgrub.R;

import java.util.ArrayList;

public class NotifiactionAdapter extends RecyclerView.Adapter<NotifiactionAdapter.RecycleViewHolder> {

    private ArrayList<String> notifiaction;
    private ArrayList<Integer> notificationImage;

    public NotifiactionAdapter( ArrayList<String> notification,ArrayList<Integer> notificationImage){
        this.notifiaction=notification;
        this.notificationImage=notificationImage;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        holder.tvNotification.setText(notifiaction.get(position));
        holder.ivNotification.setImageResource(notificationImage.get(position));

    }

    @Override
    public int getItemCount() {
        return notifiaction.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNotification;
        private ImageView ivNotification;
        public RecycleViewHolder(View view){
            super(view);

            tvNotification=view.findViewById(R.id.tv_notification);
            ivNotification=view.findViewById(R.id.iv_notification);
        }
    }
}
