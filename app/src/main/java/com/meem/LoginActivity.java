package com.meem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.white)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#53d397'>Login</font>"));
        getSupportActionBar().hide();
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    public void login(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getString(R.string.logging_in));
        progressDialog.show();
        boolean flag = true;
        if(etEmail.getText().toString().isEmpty()) {
            flag = false;
            etEmail.setError(getString(R.string.enter_email));
        }
        if(etPassword.getText().toString().isEmpty()) {
            flag = false;
            etEmail.setError(getString(R.string.enter_password));
        }
        if(flag) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                MyModel myModel = dataSnapshot.getValue(MyModel.class);
                                if(myModel.getUserType().equals("user")) {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(LoginActivity.this, VolunteerActivity.class));
                                    finish();
                                }
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                progressDialog.dismiss();
                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            etEmail.setError(getString(R.string.invalid_credential));
                        } catch(Exception e) {

                        }
                    }
                }
            });
        } else {
            progressDialog.dismiss();
        }
    }
}
