package com.meem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etPhoneNumber;
    private RadioGroup rgGender;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getCheckedRadioButtonId() == R.id.rbMale) {
                    gender = "male";
                } else {
                    gender = "female";
                }
            }
        });
    }

    public void register(View view) {
        boolean flag = true;
        if(etFullName.getText().toString().isEmpty()) {
            flag = false;
            etFullName.setError(getString(R.string.enter_full_name));
        }

        if(etPassword.getText().toString().isEmpty()) {
            flag = false;
            etPassword.setError(getString(R.string.enter_password));
        }
        if(etConfirmPassword.getText().toString().isEmpty()) {
            flag = false;
            etConfirmPassword.setError(getString(R.string.confirm_your_password));
        }
        if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            flag = false;
            etPassword.setError(getString(R.string.password_mismatch));
        }
        if(etPhoneNumber.getText().toString().isEmpty()) {
            flag = false;
            etPhoneNumber.setError(getString(R.string.enter_you_phone_number));
        }
        if(gender == null) {
            flag = false;
            Toast.makeText(RegistrationActivity.this,"Please select one of the choices below", Toast.LENGTH_LONG).show();
        }
        if(flag) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        MyModel myModel = new MyModel();
                        myModel.setUserId(user.getUid());
                        myModel.setEmail(etEmail.getText().toString());
                        myModel.setName(etFullName.getText().toString());
                        myModel.setGender(gender);
                        myModel.setUserType("user");
                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).setValue(myModel);
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            etPassword.setError(getString(R.string.weak_password));
                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            etEmail.setError(getString(R.string.invalid_credential));
                        } catch(FirebaseAuthUserCollisionException e) {
                            etEmail.setError(getString(R.string.user_exists));
                        } catch(Exception e) {
                            e.getCause();
                        }
                    }
                }
            });
        }
    }
}
