package com.aparesh01.covid_19;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatFragment extends Fragment {



    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView worldcases,worldeaths,worldrecovered,icases,ideaths,irecovered;
    String wc,wd,wr,ic,id,ir;

    public StatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat, container, false);

        worldcases = view.findViewById(R.id.cases);
        worldeaths = view.findViewById(R.id.deaths);
        worldrecovered = view.findViewById(R.id.recovered);
        icases = view.findViewById(R.id.casesindia);
        ideaths = view.findViewById(R.id.deathsindia);
        irecovered = view.findViewById(R.id.recoveredindia);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Covid");

     // Query query = databaseReference.orderByChild("cs").equalTo("1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     wc = "" + dataSnapshot.child("wcases").getValue();
                     wd = "" + dataSnapshot.child("wdeaths").getValue();
                     wr = "" + dataSnapshot.child("wrecovered").getValue();
                     ic = "" + dataSnapshot.child("Icases").getValue();
                     id = "" + dataSnapshot.child("Ideaths").getValue();
                     ir = "" + dataSnapshot.child("Irecovered").getValue();
                worldcases.setText(wc);
                worldrecovered.setText(wr);
                worldeaths.setText(wd);
                icases.setText(ic);
                ideaths.setText(id);
                irecovered.setText(ir);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }
}
