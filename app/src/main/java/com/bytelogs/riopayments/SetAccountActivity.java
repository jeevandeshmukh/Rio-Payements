package com.bytelogs.riopayments;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jeevandeshmukh.glidetoastlib.GlideToast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class SetAccountActivity extends BaseActivity {

    CircleImageView proimg;
    Uri imgUri = null;
    TextInputLayout name_layout,mobile_layout;
    EditText nameEt,mobileEt;
    Button savebtn;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    ProgressBar progressBar;
    FirebaseFirestore firebaseFirestore;
    String userid;
    Boolean isChanged = false;
    private UserViemModel userViemModel;


    public SetAccountActivity() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_account);
        proimg = findViewById(R.id.profile_image);
        name_layout = findViewById(R.id.name_layout);
        nameEt = findViewById(R.id.name);
        mobileEt = findViewById(R.id.mobile);
        savebtn = findViewById(R.id.save);
        mobile_layout = findViewById(R.id.mobile_layout);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progressBar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userid = mAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        userViemModel = ViewModelProviders.of(this).get(UserViemModel.class);

        Toast.makeText(SetAccountActivity.this,"Please Upload a picture and save it",Toast.LENGTH_SHORT).show();
        if(ConnectivityReceiver.isConnected()){
            retrieveUserInfo();
        }else {
            //Toast.makeText(this,"Your are offline,Showing you Cached data",Toast.LENGTH_SHORT).show();
            retriveUserInfoOffline();
        }

        proimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SetAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                        //granted
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(SetAccountActivity.this);

                    }else {
                        ActivityCompat.requestPermissions(SetAccountActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }


                }else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(SetAccountActivity.this);
                }
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameEt.getText().toString();
                if(isChanged) {

                    if (TextUtils.isEmpty(name)) {
                        name_layout.setError("Name can't be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(mobileEt.getText().toString())) {
                        mobile_layout.setError("Name can't be empty");
                        return;
                    }
                    if (imgUri == null) {
                        name_layout.setError("Please select a image");
                        return;
                    }
                    if (!TextUtils.isEmpty(name) && imgUri != null) {
                        progressBar.setVisibility(View.VISIBLE);


                        StorageReference imapath = storageReference.child("profile_images").child(userid + ".jpg");
                        imapath.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    storeUpload(task, name,mobileEt.getText().toString());


                                } else {
                                    String error = task.getException().getMessage();
                                    name_layout.setError(error);

                                }

                            }
                        });


                    }
                }else {
                    storeUpload(null,name,mobileEt.getText().toString());
                }
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(SetAccountActivity.this);

                } else {

                }
                return;

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgUri = result.getUri();
                proimg.setImageURI(imgUri);
                isChanged =true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public  void storeUpload(Task<UploadTask.TaskSnapshot> task, String name,String mobile){
        Toast.makeText(SetAccountActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();
        Uri downloaduri;
        if(task != null) {
            downloaduri = task.getResult().getDownloadUrl();
        }else {
            downloaduri = imgUri;
        }
        Map<String,String> usermap = new  HashMap<>();
        usermap.put("name",name);
        usermap.put("pro_image_url",downloaduri.toString());
        usermap.put("mobile",mobile);
        ///RoomDatabse insertion
        userViemModel.insertUser(new UserModel(String.valueOf(userid),name,mobile,downloaduri.toString()));

        Toast.makeText(SetAccountActivity.this,"Room Saved",Toast.LENGTH_SHORT).show();
        firebaseFirestore.collection("Users").document(userid).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);

                if(task.isSuccessful()){
                    Intent intent =new Intent(SetAccountActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    name_layout.setError(task.getException().getMessage());
                }

            }
        });

    }
    public void retrieveUserInfo(){
        firebaseFirestore.collection("Users").document(userid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String name = task.getResult().getString("name");
                        String pro_url = task.getResult().getString("pro_image_url");
                        String mobile = task.getResult().getString("mobile");
                        nameEt.setText(name);
                        imgUri = Uri.parse(pro_url);
                        mobileEt.setText(mobile);
                        Glide.with(SetAccountActivity.this).load(pro_url).into(proimg);

                    }

                }else {
                    name_layout.setError(task.getException().getMessage());
                }
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }
    public void retriveUserInfoOffline(){
        userViemModel.getUserList().observe(this, new Observer<List<UserModel>>() {
            @Override
            public void onChanged(@Nullable List<UserModel> userModels) {
                UserModel userModel = userModels.get(0);
                if(userModel != null) {
                    nameEt.setText(userModel.getName());
                    imgUri = Uri.parse(userModel.getProfile_url());
                    mobileEt.setText(userModel.getMobile());
                    Glide.with(SetAccountActivity.this).load(userModel.getProfile_url()).into(proimg);
                    progressBar.setVisibility(View.INVISIBLE);
                    new GlideToast.makeToast(SetAccountActivity.this, "Your Offline,Showing you cached data", 3000, GlideToast.CUSTOMTOAST, GlideToast.BOTTOM, R.drawable.ic_info_outline_black_24dp, "#ef5350").show();
                }

            }
        });

    }



}
