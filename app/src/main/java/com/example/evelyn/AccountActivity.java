package com.example.evelyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private String receiverUserId,current_state,sendertUserId,userName;
    private TextView recieverlastDonation,receiverName,receiverAge,receiverGroup,receiverthana,receiverDistrict,
            receiverContact,receiverHospital,otherBadhan,otherSandhani,otherMedicineClub,otherRedCrescent;
    private CircleImageView receiverImage;
    private ImageView organization;
    private DatabaseReference userRef,chatRequestRef,chatRef,organRef;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    FloatingActionButton sendMessageBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        sendertUserId = mAuth.getCurrentUser().getUid().toString();

        toolbar = (Toolbar)findViewById(R.id.account_name_Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        receiverImage = (CircleImageView)findViewById(R.id.other_Image);
        receiverName = (TextView)findViewById(R.id.other_name);
        receiverAge = (TextView)findViewById(R.id.other_age);
        recieverlastDonation = (TextView)findViewById(R.id.other_lastDonate);
        receiverthana = (TextView)findViewById(R.id.other_thana);
        receiverDistrict = (TextView)findViewById(R.id.other_district);
        receiverContact = (TextView)findViewById(R.id.other_contact);
        receiverHospital = (TextView)findViewById(R.id.other_hospital);
        receiverGroup = (TextView)findViewById(R.id.other_bloodGroup);
        sendMessageBtn = (FloatingActionButton)findViewById(R.id.other_editButton);
        otherBadhan = (TextView)findViewById(R.id.other_badhan);
        otherSandhani = (TextView)findViewById(R.id.other_sandhani);
        otherMedicineClub = (TextView)findViewById(R.id.other_medicineClub);
        otherRedCrescent = (TextView)findViewById(R.id.other_redCrescent);
        organization = (ImageView)findViewById(R.id.other_organization_image);

        current_state = "new";

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        chatRequestRef = FirebaseDatabase.getInstance().getReference().child("chat_request");
        chatRef = FirebaseDatabase.getInstance().getReference();
        organRef = FirebaseDatabase.getInstance().getReference();



        receiverUserId = getIntent().getExtras().get("visit_user_Id").toString();



        RetriveUserInfo();


        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatRef.child("Contacts").child(sendertUserId).child(receiverUserId).child("state").setValue("saved");
                chatRef.child("Contacts").child(receiverUserId).child(sendertUserId).child("state").setValue("saved");
                Intent chatIntent = new Intent(getApplicationContext(),ChatActivity.class);
                chatIntent.putExtra("visit_user_id",receiverUserId);
                chatIntent.putExtra("visit_user_name",userName);

                startActivity(chatIntent);
            }
        });

    }

    private void RetriveUserInfo() {
        userRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userImage = dataSnapshot.child("imageUrl").getValue().toString();
                    userName = dataSnapshot.child("name").getValue().toString();
                    String userAge = dataSnapshot.child("age").getValue().toString();
                    String userThana = dataSnapshot.child("thana").getValue().toString();
                    String userDistrict = dataSnapshot.child("district").getValue().toString();
                    String userContact = dataSnapshot.child("contact").getValue().toString();
                    String userBloodGroup = dataSnapshot.child("bloodGroup").getValue().toString();
                    String userHospital = dataSnapshot.child("hospital").getValue().toString();
                    String lastDonate = dataSnapshot.child("lastDate").getValue().toString();

                    Picasso.get().load(userImage).into(receiverImage);
                    receiverName.setText(userName);
                    receiverAge.setText("Age : "+userAge);
                    receiverthana.setText(userThana +", ");
                    receiverDistrict.setText(userDistrict);
                    receiverContact.setText(userContact);
                    receiverGroup.setText(userBloodGroup);
                    receiverHospital.setText(userHospital);
                    recieverlastDonation.setText(lastDonate);
                    getSupportActionBar().setTitle(userName);

                   // manageChatRequest();
                    visibleOrganization(userBloodGroup);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void visibleOrganization(String userBloodGroup) {
        organRef.child("Badhan").child(userBloodGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiverUserId)){
                    organization.setVisibility(View.VISIBLE);
                    otherBadhan.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        organRef.child("MedicineClub").child(userBloodGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiverUserId)){
                    organization.setVisibility(View.VISIBLE);
                    otherMedicineClub.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        organRef.child("Sandhani").child(userBloodGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiverUserId)){
                    organization.setVisibility(View.VISIBLE);
                    otherSandhani.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        organRef.child("RedCrescent").child(userBloodGroup).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiverUserId)){
                    organization.setVisibility(View.VISIBLE);
                    otherRedCrescent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
