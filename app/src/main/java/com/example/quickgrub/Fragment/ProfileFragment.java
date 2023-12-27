package com.example.quickgrub.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quickgrub.DataModell.UserModel;
import com.example.quickgrub.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference database;
    private FragmentProfileBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        binding.etAddress.setEnabled(false);
        binding.etName.setEnabled(false);
        binding.etEmail.setEnabled(false);
        binding.etPhone.setEnabled(false);
        binding.etPassword.setEnabled(false);

        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isEnable = false;
                isEnable = !isEnable;
                binding.etAddress.setEnabled(true);
                binding.etName.setEnabled(true);
                binding.etEmail.setEnabled(true);
                binding.etPhone.setEnabled(true);
                binding.etPassword.setEnabled(true);
                if(isEnable){
                    binding.etName.requestFocus();
                }

            }
        });

        setUserData();


        binding.btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.etName.getText().toString();
                String email = binding.etEmail.getText().toString();
                String phone = binding.etPhone.getText().toString();
                String address = binding.etAddress.getText().toString();
                String password=binding.etPassword.getText().toString();

                updateUserData(name, email, address, phone,password);
            }
        });

        return view;
    }

    private void updateUserData(String name, String email, String address, String phone,String password) {
        String userId = auth.getCurrentUser().getUid();
        if (userId != null) {

            DatabaseReference userReference = database.child("user").child(userId);

// Create a HashMap with key-value pairs
            HashMap<String, String> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("address", address);
            userData.put("email",email);
            userData.put("phone",phone);
            userData.put("password",password);

            userReference.setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(),"Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Profile updated fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void setUserData() {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        if (userId != null) {
            DatabaseReference userReference = database.child("user").child(userId);

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        Log.d("snapshot", "onDataChange: run this part ");
                        UserModel userProfile = snapshot.getValue(UserModel.class);
                        if (userProfile != null) {
                            Log.d("userProfile", "onDataChange: run this part also");
                            binding.etName.setText(userProfile.getName());
                            binding.etEmail.setText(userProfile.getEmail());
                            binding.etPhone.setText(userProfile.getPhone());
                            binding.etAddress.setText(userProfile.getAddress());
                            binding.etPassword.setText(userProfile.getPassword());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}