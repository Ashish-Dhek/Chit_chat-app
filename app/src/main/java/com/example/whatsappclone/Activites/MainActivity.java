package com.example.whatsappclone.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.Adapters.friendListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    ArrayList<User> users;
    friendListAdapter adapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView= findViewById(R.id.recyclerView);

        database=FirebaseDatabase.getInstance();
        users= new ArrayList<>();
        adapter= new friendListAdapter(this,users);

        recyclerView.setAdapter(adapter);


        getDataOfUsers();





    }

    private void getDataOfUsers() {

        database.getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users.clear();

                        for(DataSnapshot snap: snapshot.getChildren())
                        {
                            User us= snap.getValue(User.class);

                                users.add(us);


                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.searchOption:
                Toast.makeText(this, "Search option is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.groupOption:
                Toast.makeText(this, "Groups option is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.inviteOption:
                Toast.makeText(this, "Invite option is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.settingsOption:
                Toast.makeText(this, "Setting option is selected", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,PhoneNumberActivity.class));
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.topmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}