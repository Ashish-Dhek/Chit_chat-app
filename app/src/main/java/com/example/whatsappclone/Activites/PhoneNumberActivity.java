package com.example.whatsappclone.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberActivity extends AppCompatActivity {

    Button continueButton;
    EditText phoneNumber;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        getSupportActionBar().hide();
        continueButton= findViewById(R.id.continueButton);
        phoneNumber= findViewById(R.id.phoneNumberText);

        auth= FirebaseAuth.getInstance();

        if(auth.getCurrentUser() !=null)
        {
            startActivity(new Intent(PhoneNumberActivity.this,MainActivity.class));
            finish();
        }


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(PhoneNumberActivity.this,OtpActivity.class);
                intent.putExtra("phone number","+91"+ phoneNumber.getText().toString());
                startActivity(intent);
            }
        });


    }
}