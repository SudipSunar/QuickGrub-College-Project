package com.example.quickgrub.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.quickgrub.Adapter.CartAdapter;
import com.example.quickgrub.DataModell.MenuItemModel;
import com.example.quickgrub.PaymentActivity;
import com.example.quickgrub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {

    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private final ArrayList<MenuItemModel> menusItems = new ArrayList<>();
    private String userId;
    private CartAdapter cartAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        //initialize firebase and database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userId = (user != null) ? user.getUid() : "";

        retrieveMenuItem();

        Button btnCartOrder = view.findViewById(R.id.btn_cartorder);

        btnCartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get order item details before proceeding to payment
                getOrderItemDetail();
            }
        });

        recyclerView = view.findViewById(R.id.cartrecyclerView);


        return view;
    }

    //store order item list in database from user node with cart item node
    private void getOrderItemDetail() {
        DatabaseReference orderIdReference = database.getReference().child("user").child(userId).child("cartItems");
        int[] foodQuantity;
        foodQuantity = cartAdapter.getUpdatedItemsQuantities();
        List<String> foodName = new ArrayList<>();
        List<String> foodPrice = new ArrayList<>();
        List<String> foodImage = new ArrayList<>();
        List<String> foodDescription = new ArrayList<>();
        List<String> foodIngredient = new ArrayList<>();

        orderIdReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    MenuItemModel orderItems = foodSnapshot.getValue(MenuItemModel.class);
                    if (orderItems != null) {
                        foodName.add(orderItems.getFoodName());
                        foodPrice.add(orderItems.getFoodPrice());
                        foodImage.add(orderItems.getFoodImage());
                        foodDescription.add(orderItems.getFoodDescription());
                        foodIngredient.add(orderItems.getFoodIngredient());
                    }
                }
                orderNow(foodName, foodPrice, foodDescription, foodImage, foodIngredient, foodQuantity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void orderNow(
            List<String> foodName,
            List<String> foodPrice,
            List<String> foodDescription,
            List<String> foodImage,
            List<String> foodIngredient,
            int[] itemQuantity
    ) {
        if (isAdded() && getContext() != null) {
            Intent intent = new Intent(requireContext(), PaymentActivity.class);        //pass order item details to payment activity
            intent.putStringArrayListExtra("FoodItemName", new ArrayList<String>(foodName));
            intent.putStringArrayListExtra("FoodItemPrice", new ArrayList<String>(foodPrice));
            intent.putStringArrayListExtra("FoodItemImage", new ArrayList<String>(foodImage));
            intent.putStringArrayListExtra("FoodItemDescription", new ArrayList<String>(foodDescription));
            intent.putStringArrayListExtra("FoodItemIngredient", new ArrayList<String>(foodIngredient));

            // Pass the integer array as an extra
            intent.putExtra("ItemQuantities", itemQuantity);

            startActivity(intent);
        }
    }

//method for retrieve data from cartItems node and show them
    private void retrieveMenuItem() {


        DatabaseReference foodRef = database.getReference().child("user").child(userId).child("cartItems");


// fetch data form database

        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear existing data before populating
                menusItems.clear();

                //loop for through each food item

                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {

                    MenuItemModel menuItem = foodSnapshot.getValue(MenuItemModel.class);

                    if (menuItem != null) {
                        menusItems.add(menuItem);
                    }


                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("Database Error", "Error:" + error);

            }
        });
    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartAdapter = new CartAdapter(getContext(), menusItems);

        recyclerView.setAdapter(cartAdapter);

    }

}