package com.example.myapplicationlifesource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class adminpage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private TextView toolbarText;
    Button signIn;
    EditText email, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        mAuth = FirebaseAuth.getInstance();
        signIn  = findViewById(R.id.sign_adimn);
        email = findViewById(R.id.email_admin);
        pass = findViewById(R.id.pass_admin);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarText = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText("Sign In");


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                String passString = pass.getText().toString();
                if (!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passString)) {

                    if (emailString.equals("admin@h.com") && passString.equals("000000")) {

                        mAuth.signInWithEmailAndPassword(emailString, passString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    startActivity(new Intent(adminpage.this, Donorslist.class));

                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    //  Toast.makeText(adminpage.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                    Toasty.error(adminpage.this, "Error : " + errorMessage);

                                }
                            }
                        });
                    } else {
                        //Toast.makeText(adminpage.this, "you are not an admin", Toast.LENGTH_LONG).show();
                        Toasty.info(adminpage.this, "you are not an admin");
                    }
                }

            }
        });

    }
}
