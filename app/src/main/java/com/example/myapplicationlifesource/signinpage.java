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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signinpage extends AppCompatActivity {
    private EditText email, pass;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private TextView toolbarText;
    Button signIn;
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

        toolbar.setTitle("");
        toolbarText.setText("Sign In");

        //-----------------------------------------------------
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                String passString = pass.getText().toString();
                if(!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passString)){


                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(emailString,passString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){


                                Intent intent = new Intent(signinpage.this , profilepage.class);
                                startActivity(intent);
                            }else{
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(signinpage.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


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
