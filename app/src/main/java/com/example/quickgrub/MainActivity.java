package com.example.quickgrub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.quickgrub.BottomSheet.NotificationBottomSheet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static Intent getIntent(Context context){
       return  new Intent(context,MainActivity.class);
    }
    private ImageButton ibNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ibNotification=findViewById(R.id.ib_notification);

        NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController=navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView2);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        ibNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationBottomSheet notificationFragment=new NotificationBottomSheet();

                notificationFragment.setCancelable(false);
                notificationFragment.show(getSupportFragmentManager(),"notification");
            }
        });
    }
}