package com.example.evelyn;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evelyn.R;
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

public class MyChatFragment extends Fragment {

    private View chatsView;
    private RecyclerView chatRecycler;

    private FirebaseAuth mAuth;
    private DatabaseReference messageRef,userRef;
    private String currentUser;

    public MyChatFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        chatsView =  inflater.inflate(R.layout.fragment_my_chat,container,false);


        chatRecycler = (RecyclerView) chatsView.findViewById(R.id.my_chat_list);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecycler.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid().toString();
        messageRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUser);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        return chatsView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Upload> contactOption =
                new FirebaseRecyclerOptions.Builder<Upload>()
                        .setQuery(messageRef,Upload.class)
                        .build();

        FirebaseRecyclerAdapter<Upload,ContactView> contactAdapter =
                new FirebaseRecyclerAdapter<Upload, ContactView>(contactOption) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ContactView contactView, int i, @NonNull Upload upload) {
                        final String userId = getRef(i).getKey();
                        userRef.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String imageurl = dataSnapshot.child("imageUrl").getValue().toString();
                                final String name = dataSnapshot.child("name").getValue().toString();

                                contactView.nameText.setText(name);
                                contactView.messageText.setText("Last Seen :");
                                Picasso.get().load(imageurl).into(contactView.imageView);

                                contactView.itemView.setOnClickListener(new View.OnClickListener() {
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

                            };
                        });
                    }

                    @NonNull
                    @Override
                    public ContactView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,parent,false);
                        return new MyChatFragment.ContactView(view);
                    }
                };
        chatRecycler.setAdapter(contactAdapter);
        contactAdapter.startListening();
    }

    public static class ContactView extends RecyclerView.ViewHolder{
        TextView nameText,messageText;
        CircleImageView imageView;
        public ContactView(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.message_name_id);
            messageText = itemView.findViewById(R.id.message_sms_id);
            imageView = itemView.findViewById(R.id.message_profile_image);
        }
    }
}
