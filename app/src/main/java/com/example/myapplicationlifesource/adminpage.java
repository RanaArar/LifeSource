package com.example.myapplicationlifesource;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class adminpage extends AppCompatActivity {

    Button signIn;
    EditText email, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        signIn  = findViewById(R.id.sign_adimn);
        email = findViewById(R.id.email_admin);
        pass = findViewById(R.id.pass_admin);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(adminpage.this , Donorslist.class));
            }
        });

    }
}
