package com.example.evelyn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessagesViewHolder> {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private String currentUser;

    private List<Messages> userMessagesList;

    public MessageAdapter(List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;
    }


    public class MessagesViewHolder extends RecyclerView.ViewHolder{

        private TextView senderMessageText, receiverMessageText;
        private CircleImageView circleImageView;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = (TextView) itemView.findViewById(R.id.sender_massage_textViewid);
            receiverMessageText =(TextView) itemView.findViewById(R.id.receiver_message_text_view_id);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.message_image_view);
        }
    }



    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_message,parent,false);
        mAuth = FirebaseAuth.getInstance();
        return new MessagesViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MessagesViewHolder holder, int position) {

        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);
        String fromUserId = messages.getFrom();
        String fromMessageType = messages.getType();

        userRef  = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String receiverImage = dataSnapshot.child("imageUrl").getValue().toString();
                Picasso.get().load(receiverImage).into(holder.circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(fromMessageType.equals("text")){

            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.circleImageView.setVisibility(View.INVISIBLE);
            holder.senderMessageText.setVisibility(View.INVISIBLE);

            if(fromUserId.equals(messageSenderId)){
                holder.senderMessageText.setVisibility(View.VISIBLE);
                holder.senderMessageText.setBackgroundResource(R.drawable.sender_background);
                holder.senderMessageText.setText(messages.getMessage());

            }

            else{

                holder.receiverMessageText.setVisibility(View.VISIBLE);
                holder.circleImageView.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setBackgroundResource(R.drawable.receiver_background);
                holder.receiverMessageText.setText(messages.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }






}
