package com.example.myapplicationlifesource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText email, pass, confirmPass;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TextView toolbarText;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //-----------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register_donor);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        progressBar = findViewById(R.id.register_progressBar);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        toolbarText = findViewById(R.id.toolbar_title);
        back = findViewById(R.id.back);
        back.setImageResource(R.drawable.back);
        toolbar.setTitle("");
        toolbarText.setText("Register New Donor");

        //-----------------------------------------------------
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        //-----------------------------------------------------

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = email.getText().toString();
                String passString = pass.getText().toString();
                String ConfirmPassString = confirmPass.getText().toString();

                if (!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passString) && !TextUtils.isEmpty(ConfirmPassString)) {

                    if (passString.equals(ConfirmPassString)) {

                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(emailString, passString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    Intent setupIntent = new Intent(RegisterActivity.this, newdonor.class);
                                    startActivity(setupIntent);
                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    //     Toast.makeText(RegisterActivity.this, "Error :" + errorMessage, Toast.LENGTH_LONG).show();
                                    Toasty.error(RegisterActivity.this, "Error :" + errorMessage);
                                    finish();
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        });

                    } else {

                        Toasty.info(RegisterActivity.this, "Confirm Password doesn't match the password .. Type again");
                        // Toast.makeText(RegisterActivity.this, "Confirm Password doesn't match the password .. Type again", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            Intent intent = new Intent(RegisterActivity.this, newdonor.class);
            startActivity(intent);

        }

    }
}
