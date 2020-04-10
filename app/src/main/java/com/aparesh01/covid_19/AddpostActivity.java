package com.aparesh01.covid_19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aparesh01.covid_19.R.layout.*;
import com.aparesh01.covid_19.R.*;
import com.aparesh01.covid_19.doc.DoctorsList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.*;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

public class AddpostActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    EditText ptitle,pdesp;
    ImageView imageView;
    Button btn;
    String uname,uemail,uid,udp;
    Uri image_uri = null;
    ProgressDialog pd;
    public static final int CAMERA_REQUEST = 100;
    public static final int GALLERY_REQUEST = 200;
    public static final int IMAGE_PICK_CAMERA_CODE = 300;
    public static final int IMAGE_PICK_GALLERY_CODE = 400;
    
    String[] cameraPermission;
    String[] storagePermission;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpost);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add a new post");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        pd = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        ptitle = findViewById(R.id.post_title);
        pdesp = findViewById(R.id.post_descp);
        imageView = findViewById(id.post_img);
        user = firebaseAuth.getCurrentUser();
        btn = findViewById(id.postbtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ptitle.getText().toString();
                String desc = pdesp.getText().toString();
                if(TextUtils.isEmpty(title)){
                    Toast.makeText(AddpostActivity.this, "Please Enter the post title", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(desc)){
                    Toast.makeText(AddpostActivity.this, "Please Enter the post description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(image_uri == null){
                    uploadData(title,desc,"NoImage");

                }else{
                    uploadData(title,desc,String.valueOf(image_uri));
                }
            }
        });


        
        
        checkUserStatus();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(uemail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                   uname = "" + ds.child("name").getValue();
                    uemail = "" +ds.child("email").getValue();
                    udp = "" + ds.child("image").getValue();

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void uploadData(final String title, final String desc, String valueOf) {
        pd.setMessage("Uploading...");
        pd.show();
        final String timpstamp = String.valueOf(System.currentTimeMillis());
        String filePathandName = "Posts/" + "post_" + timpstamp;
        if(!valueOf.equals("NoImage")){
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathandName);
            ref.putFile(Uri.parse(valueOf))

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUri = uriTask.getResult().toString();
                            if(uriTask.isSuccessful()){


                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("uid",uid);
                                hashMap.put("uName",uname);
                                hashMap.put("uEmail",uemail);
                                hashMap.put("uDp",udp);
                                hashMap.put("pId",timpstamp);
                                hashMap.put("pTitle",title);
                                hashMap.put("pDesc",desc);
                                hashMap.put("pImage",downloadUri);
                                hashMap.put("pTime",timpstamp);
                                DatabaseReference dRef  =  FirebaseDatabase.getInstance().getReference("Posts");
                                dRef.child(timpstamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                Toast.makeText(AddpostActivity.this, "Post successfully published !!!", Toast.LENGTH_SHORT).show();
                                                ptitle.setText("");
                                                pdesp.setText("");
                                                imageView.setImageURI(null);
                                                image_uri = null;

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(AddpostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddpostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("uid",uid);
            hashMap.put("uName",uname);
            hashMap.put("uEmail",uemail);
            hashMap.put("uDp",udp);
            hashMap.put("pId",timpstamp);
            hashMap.put("pTitle",title);
            hashMap.put("pDesc",desc);
            hashMap.put("pImage","NoImage");
            hashMap.put("pTime",timpstamp);
            DatabaseReference dRef  =  FirebaseDatabase.getInstance().getReference("Posts");
            dRef.child(timpstamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(AddpostActivity.this, "Post successfully published !!!", Toast.LENGTH_SHORT).show();
                            ptitle.setText("");
                            pdesp.setText("");
                            imageView.setImageURI(null);
                            image_uri = null;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddpostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    private void showImageDialog() {
        
        String[] options = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Uploading from ");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromCamera();
                    }
                    
                }else if( i == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        
                        pickFromGallery();
                    }
                    
                }
            }
        });
        builder.create().show();
        
        }

    private void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {

        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);

    }

    private boolean checkStoragePermission(){
         boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){

        ActivityCompat.requestPermissions(this,storagePermission,GALLERY_REQUEST);
        
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){

        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        menu.findItem(id.add_post).setVisible(false);
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
    private void checkUserStatus(){
         user = firebaseAuth.getCurrentUser();
        if(user!=null){
            uemail = user.getEmail();
            uid = user.getUid();
        }
        else{
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera and Storage permission required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case GALLERY_REQUEST: {
                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show();
                    }
                }else {

                }
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                imageView.setImageURI(image_uri);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                imageView.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,DashboardActivity.class));
        super.onBackPressed();
    }
}
