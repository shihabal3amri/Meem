package com.meem;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VolunteerFinderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_finder);
        getSupportActionBar().hide();
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        TextView tvNumber = (TextView) findViewById(R.id.tvNumber);
        String color = getIntent().getExtras().getString("color");
        Integer count = getIntent().getExtras().getInt("count");
        rl.setBackgroundColor(Color.parseColor(color));
        tvNumber.setText(String.valueOf(count));

    }
}
