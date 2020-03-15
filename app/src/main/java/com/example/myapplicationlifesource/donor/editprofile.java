package com.example.myapplicationlifesource.donor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationlifesource.R;
import com.example.myapplicationlifesource.User;
import com.example.myapplicationlifesource.donor.profilepage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class editprofile extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    User user;
    String userId;
    FirebaseAuth mAuth;
    private EditText newPass, email, pass;
    private Button save;
    private Toolbar toolbar;
    private TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarText = findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        toolbarText.setText("Change Password");
        //------------------------Database---------------------------------------

        mAuth = FirebaseAuth.getInstance();

        user = new User();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        final FirebaseUser userKey = mAuth.getCurrentUser();
        userId = userKey.getUid();
        email = findViewById(R.id.edit_email);
        newPass = findViewById(R.id.edit_new_pass);
        pass = findViewById(R.id.edi_pass);
        save = findViewById(R.id.edit_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = email.getText().toString();
                String passString = pass.getText().toString();
                final String newPassString = newPass.getText().toString();
                if (!TextUtils.isEmpty(emailString) && !TextUtils.isEmpty(passString) && !TextUtils.isEmpty(newPassString)) {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(emailString, passString);

// Prompt the user to re-provide their sign-in credentials
                    userKey.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        userKey.updatePassword(newPassString).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //Toast.makeText(editprofile.this, "password Updated", Toast.LENGTH_SHORT).show();
                                                    Toasty.success(editprofile.this, "password Updated");
                                                } else {
                                                    //  Toast.makeText(editprofile.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                                    Toasty.error(editprofile.this, "Error password not updated");
                                                }
                                            }
                                        });
                                    } else {
                                        //  Toast.makeText(editprofile.this, "Error auth failed", Toast.LENGTH_SHORT).show();
                                        Toasty.error(editprofile.this, "Error auth failed");
                                    }
                                }
                            });
                }

                   /* databaseReference.child("Donor").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(editprofile.this, "تم حفظ المعلومات ", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(editprofile.this, "فشل في حفظ المعلومات ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });*/
                startActivity(new Intent(editprofile.this, profilepage.class));
            }

        });

    }
}
