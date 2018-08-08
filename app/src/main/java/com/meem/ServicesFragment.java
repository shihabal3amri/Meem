package com.meem;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ServicesFragment extends Fragment {
    private ArrayList<MyModel> arrayList;
    private MyAdapter myAdapter;
    private ListView rv;
    private String currentLang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentLang = Resources.getSystem().getConfiguration().locale.getLanguage();
        View v = inflater.inflate(R.layout.fragment_services, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.services);
        arrayList = new ArrayList<MyModel>();
        myAdapter = new MyAdapter();
        rv = (ListView) v.findViewById(R.id.rv);
        FirebaseDatabase.getInstance().getReference().child("Services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MyModel myModel = postSnapshot.getValue(MyModel.class);
                    arrayList.add(myModel);
                }
                if(rv.getAdapter() == null) {
                    rv.setAdapter(myAdapter);
                } else {
                    myAdapter.notifyDataSetChanged();
                }
                rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        PermissionListener permissionlistener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                MyModel mm = arrayList.get(i);
                                Intent intent = new Intent(getActivity(), BarcodeScannerActivity.class);
                                intent.putExtra("service", mm);
                                startActivity(intent);
                                //Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

                            }
                        };
                        TedPermission.with(getActivity())
                                .setPermissionListener(permissionlistener)
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(android.Manifest.permission.CAMERA)
                                .check();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(android.R.layout.simple_list_item_1, null);
            TextView tvService = (TextView) v.findViewById(android.R.id.text1);
            MyModel myModel = arrayList.get(i);
            switch (currentLang) {
                case "en":
                    tvService.setText(myModel.getEn().getServiceName());
                    break;
                case "ar":
                    tvService.setText(myModel.getAr().getServiceName());
                    break;
                default:
                    tvService.setText(myModel.getEn().getServiceName());
                    break;
            }
            return v;
        }
    }
}
