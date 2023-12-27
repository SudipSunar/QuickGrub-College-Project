package com.example.quickgrub.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickgrub.DataModell.MenuItemModel;
import com.example.quickgrub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.RecycleViewHolder> {


    private ArrayList<MenuItemModel> menuItems;
    private Context context;
    private String userId;

    private int[] itemQuantities;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private DatabaseReference cartItemsReference;


    public CartAdapter(Context context, ArrayList<MenuItemModel> menuItems) {
        this.menuItems = menuItems;
        this.context = context;
        auth=FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        userId = (currentUser != null) ? currentUser.getUid() : "";
        int cartItemNumber = menuItems.size();
        itemQuantities = new int[cartItemNumber];
        Arrays.fill(itemQuantities, 1);

        cartItemsReference = database.getReference().child("user").child(userId).child("cartItems");
    }


    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {

        MenuItemModel menuItem = menuItems.get(position);

        holder.tvCartName.setText(menuItem.getFoodName());
        holder.tvCartPrice.setText(menuItem.getFoodPrice());
        String uriString = menuItem.getFoodImage();

        Uri uri = Uri.parse(uriString);
        Glide
                .with(context)
                .load(uri)
                .into(holder.ivCart);

        holder.ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = holder.increaseQuantity(holder.getAdapterPosition());
                holder.tvCount.setText(String.valueOf(count));

            }
        });

        holder.ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = holder.decreaseQuantity(holder.getAdapterPosition());
                holder.tvCount.setText(String.valueOf(count)); // Update the UI
                System.out.println("this part run curretly");
            }
        });

        holder.ibBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.deleteQuantity(holder.getAdapterPosition());
            }
        });


    }

    public class RecycleViewHolder extends RecyclerView.ViewHolder {

        public ImageButton ibPlus, ibMinus, ibBin;
        public TextView tvCartName, tvCartPrice, tvCount;
        public ImageView ivCart;

        public RecycleViewHolder(View view) {
            super(view);

            ibPlus = view.findViewById(R.id.ib_plus);
            ibMinus = view.findViewById(R.id.ib_minus);
            ibBin = view.findViewById(R.id.ib_bin);
            tvCartName = view.findViewById(R.id.tv_cartname);
            tvCartPrice = view.findViewById(R.id.tv_cartprice);
            ivCart = view.findViewById(R.id.iv_cartimage);
            tvCount = view.findViewById(R.id.tv_count);


        }

        public int increaseQuantity(int position) {
            int count = 0;
            if (itemQuantities != null) {
                if (itemQuantities[position] < 10) {
                    itemQuantities[position]++;
                    count = itemQuantities[position];
                }
            }
            return count;
        }

        public int decreaseQuantity(int position) {
            int count = itemQuantities[position];
            if (count > 1) {
                itemQuantities[position]--; // Decrease the quantity
                count = itemQuantities[position]; // Update the count
            }
            return count;
        }

        public void deleteQuantity(int position) {
            final int positionRetrieve = position;
            getUniqueKeyAtPosition(positionRetrieve).addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (task.isSuccessful()) {
                        String uniqueKey = task.getResult();
                        if (uniqueKey != null) {
                            removeItem(position, uniqueKey);
                        }
                    } else {
                        // Handle the error
                    }
                }
            });
        }

        private void removeItem(int position, String uniqueKey) {
            if (uniqueKey != null) {
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        menuItems.remove(position);
                        // Update itemQuantities
                        List<Integer> updatedQuantities = new ArrayList<>();
                        for (int i = 0; i < itemQuantities.length; i++) {
                            if (i != position) {
                                updatedQuantities.add(itemQuantities[i]);
                            }
                        }
                        itemQuantities = new int[updatedQuantities.size()];
                        for (int i = 0; i < updatedQuantities.size(); i++) {
                            itemQuantities[i] = updatedQuantities.get(i);
                        }
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,menuItems.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        private Task<String> getUniqueKeyAtPosition(int positionRetrieve) {
            final TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
            cartItemsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String uniqueKey = null;
                    int index = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.getKey();
                            break;
                        }
                        index++;
                    }
                    taskCompletionSource.setResult(uniqueKey);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle onCancelled
                    taskCompletionSource.setException(error.toException());
                }
            });
            return taskCompletionSource.getTask();
        }

    }

    public int[] getUpdatedItemsQuantities() {
        return itemQuantities;
    }

}

