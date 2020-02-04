package com.example.myapplicationlifesource;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class newdonor extends AppCompatActivity {

    private EditText name , age,weight ,phone,email,password;

    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdonor);

        //-------------------------------------------------------------------------
        name = findViewById(R.id.register_name);
        age=  findViewById(R.id.register_age);
        weight= findViewById(R.id.register_weight);
        phone = findViewById(R.id.register_phone);
        email= findViewById(R.id.register_email);
        password= findViewById(R.id.register_password);
        submit = findViewById(R.id.submit_donor);

        //-------------------------------------------------------------------------

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(newdonor.this , profilepage.class));
            }
        });
    }
}
