package com.aparesh01.covid_19;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aparesh01.covid_19.adapters.AdapterChatlist;
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
public class ChatsectionFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView rv;
    List<Chatlist> chatlistList;
    List<DoctorsList> doctorsLists;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    AdapterChatlist adapterChatlist;

    public ChatsectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chatsection, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rv = view.findViewById(R.id.recycleview_chartlist);
        chatlistList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlistList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    Chatlist cl = ds.getValue(Chatlist.class);
                    chatlistList.add(cl);
                }

                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void loadChats() {
        doctorsLists = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                doctorsLists.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    DoctorsList dl = ds.getValue(DoctorsList.class);
                    for(Chatlist cl : chatlistList){
                        if(dl.getUid() != null && dl.getUid().equals(cl.getId())){

                            doctorsLists.add(dl);
                            break;
                        }

                    }

                    adapterChatlist = new AdapterChatlist(getContext(),doctorsLists);
                    rv.setAdapter(adapterChatlist);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cheUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){

        }else {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }

    }

    @Override
    public void onStart() {
        cheUserStatus();
        super.onStart();
    }
    
}
