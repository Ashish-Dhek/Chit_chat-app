package com.example.whatsappclone.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsappclone.Adapters.MessagesAdapter;
import com.example.whatsappclone.Models.MszModel;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class chatActivity extends AppCompatActivity {

    ImageView sendBtn;
    EditText mszTxt;
    String name,recieverId,senderId;
    MessagesAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<MszModel> messsages;
    FirebaseDatabase database;
    String senderRoom,recieverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name= getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recieverId= getIntent().getStringExtra("uId");
        senderId= FirebaseAuth.getInstance().getUid();
        recyclerView= findViewById(R.id.chatRecycler);
        sendBtn=findViewById(R.id.sentBox);
        mszTxt= findViewById(R.id.messageBox);
        messsages= new ArrayList<>();
        senderRoom= senderId+recieverId;
        recieverRoom= recieverId+senderId;

        adapter= new MessagesAdapter(this,messsages,senderRoom,recieverRoom);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);




        database= FirebaseDatabase.getInstance();

        extractAllMessages();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messsageSend();
            }
        });









    }

    private void extractAllMessages() {


        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messsages.clear();
                        for (DataSnapshot snap: snapshot.getChildren())
                        {
                            MszModel msz= snap.getValue(MszModel.class);
                            msz.setMessageId(snap.getKey());
                            messsages.add(msz);
                        }

                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    private void messsageSend() {

        String messageTxt= mszTxt.getText().toString();
        Date date= new Date();

        MszModel mssz= new MszModel(messageTxt, senderId, date.getTime(),-1);
        mszTxt.setText("");



        String randomAddres= database.getReference().push().getKey();

        Format formatter = new SimpleDateFormat("HH:mm");
        String s = formatter.format(date);
        HashMap<String, Object> lastMszObj= new HashMap<>();
        lastMszObj.put("lastMessage",mssz.getMessage());
        lastMszObj.put("lastMessageTime",s);


        database.getReference().child("chats").child(senderRoom).updateChildren(lastMszObj);
        database.getReference().child("chats").child(recieverRoom).updateChildren(lastMszObj);




        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .child(randomAddres)
                .setValue(mssz).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        database.getReference().child("chats")
                                .child(recieverRoom)
                                .child("messages")
                                .child(randomAddres)
                                .setValue(mssz).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(chatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

    }





    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}