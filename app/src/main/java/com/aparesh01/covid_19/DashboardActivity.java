package com.aparesh01.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        firebaseAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_btm);
        bottomNavigationView.setOnNavigationItemSelectedListener(item);
        actionBar.setTitle("Home");
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.box,fragment1,"");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
 private BottomNavigationView.OnNavigationItemSelectedListener item = new BottomNavigationView.OnNavigationItemSelectedListener() {
     @Override
     public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
         switch (menuItem.getItemId()) {
             case R.id.home:
                 actionBar.setTitle("Home");
                 HomeFragment fragment1 = new HomeFragment();
                 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                 ft.replace(R.id.box,fragment1,"");
                 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                 ft.commit();
                 return true;
             case R.id.stat:
                 actionBar.setTitle("Statistic");
                 StatFragment fragment2 = new StatFragment();
                 FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                 ft1.replace(R.id.box,fragment2,"");
                 ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                 ft1.commit();
                 return true;
             case R.id.warzone:
                 actionBar.setTitle("Warning Zone");
                 WarzoneFragment frag = new WarzoneFragment();
                 FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                 ft3.replace(R.id.box,frag,"");
                 ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                 ft3.commit();
                 return true;
             case R.id.adviser:
                 actionBar.setTitle("Doctor Advice");
                 AdviserFragment frag1 = new AdviserFragment();
                 FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                 ft4.replace(R.id.box,frag1,"");
                 ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                 ft4.commit();
                 return true;
             case R.id.profile:
                 actionBar.setTitle("Profile");
                 ProfileFragment pf = new ProfileFragment();
                 FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                 ft5.replace(R.id.box,pf,"");
                 ft5.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                 ft5.commit();
                 return true;
         }
         return false;
     }
 };
    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
        }
        else{
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_logout: {
                firebaseAuth.signOut();
                checkUserStatus();
            }
            break;
            case R.id.chatwithall:{

                actionBar.setTitle("Chat");
                ChatsectionFragment cf = new ChatsectionFragment();
                FragmentTransaction ft6 = getSupportFragmentManager().beginTransaction();
                ft6.replace(R.id.box,cf,"");
                ft6.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft6.commit();
            }
            break;
            case R.id.add_post:{
                actionBar.setTitle("Add a new post");
                startActivity(new Intent(this,AddpostActivity.class));
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
/*
FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
FirebaseUser user = firebaseAuth.getCurrentUsers();
UserRecord userRecord = FirebaseAuth.getInstance.getUser(uid);
ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
while(page != null){
for(ExportedUserRecord user: page.getValues()){
        System.out.println("User: " + user.getUid());
    }
    page = page.getNextPage();
}
page = FirebaseAuth.getInstance().listUsers(null);
for(ExportedUserRecord user: page.iterateAll()){
    System.out.println("User : " + user.getUid());
}
 */