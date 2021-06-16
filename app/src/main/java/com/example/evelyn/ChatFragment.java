package com.example.evelyn;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class ChatFragment extends Fragment {

    private View friendListView;
    private RecyclerView friendlistItem;
    private DatabaseReference friendRef,UserRef;
    private FirebaseAuth mAuth;
    private String currentUserId;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        friendListView =  inflater.inflate(R.layout.fragment_chat, container, false);

        friendlistItem = (RecyclerView)friendListView.findViewById(R.id.contact_recycler_view_id);
        friendlistItem.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid().toString();
        friendRef = FirebaseDatabase.getInstance().getReference().child("Chats").child(currentUserId);
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        return friendListView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Upload>()
                .setQuery(friendRef,Upload.class)
                .build();

        FirebaseRecyclerAdapter<Upload,SearchFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Upload, SearchFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SearchFriendViewHolder searchFriendViewHolder, int i, @NonNull Upload upload) {

                        final String userId = getRef(i).getKey();
                        UserRef.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String userImage = dataSnapshot.child("imageUrl").getValue().toString();
                                String userName = dataSnapshot.child("name").getValue().toString();

                                String userThana = dataSnapshot.child("thana").getValue().toString();
                                String userDistrict = dataSnapshot.child("district").getValue().toString();

                                String userBloodGroup = dataSnapshot.child("bloodGroup").getValue().toString();
                                String userHospital = dataSnapshot.child("hospital").getValue().toString();


                                searchFriendViewHolder.bloodGroupText.setText(userBloodGroup);
                                searchFriendViewHolder.districtText.setText(userDistrict);
                                searchFriendViewHolder.hospitalText.setText(userHospital);
                                searchFriendViewHolder.nameText.setText(userName);
                                searchFriendViewHolder.thanaText.setText(userThana);
                                Picasso.get().load(userImage).into(searchFriendViewHolder.image);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SearchFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
                        SearchFriendViewHolder viewHolder = new SearchFriendViewHolder(view);
                        return viewHolder;
                    }
                };

        friendlistItem.setAdapter(adapter);
        adapter.startListening();
    }

    public static class SearchFriendViewHolder extends RecyclerView.ViewHolder{
        TextView nameText,bloodGroupText,thanaText,districtText,hospitalText;
        CircleImageView image;

        public SearchFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.view_name_id);
            bloodGroupText = itemView.findViewById(R.id.view_blood_Group_id);

            image = itemView.findViewById(R.id.View_profile_image);
        }
    }
}
