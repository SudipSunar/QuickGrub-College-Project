package com.example.quickgrub.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.quickgrub.Adapter.MenuAdapter;
import com.example.quickgrub.ClickListener;
import com.example.quickgrub.DataModell.MenuItemModel;
import com.example.quickgrub.databinding.FragmentSearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchFragment extends Fragment {

    private SearchView searchView;

    private FirebaseDatabase database;
    private ClickListener clickListener;
    private ArrayList<MenuItemModel> originalMenuItems = new ArrayList<>();
    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        //retrieve menu item from database
        retrieveMenuItem();
        setUpSearchView();

        return view;
    }

    private void retrieveMenuItem() {
        //get database reference
        database = FirebaseDatabase.getInstance();

        //reference to the menu node
        DatabaseReference foodReference = database.getReference().child("menu");

        foodReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    MenuItemModel menuItem = foodSnapshot.getValue(MenuItemModel.class);
                    if (menuItem != null) {
                        originalMenuItems.add(menuItem);
                    }
                }
                showAllMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAllMenu() {
        ArrayList<MenuItemModel> filterMenuItem = originalMenuItems;
        setAdapter(filterMenuItem);
    }

    private void setAdapter(ArrayList<MenuItemModel> filterMenuItem) {

        MenuAdapter adapter = new MenuAdapter(getContext(), filterMenuItem, clickListener);

        RecyclerView rv = binding.recyclerviewmenu;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

    }


    private void setUpSearchView() {

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterMenuItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterMenuItem(newText);
                return true;
            }
        });

    }

    private void filterMenuItem(String query) {

        ArrayList<MenuItemModel> filteredMenuItems = new ArrayList<>();

        for (MenuItemModel menuItem : originalMenuItems) {
            if (menuItem.getFoodName() != null && menuItem.getFoodName().toLowerCase().contains(query.toLowerCase())) {
                filteredMenuItems.add(menuItem);
            }
        }
        setAdapter(filteredMenuItems);
    }
}