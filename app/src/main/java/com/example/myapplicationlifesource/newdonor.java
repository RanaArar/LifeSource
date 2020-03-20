package com.example.myapplicationlifesource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationlifesource.donor.profilepage;
import com.example.myapplicationlifesource.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import es.dmoral.toasty.Toasty;

import static com.example.myapplicationlifesource.JavaMail.Config.sentToEmail;

public class newdonor extends AppCompatActivity {

    private EditText name, age, weight, phone, email;
    private Spinner bloodType;
    User user;
    private String userId;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private Button submit, logout;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TextView toolbarText;
    String genderValue="F",diseaseValue="N";

    private RadioGroup gender , disease;
    private RadioButton selectedGender , selectedDisease;
    private int selectedGenderID,selectedDiseaseId;
    private static String selectedBloodType;
    final List<String> items= new ArrayList<String>();;
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdonor);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.sendTag("User_ID", sentToEmail);
        //-------------------------------------------------------------------------
        name = findViewById(R.id.register_name);
        age = findViewById(R.id.register_age);
        weight = findViewById(R.id.register_weight);
        phone = findViewById(R.id.register_phone);
        email = findViewById(R.id.register_email);
        submit = findViewById(R.id.submit_donor);
        logout = findViewById(R.id.logout_donor);
        bloodType = findViewById(R.id.register_bloodtype);
        //---------------------RadioGroups-------------------------------------

        gender = findViewById(R.id.register_gender);
        selectedGenderID = gender.getCheckedRadioButtonId();
        selectedGender = (RadioButton) findViewById(selectedGenderID);

        disease = findViewById(R.id.register_disease);
        selectedDiseaseId = disease.getCheckedRadioButtonId();
        selectedDisease = (RadioButton) findViewById(selectedDiseaseId);
        //-------------------------ProgressBar--------------------------------------

        progressBar = findViewById(R.id.new_donor_progressBar);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarText = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText("Register Information");
        //------------------------Database---------------------------------------

        mAuth = FirebaseAuth.getInstance();

        user = new User();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        final FirebaseUser userKey = mAuth.getCurrentUser();
        userId = userKey.getUid();
        userEmail = userKey.getEmail();
        //------------------------spinner---------------------------------------

        items.add("A+");
        items.add("A-");
        items.add("B+");
        items.add("B-");
        items.add("O+");
        items.add("O-");
        items.add("AB+");
        items.add("AB-");
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(newdonor.this, android.R.layout.simple_spinner_item, items);
        listAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bloodType.setAdapter(listAdapter);
        bloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBloodType = items.get(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showDialog("You have to select a Blood Type");
            }
        });

        //-------------------- retrieveData-------------------------------------------

        databaseReference.child("Donor").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.exists()) {
                        user = dataSnapshot.getValue(User.class);
                        retrieveValue();
                    } else {
                        Toast.makeText(newdonor.this, "No data for the user", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //-------------------------------------------------------------------------
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signOut();

                Intent intent = new Intent(newdonor.this, signinpage.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                storeData();
            }
        });
    }

    private void retrieveValue() {

            name.setText( user.getName());
            age.setText(String.valueOf(user.getAge()));
            weight.setText(String.valueOf(user.getWeight()));
            phone.setText(user.getPhone());
            email.setText(user.getEmail());

            if(user.getGender().equals("F"))
                gender.check(R.id.register_female);
            else
                gender.check(R.id.register_male);

        if (user.getDiseases().equals("Y"))
                disease.check(R.id.register_yes);
            else
                disease.check(R.id.register_no);

            for(int i =0 ; i< items.size(); i++){
                if(items.contains(user.getBloodType()))
                    bloodType.setSelection(i);
                else
                    bloodType.setSelection(0);
            }

    }

    private void storeData() {


        if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(age.getText()) && !TextUtils.isEmpty(phone.getText()) && !TextUtils.isEmpty(email.getText())) {
            int ageValue = Integer.valueOf(age.getText().toString());
            double weightValue = Double.valueOf(weight.getText().toString());
            if (ageValue > 65 || ageValue < 18) {

                showDialog("Your age should be from 18-65");
                progressBar.setVisibility(View.INVISIBLE);

            } else if (weightValue < 50) {
                showDialog("Your weight should be more than 50 Kg");
                progressBar.setVisibility(View.INVISIBLE);

            } else if (disease.getCheckedRadioButtonId() == R.id.register_yes) {
                progressBar.setVisibility(View.INVISIBLE);

                showDialog("Sorry, you can not donate while you have a diseases");
            } else {
                takeData();
            }

        } else {
            showDialog("Please fill your information");
        }

    }

    ///----store in datatbase ------
    private void takeData() {

        switch (selectedGenderID){
            case R.id.register_male:
                genderValue = "M";
                break;

            case R.id.register_female:
                genderValue = "F";
                break;
        }

                diseaseValue = "Yes";


        user.setName(name.getText().toString());
        user.setAge(Integer.valueOf(age.getText().toString()));
        user.setEmail(email.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setWeight(Double.valueOf(weight.getText().toString()));
        user.setGender(genderValue);
        user.setDiseases(diseaseValue);
        user.setBloodType(selectedBloodType);

        databaseReference.child("Donor").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(newdonor.this, "Saved", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(newdonor.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startActivity(new Intent(newdonor.this, profilepage.class));
    }

    //-----dialog method------
    private void showDialog(String s) {
        final Dialog builder = new Dialog(newdonor.this);
        builder.setContentView(R.layout.dialog);
        TextView message = builder.findViewById(R.id.textMessage);
        ImageView cancel;
        cancel = builder.findViewById(R.id.cancel);

        message.setText(s);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.show();

    }
}
