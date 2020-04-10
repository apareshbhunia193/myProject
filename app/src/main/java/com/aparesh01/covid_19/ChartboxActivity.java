package com.aparesh01.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aparesh01.covid_19.R.layout;
import com.aparesh01.covid_19.adapters.AdapterChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChartboxActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profilepic;
    TextView name_doc,status_doc;
    EditText msgt;
    ImageView sendbtn;
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    List<ModelChat> chatList;
    AdapterChat adapterChat;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String docUid;
    String myUid;
    String dpImag;
    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        setContentView(R.layout.activity_chartbox);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name_doc = findViewById(R.id.docnam_chat);
        status_doc = findViewById(R.id.status_char);
        profilepic = findViewById(R.id.profilepic_chat);
        msgt = findViewById(R.id.meaasge_chart);
        sendbtn = findViewById(R.id.sendbtn_chart);
        recyclerView = findViewById(R.id.recycleview_chart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        docUid = intent.getStringExtra("docUid");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        Query userQuery = databaseReference.orderByChild("uid").equalTo(docUid);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String name = (String) ds.child("name").getValue();
                    dpImag = (String) ds.child("image").getValue();
                    name_doc.setText(name);
                    try{

                        Picasso.get().load(dpImag).into(profilepic);

                    }catch(Exception e){


                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = msgt.getText().toString();
                if(TextUtils.isEmpty(message)){

                    Toast.makeText(ChartboxActivity.this, "Empty message can't send", Toast.LENGTH_SHORT).show();
                }else {

                    sendMessage(message);

                }
            }
        });
        readMessage();
        seenMsg();
    }

    private void seenMsg() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");

        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(docUid)){

                        HashMap<String, Object> seenHashMap = new HashMap<>();
                        seenHashMap.put("isSeen",true);
                        ds.getRef().updateChildren(seenHashMap);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage() {
        chatList = new ArrayList<>();
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference("Chats");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(docUid) ||
                            chat.getReceiver().equals(docUid) && chat.getSender().equals(myUid)){
                        chatList.add(chat);
                    }

                    adapterChat = new AdapterChat(ChartboxActivity.this,chatList,dpImag);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String message) {

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference();

        String timestemp = String.valueOf(System.currentTimeMillis());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver",docUid);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestemp);
        hashMap.put("isSeen",false);
        dRef.child("Chats").push().setValue(hashMap);

        msgt.setText("");

        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist").child(myUid).child(docUid);

        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){

                    chatRef1.child("id").setValue(docUid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("Chatlist").child(docUid).child(myUid);

        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){

                    chatRef2.child("id").setValue(myUid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkUserStatus(){

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user!= null){
            myUid = user.getUid();
        }
        else {

            startActivity(new Intent(this,MainActivity.class));
            finish();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        menu.findItem(R.id.chatwithall).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener );
    }
}
