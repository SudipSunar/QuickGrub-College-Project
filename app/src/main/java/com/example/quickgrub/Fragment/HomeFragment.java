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
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.AnimationTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.quickgrub.Adapter.MenuAdapter;
import com.example.quickgrub.BottomSheet.MenuBottomSheetFragment;
import com.example.quickgrub.ClickListener;
import com.example.quickgrub.DataModell.MenuItemModel;
import com.example.quickgrub.DetailsActivity;
import com.example.quickgrub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class HomeFragment extends Fragment {

    private FirebaseDatabase database;
    private ArrayList<MenuItemModel> menuItem;
    private TextView tvViewMenu;
    private RecyclerView recyclerView;
    private MenuBottomSheetFragment menuBottomSheetFragment;
    private ClickListener clickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
         recyclerView = v.findViewById(R.id.reculerview);

        ArrayList<SlideModel> imagelist = new ArrayList<>();

        imagelist.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));

        tvViewMenu = v.findViewById(R.id.tv_viewmenu);
        ImageSlider imageSlider = v.findViewById(R.id.image_slider);

        imageSlider.setImageList(imagelist);
        imageSlider.setSlideAnimation(AnimationTypes.ROTATE_DOWN);


        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                System.out.println("Item selected at position: " + i);
            }

            @Override
            public void doubleClick(int i) {

            }
        });


        tvViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MenuBottomSheetFragment menuBottomSheetFragment = new MenuBottomSheetFragment();
                menuBottomSheetFragment.setCancelable(false);

                menuBottomSheetFragment.show(getParentFragmentManager(), "customDialog");
                Log.d("viewmenu", "onClick: run this part");


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

        //retrieve and display popular item
        retrieveDisplayPopularItem();


        return v;

    }

    private void retrieveDisplayPopularItem(){
        database=FirebaseDatabase.getInstance();
        DatabaseReference foodRef=database.getReference().child("menu");

         menuItem=new ArrayList<>();

         //retrieve menu iItems from database

        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItem.clear();
                for(DataSnapshot foodSnapshot:snapshot.getChildren()){
                    MenuItemModel menuItems=foodSnapshot.getValue(MenuItemModel.class);
                    if(menuItems!=null){

                        menuItem.add(menuItems);
                    }
                }
                //display random popular item
                randomPopularItem();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void randomPopularItem(){
        // Shuffle the menuItem list
        Collections.shuffle(menuItem);

        // Specify the number of items to show
        int numItemsToShow = 6;

        // Take the first numItemsToShow items from the shuffled list
        ArrayList<MenuItemModel> subsetMenuItems = new ArrayList<>(menuItem.subList(0, Math.min(numItemsToShow, menuItem.size())));

        // Set the adapter for popular items
        setPopularItemAdapter(subsetMenuItems);

        setPopularItemAdapter(subsetMenuItems);

    }
    private void setPopularItemAdapter(ArrayList<MenuItemModel> menuItem){


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        MenuAdapter menuAdapter=new MenuAdapter(getContext(),menuItem,clickListener);
        recyclerView.setAdapter(menuAdapter);
    }
}