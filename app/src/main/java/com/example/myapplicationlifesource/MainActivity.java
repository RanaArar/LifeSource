package com.example.myapplicationlifesource;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    Button adminPage , SignIn , newDonor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adminPage = (Button) findViewById(R.id.admin_page);
        SignIn =(Button) findViewById(R.id.sign_in);
        newDonor = (Button) findViewById(R.id.new_donor);


        adminPage.setOnClickListener(this);
        SignIn.setOnClickListener(this);
        newDonor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = null;
        switch (v.getId()){

            case R.id.admin_page:
                intent = new Intent(MainActivity.this,adminpage.class);

                break;

            case R.id.sign_in:
                intent = new Intent(MainActivity.this,signinpage.class);

                break;

            case R.id.new_donor:
                intent = new Intent(MainActivity.this,newdonor.class);

                break;
        }

        startActivity(intent);
    }
}


