package com.example.myapplicationlifesource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class profilepage extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    Button editInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        editInfo = findViewById(R.id.edit_info);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        mAuth = FirebaseAuth.getInstance();
editInfo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(profilepage.this , newdonor.class));
    }
});
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      //      Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.logout:
                    mAuth.signOut();

                    Intent intent = new Intent(profilepage.this, signinpage.class);
                    startActivity(intent);
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
