package com.example.myapplicationlifesource.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplicationlifesource.JavaMail.SendMailAsyncTask;
import com.example.myapplicationlifesource.R;
import com.google.firebase.auth.FirebaseAuth;
import com.onesignal.OneSignal;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Scanner;

import es.dmoral.toasty.Toasty;

import static com.example.myapplicationlifesource.JavaMail.Config.EMAIL;
import static com.example.myapplicationlifesource.JavaMail.Config.sentToEmail;

public class Donorsinfo extends AppCompatActivity {

    private TextView name, age, phone, email, disease, weight;
    private ImageView gender, bloodType;
    private Button call;
    Toolbar toolbar;
    TextView toolbarText;
    ImageView back;
    //----------------------

    Intent intent;
    String user;
    String needType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorsinfo);
        // debug the OneSignal
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        OneSignal.sendTag("User_ID", EMAIL);
        //--------------------------------------------
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
        intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        age.setText(String.valueOf(intent.getIntExtra("age", 0)));
        phone.setText(intent.getStringExtra("phone"));
        email.setText(intent.getStringExtra("email"));
        disease.setText(intent.getStringExtra("disease"));

        weight.setText(String.valueOf(intent.getDoubleExtra("weight", 0)));

        if (intent.getStringExtra("gender").startsWith("F"))
            gender.setImageResource(R.drawable.female1);
        else
            gender.setImageResource(R.drawable.male1);


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

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendEmailAndNotification();
            }
        });

    }

    private void SendEmailAndNotification() {

        //--------------send email -------------


        needType = intent.getStringExtra("bloodType");
        String title = "We need you, LIFE SAVER";
        String content = "Dear donor , \n \n the hospital need certain amount of blood type (" + needType +
                ")\n\n you can save lives by coming to the hospital to donate your blood at this time.\n\n Regards" +
                "\n\n  Life Saver team";
        new SendMailAsyncTask(Donorsinfo.this, sentToEmail, title, content).execute();


        //--------------notification--------------

        sendNotification();

    }

    private void sendNotification() {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email = sentToEmail;


                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic Mjk1MjhkZmQtMjY3YS00ZTY2LTkxYTMtNDU3NDdjNzk5OTI5");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"bd9bb495-89b5-497f-ae50-d6a6d99c1aff\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"WE HAVE A NEED FOR YOUR BLOOD " + needType + " , PLEASE COME !!\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }
}
