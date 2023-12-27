package com.example.quickgrub.BottomSheet;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.quickgrub.Adapter.NotifiactionAdapter;
import com.example.quickgrub.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;


public class NotificationBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView notiRecyclerView;
    private ImageButton ibBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_notification,container,false);

        notiRecyclerView=view.findViewById(R.id.noti_recycleview);


        String[] notification={"Your order has been Canceled Successfully",
                "Order has been taken by the driver",
                "Congrats Your Order Placed"};

        Integer[] notificationImage={R.drawable.sademoji,R.drawable.delivery,R.drawable.illustration};

        ArrayList<String> list = new ArrayList<>(Arrays.asList(notification));
        ArrayList<Integer> list2=new ArrayList<>(Arrays.asList(notificationImage));

        notiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        NotifiactionAdapter notifiactionAdapter=new NotifiactionAdapter(list,list2);
        notiRecyclerView.setAdapter(notifiactionAdapter);

        ibBack=view.findViewById(R.id.ib_backbutton);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });




        return view;
    }
}