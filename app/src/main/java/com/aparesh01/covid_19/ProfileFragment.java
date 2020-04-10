package com.aparesh01.covid_19;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ImageView imgtv,covertv;
    TextView nametv, emailtv, phonetv;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String storage = "Users_Profile_Cover_Imags/";
    ProgressDialog pd;
    FloatingActionButton fab;
    Uri image_uri;
    private static final int CAMERA_REQUEST =100;
    private static final int STORAGE_REQUEST =200;
    private static final int IMAGE_REQUEST =300;
    private static final int GALLERY_REQUEST =400;

    String[] cameraPermission;
    String[] storagePermission;
    String check;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        imgtv = view.findViewById(R.id.pfdp);
        covertv = view.findViewById(R.id.coverpf);
        nametv = view.findViewById(R.id.pfname);
        emailtv = view.findViewById(R.id.pfemail);
        phonetv = view.findViewById(R.id.pfphone);
        pd = new ProgressDialog(getActivity());
        fab = view.findViewById(R.id.fab);
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = "" + ds.child("name").getValue();
                    String email = "" +ds.child("email").getValue();
                    String phone = "" + ds.child("job").getValue();
                    String img = "" + ds.child("image").getValue();
                    String cover = "" + ds.child("cover").getValue();

                    nametv.setText(name);
                    emailtv.setText(email);
                    phonetv.setText(phone);
                    try{

                        Picasso.get().load(img).into(imgtv);

                    }catch(Exception ignored){
                        Picasso.get().load(R.drawable.ic_add_a_photo).into(imgtv);
                    }
                    try{
                        Picasso.get().load(cover).into(covertv);
                    }catch(Exception ignored){
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        return view;
    }

    private boolean checkStoragePermission(){
        boolean g = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return g;
    }
    private void requestStoragePermission(){
        requestPermissions(storagePermission,STORAGE_REQUEST);
    }

    private boolean checkCameraPermission(){
        boolean g = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean g1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return g && g1;
    }
    private void requestCameraPermission(){
        requestPermissions(cameraPermission,CAMERA_REQUEST);
    }

    private void editProfile() {
        String[] options = {"Edit Profile Picture","Edit Cover Photo","Edit Name","Add Work Experience"};
        AlertDialog.Builder bilder = new AlertDialog.Builder(getActivity());
        bilder.setTitle("Choose Action");
        bilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(i ==0){
                    pd.setMessage("Wait a minute loading profile");
                    check = "image";
                    showDpDialog();
                }
                else if(i ==1){
                    pd.setMessage("Wait a minute loading profile");
                    check = "cover";
                    showDpDialog();

                }
                else if(i == 2){
                    pd.setMessage("Wait a minute loading profile");
                    showNameAndPhoneUpdateDialog("name");

                }
                else if(i ==3){
                    pd.setMessage("Wait a minute loading profile");
                    showNameAndPhoneUpdateDialog("job");
                }
            }
        });
        bilder.create().show();
    }

    private void showNameAndPhoneUpdateDialog(final String k) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit your " + k);
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter your " + k);
        editText.setMinEms(16);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString();
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String,Object> result = new HashMap<>();
                    result.put(k,value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), k+" updated...", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Please Enter " + k, Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pd.dismiss();
            }
        });
        builder.create().show();

    }

    private void showDpDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder bilder = new AlertDialog.Builder(getActivity());
        bilder.setTitle("Uploading From");
        bilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(i ==0){
//                    pd.setMessage("Wait a minute loading profile");
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }

                }
                else if(i ==1){
//                    pd.setMessage("Wait a minute loading profile");
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        bilder.create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean cameraA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageWrite = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraA && storageWrite) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{

                if(grantResults.length>0){
                    boolean storageWrite = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageWrite){
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){

            if(requestCode==IMAGE_REQUEST){

                setProfileCover(image_uri);
            }
            if(requestCode == GALLERY_REQUEST){
                image_uri = data.getData();
                setProfileCover(image_uri);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setProfileCover(Uri image_uri) {
       pd.show();
        String filePathAndName = storage + "" + check + " _ " + user.getUid();
        StorageReference storageReference1 = storageReference.child(filePathAndName);
        storageReference1.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if (uriTask.isSuccessful()){
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(check,downloadUri.toString());
                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Image Successfully Uploaded..", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Error! Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_REQUEST);
    }
}
