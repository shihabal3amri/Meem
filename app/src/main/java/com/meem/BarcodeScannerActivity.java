package com.meem;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private DatabaseReference myRef;
    private MyModel myModel;
    private String serviceId;
    private String serviceNameEn;
    private String serviceNameAr;
    private FirebaseUser user;
    private String name;
    private String email;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(BarcodeScannerActivity.this);
        getSupportActionBar().setTitle("QR Scanner");
        myRef =  FirebaseDatabase.getInstance().getReference().child("Requests");
        MyModel myModel = getIntent().getExtras().getParcelable("service");
        serviceId = myModel.getServiceId();
        serviceNameEn = myModel.getEn().getServiceName();
        serviceNameAr = myModel.getAr().getServiceName();
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               MyModel myModel = dataSnapshot.getValue(MyModel.class);
               name = myModel.getName();
               email = myModel.getEmail();
               phone = myModel.getPhoneNo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setContentView(mScannerView);

    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    @Override
    public void handleResult(Result result) {
        FirebaseDatabase.getInstance().getReference().child("QR").orderByChild("qrCode").equalTo(result.getText()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    forLoop:
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        MyModel myModel = postSnapshot.getValue(MyModel.class);
                        MyModel mm = new MyModel();
                        mm.setServiceId(serviceId);
                        mm.setAccepted(false);
                        mm.setUserId(user.getUid());
                        mm.setEmail(email);
                        mm.setName(name);
                        mm.setStatusCode(0);
                        Locale arabicLocale = new Locale("ar");
                        DateFormat dfDate = DateFormat.getDateInstance(DateFormat.FULL, arabicLocale);
                        DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.MEDIUM, arabicLocale);
                        MyModel mLang = new MyModel();
                        mLang.setServiceName(serviceNameAr);
                        mLang.setAreaName(myModel.getAr().getAreaName());
                        mLang.setDate(dfDate.format(new Date()));
                        mLang.setTime(dfTime.format(new Date()));
                        mm.setAr(mLang);

                        mLang = new MyModel();
                        dfDate = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH);
                        dfTime = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                        mLang.setDate(dfDate.format(new Date()));
                        mLang.setTime(dfTime.format(new Date()));
                        mLang.setServiceName(serviceNameEn);
                        mLang.setAreaName(myModel.getEn().getAreaName());
                        mm.setEn(mLang);

                        String key = myRef.push().getKey();
                        mm.setRequestId(key);
                        myRef.child(key).setValue(mm);
                        Toast.makeText(BarcodeScannerActivity.this, R.string.request_added,Toast.LENGTH_LONG).show();
                        break forLoop;
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScannerView.resumeCameraPreview(BarcodeScannerActivity.this);
                            finish();
                        }
                    }, 2000);
                } else {
                    Toast.makeText(BarcodeScannerActivity.this, R.string.invalid_qr_code,Toast.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScannerView.resumeCameraPreview(BarcodeScannerActivity.this);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
