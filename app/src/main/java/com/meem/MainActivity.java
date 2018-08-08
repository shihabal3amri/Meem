package com.meem;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String currentLang;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentLang = Resources.getSystem().getConfiguration().locale.getLanguage();
        Locale locale = new Locale(currentLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                if(user!=null) {
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("Users");
                    String id = user.getUid();
                    myRef.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MyModel myModel = dataSnapshot.getValue(MyModel.class);
                            if(myModel.getUserType().equals("user")) {
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            }
                            else {
                                startActivity(new Intent(MainActivity.this, VolunteerActivity.class));
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, secondsDelayed * 1000);
    }
}
