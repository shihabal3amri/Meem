package com.meem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {

    private EditText etServiceNameEn;
    private EditText etServiceNameAr;
    private Button btnAddService;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        etServiceNameEn = (EditText) findViewById(R.id.etServiceEn);
        etServiceNameAr = (EditText) findViewById(R.id.etServiceAr);
        btnAddService = (Button) findViewById(R.id.btnAddService);
        myRef = FirebaseDatabase.getInstance().getReference().child("Services");
        btnAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyModel myModel = new MyModel();
                MyModel mLang = new MyModel();
                mLang.setServiceName(etServiceNameEn.getText().toString());
                myModel.setEn(mLang);
                mLang = new MyModel();
                mLang.setServiceName(etServiceNameAr.getText().toString());
                myModel.setAr(mLang);
                String key = myRef.push().getKey();
                myModel.setServiceId(key);
                myRef.child(key).setValue(myModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });
    }
}
