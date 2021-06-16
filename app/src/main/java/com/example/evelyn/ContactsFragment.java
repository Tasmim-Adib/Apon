package com.example.evelyn;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private View chatsView;
    private RecyclerView chatRecycler;

    private FirebaseAuth mAuth;
    private DatabaseReference messageRef,userRef;
    private String currentUser;
    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatsView =  inflater.inflate(R.layout.fragment_contacts, container, false);
        chatRecycler = (RecyclerView) chatsView.findViewById(R.id.message_list_id);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecycler.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid().toString();
        messageRef = FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUser);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        return chatsView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Upload> options =
                new FirebaseRecyclerOptions.Builder<Upload>()
                .setQuery(messageRef,Upload.class)
                .build();

        FirebaseRecyclerAdapter<Upload,  ChatViewHolder> adapter =
                new FirebaseRecyclerAdapter<Upload, ChatViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatViewHolder chatViewHolder, int i, @NonNull Upload upload) {

                        final String userId = getRef(i).getKey();

                        userRef.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String imageurl = dataSnapshot.child("imageUrl").getValue().toString();
                                final String name = dataSnapshot.child("name").getValue().toString();

                                chatViewHolder.nameText.setText(name);
                                chatViewHolder.messageText.setText("Last Seen :");
                                Picasso.get().load(imageurl).into(chatViewHolder.imageView);


                                chatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent chatIntent = new Intent(getActivity(),ChatActivity.class);
                                        chatIntent.putExtra("visit_user_id",userId);
                                        chatIntent.putExtra("visit_user_name",name);

                                        startActivity(chatIntent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,parent,false);
                        return new ChatViewHolder(view);
                    }
                };

        chatRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView nameText,messageText;
        CircleImageView imageView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.message_name_id);
            messageText = itemView.findViewById(R.id.message_sms_id);
            imageView = itemView.findViewById(R.id.message_profile_image);
        }
    }
}
