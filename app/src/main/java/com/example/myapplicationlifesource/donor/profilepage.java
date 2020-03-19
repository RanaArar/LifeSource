package com.example.myapplicationlifesource.donor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationlifesource.JavaMail.SendMailAsyncTask;
import com.example.myapplicationlifesource.R;
import com.example.myapplicationlifesource.model.User;
import com.example.myapplicationlifesource.newdonor;
import com.example.myapplicationlifesource.signinpage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

import static com.example.myapplicationlifesource.JavaMail.Config.sentToEmail;

public class profilepage extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private User user;
    private String userId;
    //------------------------------------
    private Button editInfo,
            getAppointment;


    private ImageView doning,
            amalLocation, nabdLocation, hashimiLocation;
    private TextView name, amalHospital, nabdHospital, hashimiHosiptal;

    private LinearLayout calnder;
    private int count = 0;
    private ArrayList<String> donateTimes;
    //--------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // savedInstanceState1 = savedInstanceState;
        setContentView(R.layout.activity_profilepage);
        //---------------------------------------------
        editInfo = findViewById(R.id.edit_info);
        //   notification = findViewById(R.id.user_notification);
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
        donateTimes = new ArrayList<>();
        //-------------------------Listeners---------------------------------------
        editInfo.setOnClickListener(this);
        calnder.setOnClickListener(this);
//        notification.setOnClickListener(this);
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
                        if (!user.getHospitals().equals(null))
                            donateTimes = user.getHospitals();

                    } else {
                        Toasty.error(profilepage.this, "No data for the user");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /* -------------------------------------------------*
     *                                                  *
     *             Select Menu Functions                *
     *                                                  *
     *--------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    /* -------------------------------------------------*
     *                                                  *
     *         on click buttons Function                *
     *                                                  *
     *--------------------------------------------------*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donoing:
                donateTime();
                break;
            case R.id.edit_info:
                startActivity(new Intent(profilepage.this, newdonor.class));
                break;

            case R.id.donate_layout:
                SendingEmail();
                break;
            case R.id.amal_hospital:
                selectedHospital(amalHospital, nabdHospital, hashimiHosiptal);
                break;
            case R.id.nabd_hospital:
                selectedHospital(nabdHospital, amalHospital, hashimiHosiptal);
                break;
            case R.id.hashimi_hospital:
                selectedHospital(hashimiHosiptal, amalHospital, nabdHospital);
                break;

            case R.id.amal_location:
                sendToLocation("King Fahad Medical City - KFMC", 24.686323, 46.704937);
                break;
            case R.id.nabd_location:
                sendToLocation("Dallah Hospitals - Alnakheel", 24.748192, 46.652106);
                break;
            case R.id.hashmi_location:

                sendToLocation("Al Hammadi Hospital Al Olaya", 24.710375, 46.681370);
                break;
        }
    }

    /* -------------------------------------------------*
     *                                                  *
     *         show donating dialog Function            *
     *                                                  *
     *--------------------------------------------------*/
    private void donateTime() {
        Dialog donating = new Dialog(profilepage.this);
        donating.setContentView(R.layout.donating_layout);
        ListView recyclerView = donating.findViewById(R.id.recycler_view);
        TextView counting = donating.findViewById(R.id.donating_time);
        counting.setText("Number of donation:" + donateTimes.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(profilepage.this, R.layout.hospital_name, R.id.hos_name, donateTimes);
        recyclerView.setAdapter(adapter);
        donating.show();
    }

    /* -------------------------------------------------*
     *                                                  *
     *         send to map activity Function            *
     *                                                  *
     *--------------------------------------------------*/
    private void sendToLocation(String s, double lat, double lag) {
        Intent intent = new Intent(profilepage.this, MapActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("long", lag);
        intent.putExtra("title", s);
        startActivity(intent);
    }

    /* -------------------------------------------------*
     *                                                  *
     *        sending emails Function                   *
     *                                                  *
     *--------------------------------------------------*/
    private void SendingEmail() {
//--take date ----
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_YEAR, 1);
        date.add(Calendar.HOUR_OF_DAY, 1);
        Date tomorrow = date.getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String formattedDate = df.format(tomorrow);
        String title = "Confirm of appointment";
        String content = "Dear donor " + user.getName() + ", \n \nthis is a confirmation of your appointment at " + formattedDate + " in " + user.getHospital() + "\n \n Donate blood ,\n Donate Love";
        new SendMailAsyncTask(profilepage.this, sentToEmail, title, content).execute();
        count++;

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
        donateTimes.add(selectedHospital);
        user.setHospitals(donateTimes);
        ///store in database
        databaseReference.child("Donor").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(profilepage.this, selectedHospital + " is stored ", Toast.LENGTH_LONG).show();
                    //  Toasty.success(profilepage.this, selectedHospital + " is stored ");
                } else {
                    Toast.makeText(profilepage.this, "Failed ", Toast.LENGTH_LONG).show();
                    // Toasty.error(profilepage.this, "Failed ");
                }


            }
        });

    }

}
