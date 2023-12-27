package com.example.quickgrub.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickgrub.ClickListener;
import com.example.quickgrub.DataModell.MenuItemModel;
import com.example.quickgrub.R;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.RecycleViewHolder> {

    private ArrayList<MenuItemModel> menuItems;
    private ClickListener clickListener;
    private Context context;

    public MenuAdapter(Context context, ArrayList<MenuItemModel> menuItems, ClickListener clickListener) {
        this.context = context;
        this.menuItems = menuItems;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        MenuItemModel menuItem = menuItems.get(position);

        holder.tvMenuFoodName.setText(menuItem.getFoodName());
        holder.tvMenuPrice.setText(menuItem.getFoodPrice());

        Uri uri = Uri.parse(menuItem.getFoodImage());
        Log.d("menuAdapter", "onBindViewHolder: "+menuItem.getFoodImage());
        Glide
                .with(context)
                .load(uri)
                .into(holder.menuImage);

    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView menuImage;

        private TextView tvMenuFoodName, tvMenuPrice, tvMenuCart;
        private CardView cardItem;

        public RecycleViewHolder(View view) {
            super(view);
            menuImage = view.findViewById(R.id.menuimage);
            cardItem = view.findViewById(R.id.menu_card_view_item);
            tvMenuFoodName = view.findViewById(R.id.tv_menufoodname);
            tvMenuPrice = view.findViewById(R.id.tv_menuprice);
            tvMenuCart = view.findViewById(R.id.tv_menucart);
            cardItem.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (clickListener != null) {
                clickListener.onClick(getAbsoluteAdapterPosition());
            }

        }

    }

}

