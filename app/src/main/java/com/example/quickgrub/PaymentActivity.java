package com.example.quickgrub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickgrub.BottomSheet.congratulationBottomSheet;
import com.example.quickgrub.DataModell.OrderDetails;
import com.example.quickgrub.databinding.ActivityPaytmentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    public static Intent getIntent(Context context) {

        return new Intent(context, PaymentActivity.class);
    }

    private ImageButton ibBack;
    private EditText etName, etPhone, etAddress;
    private TextView tvTotalAmount;
    private Button btnPlaceOrder;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String name;
    private String address;
    private String phone;
    private String totalAmount;
    private ArrayList<String> foodItemName;
    private ArrayList<String> foodItemPrice;
    private ArrayList<String> foodItemImage;
    private ArrayList<String> foodItemDescription;
    private ArrayList<String> foodItemIngredient;
    private int[] itemQuantities;
    private String userId;

    private ActivityPaytmentBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPaytmentBinding.inflate(getLayoutInflater()); // Initialize binding
        View view = binding.getRoot();
        setContentView(view);

        init();

        setUserData();

        //get user details from database

        Intent intent = getIntent();
        foodItemName = intent.getStringArrayListExtra("FoodItemName");
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice");
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage");
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription");
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient");
        itemQuantities = intent.getIntArrayExtra("ItemQuantities");
        totalAmount = Integer.toString(calculateTotalAmount()) + "rs";
        binding.tvTotalAmount.setEnabled(false);
        binding.tvTotalAmount.setText(totalAmount);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from edit text

                name = binding.etName.getText().toString().trim();
                phone = binding.etPhone.getText().toString().trim();
                address = binding.etAddress.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    placeOrder();
                }
            }
        });

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void placeOrder() {

        userId = auth.getCurrentUser().getUid();

        Long time = System.currentTimeMillis();
        String itemPushKey = databaseReference.child("OrderDetails").push().getKey();
        ArrayList<Integer> itemQuantitiesList = new ArrayList<>();
        for (int quantity : itemQuantities) {
            itemQuantitiesList.add(quantity);
        }
//        OrderDetails orderDetails = new OrderDetails(userId, name, foodItemName, foodItemPrice, foodItemImage, itemQuantitiesList, address, phone, time, itemPushKey, false, false);

       OrderDetails orderDetails=new OrderDetails(userId,name,address,phone,foodItemName,foodItemPrice,itemQuantitiesList,foodItemImage,totalAmount,time,itemPushKey,false,false);
        DatabaseReference orderReference = databaseReference.child("OrderDetails").child(itemPushKey);

        orderReference.setValue(orderDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                congratulationBottomSheet congratulationBottomSheet = new congratulationBottomSheet();
                congratulationBottomSheet.setCancelable(false);
                congratulationBottomSheet.show(getSupportFragmentManager(), "order Complete");
                removeItemFromCart();
                addOrderToHistory(orderDetails);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaymentActivity.this, "Due  to some reason Order is not complete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addOrderToHistory(OrderDetails orderDetails) {

        databaseReference.child("user").child(userId).child("BuyHistory")
                .child(orderDetails.getItemPushKey()).setValue(orderDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PaymentActivity.this, "Fail to Order", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeItemFromCart() {

        DatabaseReference cartItemReference=databaseReference.child("user").child(userId).child("cartItems");
        cartItemReference.removeValue();
    }

    private int calculateTotalAmount() {
        int totalAmount = 0;
        for (int i = 0; i < foodItemPrice.size(); i++) {
            String price = foodItemPrice.get(i);
            char lastChar = price.charAt(price.length() - 1);
            int priceIntValue;

            if (lastChar == '@') {
                String trimmedPrice = price.substring(0, price.length() - 1);
                priceIntValue = Integer.parseInt(trimmedPrice);
            } else {
                priceIntValue = Integer.parseInt(price);
            }

            int quantity = itemQuantities[i];
            totalAmount += priceIntValue * quantity;
        }
        return totalAmount;
    }

    private void init() {

        etName = findViewById(R.id.et_name);
        etAddress = findViewById(R.id.et_address);
        etPhone = findViewById(R.id.et_phone);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        btnPlaceOrder = findViewById(R.id.btn_placeorder);
        ibBack = findViewById(R.id.ib_backbutton);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setUserData() {

        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        if (user != null) {
            DatabaseReference userRef = databaseReference.child("user").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        etName.setText(name);
                        etAddress.setText(address);
                        etPhone.setText(phone);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}