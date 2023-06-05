package com.example.whatsappclone.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {



    EditText otpNumber;
    Button verifyButton;
    String number,verificationID;
    FirebaseAuth auth;
    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        getSupportActionBar().hide();
        otpNumber= findViewById(R.id.otpText);
        verifyButton= findViewById(R.id.verifyButton);
        auth=FirebaseAuth.getInstance();
        number=getIntent().getStringExtra("phone number");

        generateOTP();




            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    verifyOTP(otpNumber.getText().toString());
                }
            });



    }

    private void verifyOTP(String otp) {

        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationID,otp);

        signinbyCredentials(credential);

    }

    private void signinbyCredentials(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    FirebaseDatabase.getInstance().getReference()
                                    .child("users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snap: snapshot.getChildren())
                                    {
                                        User u= snap.getValue(User.class);
                                        if(u.getPhoneNumber().equals(number))
                                        {
                                            startActivity(new Intent(OtpActivity.this,MainActivity.class));
                                            finishAffinity();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                       startActivity(new Intent(OtpActivity.this, userProfileSetup.class));
                       finishAffinity();


                }
                else
                {
                    Toast.makeText(OtpActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void generateOTP() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


  private   PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

          final String code= credential.getSmsCode();

            if(code!= null)
            {
                verifyOTP(code);
            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(OtpActivity.this, "verification failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {

                super.onCodeSent(verificationId,token);

                verificationID=verificationId;

        }
    };




}