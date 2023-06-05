package com.example.whatsappclone.Activites;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class userProfileSetup extends AppCompatActivity {

    ImageView userImg;
    EditText userName;
    Button saveBtn;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Uri uri;
    ProgressDialog dialog;



    ActivityResultLauncher<String> activityResultLauncher= registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                      if(result!= null)
                      {
                          uri=result;
                          userImg.setImageURI(result);
                      }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_setup);

        getSupportActionBar().hide();

        userImg= findViewById(R.id.userImage);
        userName= findViewById(R.id.userName);
        saveBtn= findViewById(R.id.saveProfileBtn);
        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog= new ProgressDialog(this);
        dialog.setMessage("Updating Profile.....");
        dialog.setCancelable(false);


        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activityResultLauncher.launch("image/*");

            }
        });



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name= userName.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(userProfileSetup.this, "Please Enter the name", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.show();

                if(uri== null)
                {
                    Toast.makeText(userProfileSetup.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
                else if(uri !=null)
                {

                    uploadImageTostorgae(name);

                }
            }
        });



    }

    private void uploadImageTostorgae(String name) {


        StorageReference reference= storage.getReference().child("Profiles").child(auth.getUid());

        reference.putFile(uri).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                reference.getDownloadUrl().addOnSuccessListener(u -> {


                    String imageUrl= u.toString();
                    String uId= auth.getUid();
                    String userPhoneNumber= auth.getCurrentUser().getPhoneNumber();



                    User user= new User(uId,name,userPhoneNumber,imageUrl);


                    database.getReference()
                            .child("users")
                            .child(uId)
                            .setValue(user)
                            .addOnSuccessListener(unused -> {
                                dialog.dismiss();
                                startActivity(new Intent(userProfileSetup.this,MainActivity.class));
                                finish();
                            });
                });
            }
        });

    }
}