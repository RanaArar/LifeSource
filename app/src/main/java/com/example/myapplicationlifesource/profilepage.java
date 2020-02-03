package com.example.myapplicationlifesource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profilepage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      //      Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.logout:
                  //  selectedFragment = new AzkarFragment();
                    break;
                case R.id.map:
                 //   selectedFragment = new FavoriteFragment();
                    break;

                case R.id.home:
                 //   selectedFragment = new MasbahaFragment();
                    break;
            }
/*
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();*/
            return true;
        }
    };
}
