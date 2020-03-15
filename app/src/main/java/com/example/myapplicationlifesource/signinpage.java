package com.example.myapplicationlifesource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationlifesource.donor.profilepage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class signinpage extends AppCompatActivity {
    private EditText email, pass;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TextView toolbarText;
    Button signIn;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinpage);

        //-----------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.signin_email);
        pass = findViewById(R.id.signin_pass);
        progressBar = findViewById(R.id.signin_progressBar);
        signIn = findViewById(R.id.sign_donor);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        toolbarText = findViewById(R.id.toolbar_title);
        back = findViewById(R.id.back);
        toolbar.setTitle("");
        toolbarText.setText("Sign In");
        back.setImageResource(R.drawable.back);

        //-----------------------------------------------------
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signinpage.this, MainActivity.class));
            }
        });
        //-----------------------------------------------------
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailString = email.getText().toString();
                String passString = pass.getText().toString();
                if(!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passString)){


                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(emailString, passString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                if (emailString.equals("admin@h.com")) {
                                    Toasty.error(signinpage.this, "Sign in from Admin page");
                                } else {
                                    Intent intent = new Intent(signinpage.this, profilepage.class);
                                    startActivity(intent);
                                }
                            } else {
                                String errorMessage = task.getException().getMessage();
                                //  Toast.makeText(signinpage.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                Toasty.error(signinpage.this, "Error : " + errorMessage);

                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }

        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser= mAuth.getCurrentUser();
        if (currentUser!=null)
        {


            Intent intent = new Intent(signinpage.this , profilepage.class);
            startActivity(intent);
        }
    }
}
