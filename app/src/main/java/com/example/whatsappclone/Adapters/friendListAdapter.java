package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Activites.chatActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class friendListAdapter extends RecyclerView.Adapter<friendListAdapter.chatViewHolder>{

    Context context;
    ArrayList<User> users;

    public friendListAdapter(Context context, ArrayList<User> users){
        this.context= context;
        this.users= users;

    }

    @NonNull
    @Override
    public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.coversation,parent,false);
        return new chatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatViewHolder holder, int position) {

        User user= users.get(position);

        String senderId= FirebaseAuth.getInstance().getUid();
        String senderRoom= senderId+ user.getUid();

        FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(senderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.exists()) {
                                            String lastMessage = snapshot.child("lastMessage").getValue(String.class);
                                            String lastTime = snapshot.child("lastMessageTime").getValue(String.class);

                                            holder.lastTxt.setText(lastMessage);
                                            holder.time.setText(lastTime);
                                        }
                                        else
                                        {
                                            holder.lastTxt.setText("Tap to chat");
                                            holder.time.setText("");
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



        holder.frienname.setText(user.getName());


        Glide.with(context)
                .load(user.getProfileImg())
                .into(holder.friendImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, chatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("uId",user.getUid());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class chatViewHolder extends RecyclerView.ViewHolder{

        TextView frienname,lastTxt,time;
        ImageView friendImage;

        public chatViewHolder(@NonNull View itemView) {
            super(itemView);

            frienname=itemView.findViewById(R.id.nameTxt);
            lastTxt=itemView.findViewById(R.id.lastmsgText);
            time=itemView.findViewById(R.id.timeTxt);
            friendImage= itemView.findViewById(R.id.friendImg);


        }
    }
}
