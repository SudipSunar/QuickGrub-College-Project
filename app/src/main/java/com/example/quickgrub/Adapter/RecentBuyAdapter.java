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
import com.example.quickgrub.databinding.RecentBuyItemBinding;

import java.util.ArrayList;

public class RecentBuyAdapter extends RecyclerView.Adapter<RecentBuyAdapter.RecentBuyViewHolder> {

    private ArrayList<OrderDetails> recentItemList;
    private ArrayList<String> foodName;
    private ArrayList<String> foodNameList;
    private ArrayList<String> foodPriceList;
    private ArrayList<String> foodImageList;
    private ArrayList<Integer> foodQuantityList;
    private RecentBuyItemBinding binding;
    private Context context;

    public RecentBuyAdapter(Context context, ArrayList<String> foodNameList, ArrayList<String> foodPriceList, ArrayList<String> foodImageList, ArrayList<Integer> foodQuantityList) {
        this.context = context;
        this.foodNameList=foodNameList;
        this.foodPriceList=foodPriceList;
        this.foodImageList=foodImageList;
        this.foodQuantityList=foodQuantityList;
    }

    @NonNull
    @Override
    public RecentBuyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecentBuyItemBinding.inflate(LayoutInflater.from(context), parent, false);
        View view = binding.getRoot();
        return new RecentBuyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentBuyViewHolder holder, int position) {


        holder.tvRecentName.setText(foodNameList.get(position));
        holder.tvRecentPrice.setText(foodPriceList.get(position));
        holder.tvRecentQuantity.setText(foodQuantityList.get(position).toString());
        String uriString = foodImageList.get(position);
        Uri uri = Uri.parse(uriString);
        Glide
                .with(context)
                .load(uri)
                .into(holder.ivRecentImage);
    }

    @Override
    public int getItemCount() {
        return foodNameList.size();
    }

    public class RecentBuyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRecentPrice, tvRecentName,tvRecentQuantity;
        private ImageView ivRecentImage;

        public RecentBuyViewHolder(View view) {
            super(view);
            tvRecentPrice = binding.tvRecentPrice;
            tvRecentName = binding.tvRecentName;
            ivRecentImage = binding.ivRecentImage;
            tvRecentQuantity=binding.tvQuantity;


        }
    }
}
