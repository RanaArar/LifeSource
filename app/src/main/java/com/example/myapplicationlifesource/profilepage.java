package com.example.myapplicationlifesource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class profilepage extends AppCompatActivity implements View.OnClickListener {
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    User user;
    String userId;
    private Button editInfo, notification,
            getAppointment, logout;

    private static String sentToEmail;
    private ImageView calnder, doning,
            amalLocation, nabdLocation, hashimiLocation;
    private TextView name, amalHospital, nabdHospital, hashimiHosiptal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        //---------------------------------------------
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        editInfo = findViewById(R.id.edit_info);
        notification = findViewById(R.id.user_notification);
        amalHospital = findViewById(R.id.amal_hospital);
        nabdHospital = findViewById(R.id.nabd_hospital);
        hashimiHosiptal = findViewById(R.id.hashimi_hospital);
        doning = findViewById(R.id.donoing);
        getAppointment = findViewById(R.id.get_appointment);
        calnder = findViewById(R.id.calender);
        name = findViewById(R.id.user_name);
        logout = findViewById(R.id.logout_donor1);
        amalLocation = findViewById(R.id.amal_location);
        nabdLocation = findViewById(R.id.nabd_location);
        hashimiLocation = findViewById(R.id.hashmi_location);
        ///---------------database----------------------------
        user = new User();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        final FirebaseUser userKey = mAuth.getCurrentUser();
        userId = userKey.getUid();
        //------------------bottom navigation-----------------------
        //  bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        mAuth = FirebaseAuth.getInstance();
        //-------------------------Listeners---------------------------------------
        editInfo.setOnClickListener(this);
        calnder.setOnClickListener(this);
        notification.setOnClickListener(this);
        getAppointment.setOnClickListener(this);
        logout.setOnClickListener(this);
        amalLocation.setOnClickListener(this);
        nabdLocation.setOnClickListener(this);
        hashimiLocation.setOnClickListener(this);
        //------------------------retrieve user name-------------------------

        databaseReference.child("Donor").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.exists()) {
                        user = dataSnapshot.getValue(User.class);
                        name.setText(user.getName());
                        sentToEmail = user.getEmail();

                    } else {
                        Toast.makeText(profilepage.this, "No data for the user", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

/*
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
*/
/*
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();*//*

            return true;
        }
    };
*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info:
                startActivity(new Intent(profilepage.this, newdonor.class));
                break;
            case R.id.user_notification:
                startActivity(new Intent(profilepage.this, Notificationpage.class));
                break;
            case R.id.get_appointment:
                chooseDate();
                break;
            case R.id.amal_hospital:
                selectedHospital(amalHospital);
                break;
            case R.id.nabd_hospital:
                selectedHospital(nabdHospital);
                break;
            case R.id.hashimi_hospital:
                selectedHospital(hashimiHosiptal);
                break;
            case R.id.logout:
                mAuth.signOut();

                Intent intent = new Intent(profilepage.this, signinpage.class);
                startActivity(intent);
                break;
            case R.id.amal_location:
                sendToLocation(amalLocation, 12, 12);
                break;
            case R.id.nabd_location:
                sendToLocation(nabdLocation, 12, 12);
                break;
            case R.id.hashmi_location:
                sendToLocation(hashimiLocation, 12, 12);
                break;
        }
    }

    private void sendToLocation(ImageView amalLocation, int lat, int lag) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lag);

        Uri location = Uri.parse(uri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void chooseDate() {


    }

    private void selectedHospital(TextView t) {
        t.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        final String selectedHospital = t.getText().toString();
        user.setHospital(selectedHospital);
        databaseReference.child("Donor").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(profilepage.this, selectedHospital + " is stored ", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(profilepage.this, "Failed ", Toast.LENGTH_LONG).show();
                }


            }
        });

    }
}
