package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.MszModel;
import com.example.whatsappclone.R;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<MszModel> messages;
    String senderRoom, recieverRoom;

    final int ITEM_SENT=1, ITEM_RECIEVE=2;


    public MessagesAdapter(Context context, ArrayList<MszModel> messages, String senderRoom, String recieverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.recieverRoom = recieverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType== ITEM_SENT)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.text_send,parent,false);
                return new SentViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.text_recieve,parent,false);
            return new RecieverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {

        MszModel message= messages.get(position);

        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId()))
        {
            return ITEM_SENT;
        }
        else
            return ITEM_RECIEVE;

    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MszModel message= messages.get(position);



        int react[]= new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(react)
                .build();


        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

            if(holder.getClass()== RecieverViewHolder.class)
            {
                RecieverViewHolder viewHolder= (RecieverViewHolder) holder;
                viewHolder.recieveEmoji.setImageResource(react[pos]);
                viewHolder.recieveEmoji.setVisibility(View.VISIBLE);
                message.setEmoji(pos);

                FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(message.getMessageId())
                        .setValue(message);

                FirebaseDatabase.getInstance().getReference()
                        .child("chats")
                        .child(recieverRoom)
                        .child("messages")
                        .child(message.getMessageId())
                        .setValue(message);

            }


            return true;
        });





        if(holder.getClass()== SentViewHolder.class)
            {
                SentViewHolder viewHolder= (SentViewHolder) holder;

                viewHolder.sendMessage.setText(message.getMessage());
                if(message.getEmoji()>=0)
                {
                    viewHolder.sendEmoji.setImageResource(react[message.getEmoji()]);
                  viewHolder.sendEmoji.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewHolder.sendEmoji.setVisibility(View.INVISIBLE);

                }


            }
            else
            {
               RecieverViewHolder viewHolder= (RecieverViewHolder) holder;

                viewHolder.recieveMessage.setText(message.getMessage());

                if(message.getEmoji()>=0)
                {
                    viewHolder.recieveEmoji.setImageResource(react[message.getEmoji()]);
                    viewHolder.recieveEmoji.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewHolder.recieveEmoji.setVisibility(View.INVISIBLE);
                }

                viewHolder.recieveMessage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        popup.onTouch(view,motionEvent);
                        return false;
                    }
                });
            }




    }

    @Override
    public int getItemCount() {
        return messages.size();
    }







    public class SentViewHolder extends RecyclerView.ViewHolder{

        TextView sendMessage;
        ImageView sendEmoji;


        public SentViewHolder(@NonNull View itemView) {
            super(itemView);

            sendMessage= itemView.findViewById(R.id.sendMsz);
            sendEmoji= itemView.findViewById(R.id.emojiSend);
        }
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView recieveMessage;
        ImageView recieveEmoji;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            recieveMessage= itemView.findViewById(R.id.recieveMsz);
            recieveEmoji= itemView.findViewById(R.id.emojiRecive);
        }
    }

}
