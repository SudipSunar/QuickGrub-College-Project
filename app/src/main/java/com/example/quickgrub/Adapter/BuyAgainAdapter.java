package com.example.quickgrub.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickgrub.DataModell.OrderDetails;
import com.example.quickgrub.R;

import java.util.ArrayList;

public class BuyAgainAdapter extends RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder> {

    private ArrayList<String> foodName;
    private ArrayList<String> foodPrice;
    private ArrayList<String> foodImage;

    private Context context;

    public BuyAgainAdapter(Context context, ArrayList<String> foodName, ArrayList<String> foodPrice, ArrayList<String> foodImage) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
        this.context = context;
    }

    @NonNull
    @Override
    public BuyAgainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.buy_again_item, parent, false);
        return new BuyAgainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyAgainViewHolder holder, int position) {


        holder.tvName.setText(foodName.get(position));
        holder.tvPrice.setText(foodPrice.get(position));
        String image = foodImage.get(position);

        Uri uri = Uri.parse(image);
        Glide
                .with(context)
                .load(uri)
                .into(holder.ivImage);

//        holder.ivImage.setImageResource(dataModel.getImage());
    }

    @Override
    public int getItemCount() {
        return foodName.size();
    }

    public class BuyAgainViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice;
        private ImageView ivImage;

        public BuyAgainViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_buy_recent_name);
            tvPrice = view.findViewById(R.id.tv_buy_recent_price);
            ivImage = view.findViewById(R.id.iv_buy_recent_image);
        }
    }
}
