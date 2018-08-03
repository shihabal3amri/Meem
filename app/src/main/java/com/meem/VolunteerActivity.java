package com.meem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VolunteerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);
        final TextView tvName = v.findViewById(R.id.tvName);
        final TextView tvEmail = v.findViewById(R.id.tvEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyModel myModel = dataSnapshot.getValue(MyModel.class);
                tvEmail.setText(myModel.getEmail());
                tvName.setText(myModel.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl, new VolunteerRequestsFragment(), "VRF");
        //navigationView.setCheckedItem(R.id.nav_home);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl, new VolunteerRequestsFragment(), "VRF");
            ft.commit();
        } else  if(id == R.id.nav_accepted){
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl, new VolunteerAcceptedRequestsFragment(), "VARF");
            ft.commit();
        } else if (id == R.id.nav_gallery) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(VolunteerActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
