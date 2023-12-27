package com.example.quickgrub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quickgrub.DataModell.MenuItemModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsActivity extends AppCompatActivity {

    public static Intent getIntent(Context context, MenuItemModel menuItems) {
        Intent intent = new Intent(context, DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("FoodName", menuItems.getFoodName());
        bundle.putString("FoodPrice", menuItems.getFoodPrice());
        bundle.putString("FoodImage", menuItems.getFoodImage());
        bundle.putString("FoodDescription", menuItems.getFoodDescription());
        bundle.putString("FoodIngredient", menuItems.getFoodIngredient());
        intent.putExtras(bundle);
        return intent;
    }

    private  String foodName;
    private  String foodPrice;
    private  String foodImage;
    private Uri uri;
    private  String foodDescription;
    private String foodIngredient;

    private FirebaseAuth auth;

    private ImageView imageView, ibBack;
    private Button btnAddCart;

    private TextView tvDetailFoodName, tvDetailDescription, tvIngredient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        init();

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToCart();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {

            Bundle bundle = intent.getExtras();
             foodName = bundle.getString("FoodName");
            foodDescription = bundle.getString("FoodDescription");
            foodIngredient = bundle.getString("FoodIngredient");
            foodPrice = bundle.getString("FoodPrice");

            foodImage = bundle.getString("FoodImage");
            Log.d("Image URL", "URL: " + foodImage);
            uri = Uri.parse(foodImage);
            Glide
                    .with(DetailsActivity.this)
                    .load(uri)
                    .placeholder(R.drawable.banner1)
                    .into(imageView);
            tvDetailFoodName.setText(foodName);
            tvDetailDescription.setText(foodDescription);
            tvIngredient.setText(foodIngredient);

        }
    }

    private void init() {
        tvIngredient = findViewById(R.id.tv_ingredients);
        tvDetailFoodName = findViewById(R.id.tv_detail_food_name);
        tvDetailDescription = findViewById(R.id.tv_description);
        imageView = findViewById(R.id.iv_detail_image);
        btnAddCart = findViewById(R.id.btn_add_cart);
        ibBack = findViewById(R.id.ib_backbutton);

        auth=FirebaseAuth.getInstance();
    }
    private void addItemToCart(){

        DatabaseReference database= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = auth.getCurrentUser();
        String userId = (user != null) ? user.getUid() : "";

        MenuItemModel menuItem=new MenuItemModel(
                foodName,
                foodPrice,
                foodImage,
                foodDescription,
                foodIngredient
        );

        //save data to cart item to firebase database

        database.child("user").child(userId).child("cartItems").push().setValue(menuItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Add", "onSuccess: run this part");
                Toast.makeText(DetailsActivity.this, "Items added into cart sucessfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("fail", "onFailure: run this part also");
                Toast.makeText(DetailsActivity.this, "Items not added ", Toast.LENGTH_SHORT).show();
            }
        });


    }
}