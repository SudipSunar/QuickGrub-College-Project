package com.example.quickgrub.BottomSheet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.quickgrub.Adapter.MenuAdapter;
import com.example.quickgrub.ClickListener;
import com.example.quickgrub.DataModell.MenuItemModel;
import com.example.quickgrub.DetailsActivity;
import com.example.quickgrub.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MenuBottomSheetFragment extends BottomSheetDialogFragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<MenuItemModel> menuItem = new ArrayList<>();
    private RecyclerView recyclerView;
    ClickListener clickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.menu_bottom_sheet, container, false);

        ImageButton ibBack = view.findViewById(R.id.ib_backbutton);
        recyclerView = view.findViewById(R.id.rv_recent_buy);


        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        clickListener = new ClickListener() {
            @Override
            public void onClick(int position) {
                MenuItemModel menuItemModel = menuItem.get(position);
                Intent intent = DetailsActivity.getIntent(getContext(), menuItemModel);
                startActivity(intent);
            }
        };


        retrieveMenuItem();


        return view;
    }

    //retrieve data from database from node menu
    private void retrieveMenuItem() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference foodRef = database.getReference().child("menu");

        //fetch data from database
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear existing data before populating
                menuItem.clear();
                //loop for through each food item

                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {

                    MenuItemModel menuItems = foodSnapshot.getValue(MenuItemModel.class);

                    if (menuItems != null) {
                        menuItem.add(menuItems);
                        Log.d("image", "onDataChange: "+menuItem);
                    }
                }
                Log.d("items", "onDataChange: Data received");
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("Database Error", "Error:" + error);

            }
        });
    }

    private void setAdapter() {

        if (menuItem != null) {

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            MenuAdapter menuAdapter = new MenuAdapter(getContext(), menuItem, clickListener);
            recyclerView.setAdapter(menuAdapter);
            Log.d("items", "setAdapter: data set");
        } else {
            Log.d("items", "setAdapter: data not set");
        }

    }

}
