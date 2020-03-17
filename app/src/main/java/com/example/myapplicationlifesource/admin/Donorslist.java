package com.example.myapplicationlifesource.admin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.myapplicationlifesource.MainActivity;
import com.example.myapplicationlifesource.R;
import com.example.myapplicationlifesource.User;
import com.example.myapplicationlifesource.adapter.UserAdapter;
import com.example.myapplicationlifesource.admin.Donorsinfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Donorslist extends AppCompatActivity {

    private ListView listView;
    private ArrayList<User> arrayList;
    private UserAdapter userAdapter;
    private User user;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    Toolbar toolbar;
    TextView toolbarText;
    Button logout;
    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donorslist);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarText = findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        toolbarText.setText("Donors");
        logout = findViewById(R.id.logout_admin);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        user = new User();
        listView = findViewById(R.id.list);
        arrayList = new ArrayList<>();
        userAdapter = new UserAdapter(Donorslist.this, R.layout.list_item, arrayList);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();

                Intent intent = new Intent(Donorslist.this, MainActivity.class);
                startActivity(intent);
            }
        });
        databaseReference.child("Donor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    user = data.getValue(User.class);
                    userAdapter.addElement(user);


                }
                listView.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userAdapter.getItem(i);
                if (user != null) {
                    Intent intent = new Intent(Donorslist.this, Donorsinfo.class);
                    intent.putExtra("name", user.getName());
                    intent.putExtra("gender", user.getGender());
                    intent.putExtra("phone", user.getPhone());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("age", user.getAge());
                    intent.putExtra("disease", user.getDiseases());
                    intent.putExtra("weight", user.getWeight());
                    intent.putExtra("bloodType", user.getBloodType());
                    startActivity(intent);
                }
            }
        });


    }


}
