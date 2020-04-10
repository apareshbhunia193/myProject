package com.aparesh01.covid_19;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aparesh01.covid_19.adapters.AdapterUser;
import com.aparesh01.covid_19.doc.DoctorsList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdviserFragment extends Fragment {

    RecyclerView rv;
    AdapterUser adapterUser;
    List<DoctorsList> doctorsLists;

    public AdviserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_adviser, container, false);
        rv = view.findViewById(R.id.doctors_list);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        doctorsLists = new ArrayList<>();

        getAllUsers();

        return view;
    }

    private void getAllUsers() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorsLists.clear();

                /*for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DoctorsList doctorsList = ds.getValue(DoctorsList.class);
                    if (!doctorsList.getUid().equals(firebaseUser.getUid())) {
                        doctorsLists.add(doctorsList);
                    }
                    adapterUser = new AdapterUser(getActivity(), doctorsLists);
                    rv.setAdapter(adapterUser);

                }*/
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DoctorsList doctorsList = ds.getValue(DoctorsList.class);
                    if (!doctorsList.getUid().equals(firebaseUser.getUid()) && doctorsList.getJob().equals("Doctor")) {
                        doctorsLists.add(doctorsList);
                    }
                    adapterUser = new AdapterUser(getActivity(), doctorsLists);
                    rv.setAdapter(adapterUser);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
