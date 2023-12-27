package com.example.quickgrub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quickgrub.Adapter.RecentBuyAdapter;
import com.example.quickgrub.DataModell.OrderDetails;
import com.example.quickgrub.databinding.ActivityRecentOrderItemBinding;

import java.util.ArrayList;

public class RecentOrderItemActivity extends AppCompatActivity {

    private ActivityRecentOrderItemBinding binding;
    private ArrayList<String> foodName=new ArrayList<>();
    private ArrayList<String> foodPrice=new ArrayList<>();
    private ArrayList<String> foodImage=new ArrayList<>();
    private ArrayList<Integer> foodQuantity=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentOrderItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.ibBackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        // Extract the ArrayList<OrderDetails> from the intent
        OrderDetails recentOrderItem = (OrderDetails) intent.getSerializableExtra("RecentBuyOrderItem");

        if (recentOrderItem != null && !recentOrderItem.getFoodItemImage().isEmpty()) {
                ArrayList<String> foodItemName = recentOrderItem.getFoodItemName();
                ArrayList<String> foodItemPrice = recentOrderItem.getFoodItemPrice();
                ArrayList<String> foodItemImage = recentOrderItem.getFoodItemImage();
                ArrayList<Integer> foodItemQuantity = recentOrderItem.getItemQuantities();

                foodName.addAll(foodItemName);
                foodPrice.addAll(foodItemPrice);
                foodImage.addAll(foodItemImage);
                foodQuantity.addAll(foodItemQuantity);

        }
        setAdapter();
    }

    private void setAdapter() {

        RecyclerView rv = binding.rvRecentBuy;
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecentBuyAdapter recentBuyAdapter = new RecentBuyAdapter(this, foodName, foodPrice, foodImage, foodQuantity);
        rv.setAdapter(recentBuyAdapter);


    }
}