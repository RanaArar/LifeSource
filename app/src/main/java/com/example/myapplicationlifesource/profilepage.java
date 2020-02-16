package com.example.myapplicationlifesource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import org.w3c.dom.Text;

import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class profilepage extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private User user;
    private String userId;
    //------------------------------------
    private Button editInfo, notification,
            getAppointment;

    private static String sentToEmail;
    private ImageView doning,
            amalLocation, nabdLocation, hashimiLocation;
    private TextView name, amalHospital, nabdHospital, hashimiHosiptal;

    private LinearLayout calnder;
    private int count = 0;
    //--------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // savedInstanceState1 = savedInstanceState;
        setContentView(R.layout.activity_profilepage);
        //---------------------------------------------
        editInfo = findViewById(R.id.edit_info);
        notification = findViewById(R.id.user_notification);
        amalHospital = findViewById(R.id.amal_hospital);
        nabdHospital = findViewById(R.id.nabd_hospital);
        hashimiHosiptal = findViewById(R.id.hashimi_hospital);
        doning = findViewById(R.id.donoing);
        getAppointment = findViewById(R.id.get_appointment);
        calnder = findViewById(R.id.donate_layout);
        name = findViewById(R.id.user_name);
        amalLocation = findViewById(R.id.amal_location);
        nabdLocation = findViewById(R.id.nabd_location);
        hashimiLocation = findViewById(R.id.hashmi_location);
        ///---------------database----------------------------
        user = new User();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        final FirebaseUser userKey = mAuth.getCurrentUser();
        userId = userKey.getUid();
        //------------------------------------------------------------------

        //-------------------------Listeners---------------------------------------
        editInfo.setOnClickListener(this);
        calnder.setOnClickListener(this);
        notification.setOnClickListener(this);
        getAppointment.setOnClickListener(this);
        amalLocation.setOnClickListener(this);
        nabdLocation.setOnClickListener(this);
        hashimiLocation.setOnClickListener(this);
        doning.setOnClickListener(this);
        hashimiHosiptal.setOnClickListener(this);
        amalHospital.setOnClickListener(this);
        nabdHospital.setOnClickListener(this);


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
                        Toasty.error(profilepage.this, "No data for the user");
                        //  Toast.makeText(profilepage.this, "No data for the user", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_out:
                mAuth.signOut();
                Intent intent = new Intent(profilepage.this, signinpage.class);
                startActivity(intent);
                return true;
            case R.id.change_password:
                startActivity(new Intent(profilepage.this, editprofile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donoing:
                // Toast.makeText(this, "Number of donation:" + count, Toast.LENGTH_SHORT).show();
                Toasty.info(this, "Number of donation:" + count);
                break;
            case R.id.edit_info:
                startActivity(new Intent(profilepage.this, newdonor.class));
                break;
            case R.id.user_notification:
                startActivity(new Intent(profilepage.this, Notificationpage.class));
                break;
            case R.id.donate_layout:
                SendingEmail();
                break;
            case R.id.amal_hospital:
                //  selectHospitalBoolean(amalHospital, amalBoolean, nabdBoolean, hashimiBoolean);
                selectedHospital(amalHospital, nabdHospital, hashimiHosiptal);
                break;
            case R.id.nabd_hospital:
                selectedHospital(nabdHospital, amalHospital, hashimiHosiptal);
                break;
            case R.id.hashimi_hospital:
                selectedHospital(hashimiHosiptal, amalHospital, nabdHospital);
                break;

            case R.id.amal_location:
                // showMapDialog(savedInstanceState1);
                sendToLocation("International Medical Center", 21.513575, 39.174125);
                break;
            case R.id.nabd_location:
                //   showMapDialog(savedInstanceState1);
                sendToLocation("King Fahad General Hospital", 21.543346, 39.166610);
                break;
            case R.id.hashmi_location:
                // showMapDialog(savedInstanceState1);

                sendToLocation("United Doctors Hospital", 21.505549, 39.166208);
                break;
        }
    }


    private void sendToLocation(String s, double lat, double lag) {
        Intent intent = new Intent(profilepage.this, MapActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("long", lag);
        intent.putExtra("title", s);
        startActivity(intent);
/*        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=21", lat, lag);

        Uri location = Uri.parse(uri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Install Google Map to View Maps", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void SendingEmail() {
//  for sending emails

    }


    /* -------------------------------------------------*
     *                                                  *
     *         Select Hospital Function                 *
     *                                                  *
     *--------------------------------------------------*/
    private void selectedHospital(TextView t, TextView otherText, TextView otherText1) {
        otherText.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        otherText1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        t.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        final String selectedHospital = t.getText().toString();
        user.setHospital(selectedHospital);
        databaseReference.child("Donor").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //   Toast.makeText(profilepage.this, selectedHospital + " is stored ", Toast.LENGTH_LONG).show();
                    Toasty.success(profilepage.this, selectedHospital + " is stored ");
                } else {
                    //Toast.makeText(profilepage.this, "Failed ", Toast.LENGTH_LONG).show();
                    Toasty.error(profilepage.this, "Failed ");
                }


            }
        });

    }

}
