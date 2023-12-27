package com.example.quickgrub.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quickgrub.Adapter.BuyAgainAdapter;
import com.example.quickgrub.DataModell.OrderDetails;
import com.example.quickgrub.RecentOrderItemActivity;
import com.example.quickgrub.databinding.FragmentHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String userId;
    private ArrayList<OrderDetails> listOfOrderItem = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = (user != null) ? user.getUid() : "";

        database = FirebaseDatabase.getInstance();

        retrieveBuyHistory();

        binding.recentBuyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setItemRecentBuy();
            }
        });

        binding.btnReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrderStatus();
            }

            private void updateOrderStatus() {
                String itemPushKey=listOfOrderItem.get(0).getItemPushKey();
                DatabaseReference completeOrderReference=database.getReference().child("CompleteOrder").child(itemPushKey);
                completeOrderReference.child("paymentReceived").setValue(true);
                Toast.makeText(getContext(), "Order Received Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void setItemRecentBuy() {
        if (!listOfOrderItem.isEmpty()) {
            Collections.reverse(listOfOrderItem);
            OrderDetails mostRecentOrder = listOfOrderItem.get(0);
            Intent intent = new Intent(getContext(), RecentOrderItemActivity.class);
            intent.putExtra("RecentBuyOrderItem", mostRecentOrder);
            startActivity(intent);
        }

    }

    // ...

    private void retrieveBuyHistory() {
        binding.recentBuyItem.setVisibility(View.INVISIBLE);

        DatabaseReference buyItemReference = database.getReference().child("user").child(userId).child("BuyHistory");

        Query sortingQuery = buyItemReference.orderByChild("currentTime");

        sortingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot buySnapshot : snapshot.getChildren()) {
                    OrderDetails buyHistoryItem = buySnapshot.getValue(OrderDetails.class);
                    if (buyHistoryItem != null) {
                        listOfOrderItem.add(buyHistoryItem);
                    }
                }

                Collections.reverse(listOfOrderItem);

                if (!listOfOrderItem.isEmpty()) {
                    setDataInRecentBuy();
                    setPreviousBuyRecycleView();
                } else {
                    // Handle the case when there are no order history items
                    // For example, you can display a message or hide UI elements.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error, e.g., log or display an error message
            }
        });
    }


    private void setDataInRecentBuy() {
        binding.recentBuyItem.setVisibility(View.VISIBLE);
        OrderDetails recentOrderItem = listOfOrderItem.get(0);


        String foodName = recentOrderItem.getFoodItemName() != null && !recentOrderItem.getFoodItemName().isEmpty()
                ? recentOrderItem.getFoodItemName().get(0)
                : "";
        String price = recentOrderItem.getFoodItemPrice() != null && !recentOrderItem.getFoodItemPrice().isEmpty()
                ? recentOrderItem.getFoodItemPrice().get(0)
                : "";
        String image = recentOrderItem.getFoodItemImage() != null && !recentOrderItem.getFoodItemImage().isEmpty()
                ? recentOrderItem.getFoodItemImage().get(0)
                : "";

        if (image != null) {

            binding.tvBuyRecentName.setText(foodName);
            binding.tvBuyRecentPrice.setText(price);

            Uri uri = Uri.parse(image);
            Glide
                    .with(requireContext())
                    .load(uri)
                    .into(binding.ivBuyRecentImage);
        }
        Collections.reverse(listOfOrderItem);
    }

    private void setPreviousBuyRecycleView() {

        ArrayList<String> buyAgainFoodName = new ArrayList<>();
        ArrayList<String> buyAgainFoodPrice = new ArrayList<>();
        ArrayList<String> buyAgainFoodImage = new ArrayList<>();
        ArrayList<OrderDetails> buyItem = listOfOrderItem;

        // Initialize an empty list to store food names

        for (int i = 0; i < listOfOrderItem.size(); i++) {
            OrderDetails orderDetails = listOfOrderItem.get(i);
            ArrayList<String> foodItemName = orderDetails.getFoodItemName();
            ArrayList<String> foodItemPrice = orderDetails.getFoodItemPrice();
            ArrayList<String> foodItemImage = orderDetails.getFoodItemImage();

            if (foodItemName != null && !foodItemName.isEmpty()) {

                buyAgainFoodName.add(foodItemName.get(0).toString());
                buyAgainFoodPrice.add(foodItemPrice.get(0).toString());
                buyAgainFoodImage.add(foodItemImage.get(0).toString());


                Boolean isOrderAccepted=listOfOrderItem.get(0).getOrderAccepted();

                if(isOrderAccepted==true){
                    binding.cvOrderStatus.setCardBackgroundColor(Color.GREEN);
                    binding.btnReceived.setVisibility(View.VISIBLE);
                }
            }
        }
        RecyclerView rv = binding.rvHistory;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        BuyAgainAdapter buyAgainAdapter = new BuyAgainAdapter(getContext(), buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImage);
        rv.setAdapter(buyAgainAdapter);

    }

}