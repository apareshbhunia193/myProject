package com.aparesh01.covid_19;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {
    EditText emText,passText;
    Button sign;
    ProgressDialog pd;
    private FirebaseAuth mAuth;

/*    public SignUpActivity(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        emText = findViewById(R.id.emailEt);
        passText = findViewById(R.id.pass);
        mAuth = FirebaseAuth.getInstance();
        sign = findViewById(R.id.btn_sign);
        pd = new ProgressDialog(this);
        pd.setMessage("Creating Account...");
    }

    private void createAccount(String str_email, String str_pass) {
        pd.show();
        mAuth.createUserWithEmailAndPassword(str_email, str_pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("image","");
                            hashMap.put("cover","");
                            hashMap.put("name","");
                            hashMap.put("job","");
                            hashMap.put("uid",uid);
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
                            databaseReference.child(uid).setValue(hashMap);
                            Toast.makeText(SignUpActivity.this,"Account successfully created" + user.getEmail(),Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(SignUpActivity.this," "+e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }


    public void onSignIn(View view){
        String str_email = emText.getText().toString().trim();
        String str_pass = passText.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
            emText.setError("Invalid Email");
            emText.setFocusable(true);
        }
        else if(str_pass.length()<6){
            passText.setError("Minimum 6 characters required");
            passText.setFocusable(true);
        }
        else{
            createAccount(str_email,str_pass);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
