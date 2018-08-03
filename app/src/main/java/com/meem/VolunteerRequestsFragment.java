package com.meem;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chootdev.recycleclick.RecycleClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class VolunteerRequestsFragment extends Fragment {
    private ArrayList<MyModel> arrayList;
    private MyAdapter adapter;
    private String currentLang;
    private FirebaseUser user;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentLang = Resources.getSystem().getConfiguration().locale.getLanguage();
        View v = inflater.inflate(R.layout.fragment_volunteer_requests, null);
        final RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyModel myModel = dataSnapshot.getValue(MyModel.class);
                name = myModel.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.requests);
        arrayList = new ArrayList<MyModel>();
        adapter = new MyAdapter();
        FirebaseDatabase.getInstance().getReference().child("Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MyModel myModel = postSnapshot.getValue(MyModel.class);
                    arrayList.add(myModel);
                }
                if(rv.getAdapter() == null) {
                    rv.setAdapter(new MyAdapter());
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }
    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvTime;
        private TextView tvArea;
        private TextView tvService;
        private Button btnAccept;
        private Button btnViewDetails;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate_volunteer);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime_volunteer);
            tvArea = (TextView) itemView.findViewById(R.id.tvArea_volunteer);
            tvService = (TextView) itemView.findViewById(R.id.tvService_volunteer);
            btnAccept = (Button) itemView.findViewById(R.id.btnAccept);
            btnViewDetails = (Button) itemView.findViewById(R.id.btnViewDetails);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.cell_volunteer_home, parent, false);
            return new MyHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
            final MyModel myModel = arrayList.get(position);
            switch (currentLang) {
                case "en":
                    holder.tvDate.setText(getString(R.string.date)+" "+myModel.getEn().getDate());
                    holder.tvTime.setText(getString(R.string.time)+" "+myModel.getEn().getTime());
                    holder.tvArea.setText(getString(R.string.area)+" "+myModel.getEn().getAreaName());
                    holder.tvService.setText(getString(R.string.service)+" "+myModel.getEn().getServiceName());
                    break;
                case "ar":
                    holder.tvDate.setText(getString(R.string.date)+" "+myModel.getAr().getDate());
                    holder.tvTime.setText(getString(R.string.time)+" "+myModel.getAr().getTime());
                    holder.tvArea.setText(getString(R.string.area)+" "+myModel.getAr().getAreaName());
                    holder.tvService.setText(getString(R.string.service)+" "+myModel.getAr().getServiceName());
                    break;
                default:
                    holder.tvDate.setText(getString(R.string.date)+" "+myModel.getEn().getDate());
                    holder.tvTime.setText(getString(R.string.time)+" "+myModel.getEn().getTime());
                    holder.tvArea.setText(getString(R.string.area)+" "+myModel.getEn().getAreaName());
                    holder.tvService.setText(getString(R.string.service)+" "+myModel.getEn().getServiceName());
                    break;
            }
            if(myModel.getAccepted()) {
                holder.btnAccept.setTextColor(Color.GRAY);
            }
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!myModel.getAccepted()) {
                        FirebaseDatabase.getInstance().getReference().child("Counter").child("count").runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Integer count = mutableData.getValue(Integer.class);
                                if(count == null) {
                                    mutableData.setValue(1);
                                } else {
                                    mutableData.setValue(count + 1);
                                }
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                Integer count = dataSnapshot.getValue(Integer.class);
                                Map map = new HashMap();
                                map.put("Requests/"+myModel.getRequestId()+"/accepted", true);
                                map.put("Requests/"+myModel.getRequestId()+"/count", count);
                                Random rnd = new Random();
                                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                                String colorHex = String.format("#%06X", (0xFFFFFF & color));
                                map.put("Requests/"+myModel.getRequestId()+"/color", colorHex);
                                MyModel model = myModel;
                                model.setAcceptedById(user.getUid());
                                model.setAcceptedByName(name);
                                map.put("AcceptedRequests/"+myModel.getRequestId(), model);
                                FirebaseDatabase.getInstance().getReference().updateChildren(map);
                                adapter.notifyItemChanged(position);
                            }
                        });
                        holder.btnAccept.setTextColor(Color.GRAY);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}
