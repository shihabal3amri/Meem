package com.meem;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentLang = Resources.getSystem().getConfiguration().locale.getLanguage();
        Locale locale = new Locale(currentLang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MyModel myModel = dataSnapshot.getValue(MyModel.class);
                    if(myModel.getUserType().equals("user")) {
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, VolunteerActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}
