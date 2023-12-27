package com.example.quickgrub.BottomSheet;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.quickgrub.MainActivity;
import com.example.quickgrub.PaymentActivity;
import com.example.quickgrub.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class congratulationBottomSheet extends BottomSheetDialogFragment {

    private Button btnCong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.congrulation_bottom_sheet,container,false);

        btnCong=view.findViewById(R.id.btn_cong);


        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
}