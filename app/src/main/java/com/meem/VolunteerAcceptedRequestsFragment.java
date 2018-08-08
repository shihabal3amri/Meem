package com.meem;

import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.TextView;

import com.chootdev.recycleclick.RecycleClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class VolunteerAcceptedRequestsFragment extends Fragment {
    private ArrayList<MyModel> arrayList;
    private MyAdapter adapter;
    private String currentLang;
    private FirebaseUser user;
    private String name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentLang = Resources.getSystem().getConfiguration().locale.getLanguage();
        View v = inflater.inflate(R.layout.fragment_my_requests, null);
        final RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.accepted_requests);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        arrayList = new ArrayList<MyModel>();
        adapter = new MyAdapter();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("AcceptedRequests").orderByChild("acceptedById").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MyModel myModel = postSnapshot.getValue(MyModel.class);
                    arrayList.add(myModel);
                }
                Collections.reverse(arrayList);
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
        RecycleClick.addTo(rv).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                MyModel myModel = arrayList.get(position);
                Intent intent = new Intent(getActivity(), VolunteerFinderActivity.class);
                intent.putExtra("color", myModel.getColor());
                intent.putExtra("count", myModel.getCount());
                startActivity(intent);
            }
        });
        return v;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvTime;
        private TextView tvArea;
        private TextView tvStatus;
        private TextView tvService;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvArea = (TextView) itemView.findViewById(R.id.tvArea);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatusData);
            tvService = (TextView) itemView.findViewById(R.id.tvService);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.custom_list_accepted, parent, false);
            return new MyHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            MyModel myModel = arrayList.get(position);
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

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}
