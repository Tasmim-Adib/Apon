package com.example.evelyn;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
public class NeedBloodFragment extends Fragment {

    private View requestView;
    private RecyclerView requestRecycler;
    private FirebaseAuth mAuth;
    private DatabaseReference chatRequestRef,userRef,chatRef;
    private String currentUserId;
    String Name;



    public NeedBloodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestView =  inflater.inflate(R.layout.fragment_need_blood, container, false);
        mAuth = FirebaseAuth.getInstance();
        chatRequestRef = FirebaseDatabase.getInstance().getReference().child("chat_request");

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserId = mAuth.getCurrentUser().getUid().toString();
        chatRef = FirebaseDatabase.getInstance().getReference().child("Chats");

        requestRecycler = (RecyclerView) requestView.findViewById(R.id.request_recyler_id);
        requestRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        return requestView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Upload> option =
                new FirebaseRecyclerOptions.Builder<Upload>()
                .setQuery(chatRequestRef.child(currentUserId),Upload.class)
                .build();

        FirebaseRecyclerAdapter<Upload,RequestView> adapter =
                new FirebaseRecyclerAdapter<Upload, RequestView>(option) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestView requestView, final int i, @NonNull Upload upload) {
                        final String list_user_id = getRef(i).getKey();
                        DatabaseReference getType = getRef(i).child("request_type").getRef();

                        getType.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String type = dataSnapshot.getValue().toString();
                                    if(type.equals("received")){

                                        userRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String userImage = dataSnapshot.child("imageUrl").getValue().toString();
                                                Name = dataSnapshot.child("name").getValue().toString();

                                                String Thana = dataSnapshot.child("thana").getValue().toString();
                                                String District = dataSnapshot.child("district").getValue().toString();


                                                requestView.userName.setText(Name);
                                                requestView.userDistrict.setText(District);
                                                requestView.userThana.setText(Thana);
                                                Picasso.get().load(userImage).into(requestView.image);

                                                requestView.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        CharSequence options[] = new CharSequence[]{
                                                                "View",
                                                                "Accept",
                                                                "Decline"
                                                        };

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle(Name + " Chat Request");
                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                if(which == 1){

                                                                    chatRef.child(currentUserId).child(list_user_id)
                                                                            .child("Chats").setValue("saved")
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                    if(task.isSuccessful()){
                                                                                        chatRef.child(list_user_id).child(currentUserId)
                                                                                                .child("Chats").setValue("saved")
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                                        if(task.isSuccessful()){

                                                                                                            chatRequestRef.child(currentUserId).child(list_user_id)
                                                                                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                    if(task.isSuccessful()){
                                                                                                                        chatRequestRef.child(list_user_id).child(currentUserId)
                                                                                                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                if(task.isSuccessful()){
                                                                                                                                    Toast.makeText(getContext(), "Contacts Saved", Toast.LENGTH_SHORT).show();

                                                                                                                                }

                                                                                                                            }
                                                                                                                        });
                                                                                                                    }

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                                if(which == 0){

                                                                    String visit_user_Id = getRef(i).getKey();
                                                                    Intent AccountIntent = new Intent(getActivity(),AccountActivity.class);
                                                                    AccountIntent.putExtra("visit_user_Id",visit_user_Id);
                                                                    startActivity(AccountIntent);

                                                                }
                                                                if(which == 2){

                                                                    chatRequestRef.child(currentUserId).child(list_user_id)
                                                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                chatRequestRef.child(list_user_id).child(currentUserId)
                                                                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            Toast.makeText(getContext(), "Contacts Deleted", Toast.LENGTH_SHORT).show();

                                                                                        }

                                                                                    }
                                                                                });
                                                                            }

                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        });
                                                        builder.show();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public RequestView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_sample,parent,false);
                        RequestView holder = new RequestView(view);
                        return holder;
                    }
                };
        requestRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestView extends RecyclerView.ViewHolder{

        TextView userName,userThana,userDistrict,viewProfile,accept,decline;
        CircleImageView image;
        public RequestView(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.request_name_id);
            userThana = (TextView) itemView.findViewById(R.id.request_thana_id);
            userDistrict = (TextView) itemView.findViewById(R.id.request_district_id);
            viewProfile = (TextView) itemView.findViewById(R.id.request_view_profile_id);
            accept = (TextView) itemView.findViewById(R.id.request_accept_id);
            decline = (TextView) itemView.findViewById(R.id.request_Decline_id);
            image = (CircleImageView) itemView.findViewById(R.id.request_profile_image);
        }
    }
}
