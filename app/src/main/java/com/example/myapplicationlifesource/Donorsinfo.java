package com.example.myapplicationlifesource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Donorsinfo extends AppCompatActivity {

    private TextView name, age, phone, email, disease, weight;
    private ImageView gender, bloodType;
    private Button call;
    Toolbar toolbar;
    TextView toolbarText;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorsinfo);
        name = findViewById(R.id.retreive_name);
        age = findViewById(R.id.retreive_age);
        phone = findViewById(R.id.retreive_phone);
        email = findViewById(R.id.retreive_email);
        disease = findViewById(R.id.retreive_disease);
        weight = findViewById(R.id.retreive_weight);
        gender = findViewById(R.id.retreive_gender);
        bloodType = findViewById(R.id.retreive_blood);
        call = findViewById(R.id.call_donor);
        back = findViewById(R.id.back);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarText = findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        toolbarText.setText("Donor Information");
        back.setImageResource(R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Donorsinfo.this, Donorslist.class));
            }
        });
        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        age.setText(String.valueOf(intent.getIntExtra("age", 0)));
        phone.setText(intent.getStringExtra("phone"));
        email.setText(intent.getStringExtra("email"));
        disease.setText(intent.getStringExtra("disease"));

        weight.setText(String.valueOf(intent.getDoubleExtra("weight", 0)));

        if (intent.getStringExtra("gender").equals("F"))
            gender.setImageResource(R.drawable.female);
        else
            gender.setImageResource(R.drawable.male);


        switch (intent.getStringExtra("bloodType")) {
            case "AB-":
                bloodType.setImageResource(R.drawable.bloodab);
                break;
            case "AB+":
                bloodType.setImageResource(R.drawable.bloodabplus);
                break;
            case "A":
                bloodType.setImageResource(R.drawable.bloodaplus);
                break;
            case "A-":
                bloodType.setImageResource(R.drawable.blooda);
                break;
            case "B+":
                bloodType.setImageResource(R.drawable.bloodbplus);
                break;
            case "B-":
                bloodType.setImageResource(R.drawable.bloodb);
                break;
            case "O-":
                bloodType.setImageResource(R.drawable.bloodo);
                break;
            case "O+":
                bloodType.setImageResource(R.drawable.bloodoplus);
                break;

        }


    }
}
