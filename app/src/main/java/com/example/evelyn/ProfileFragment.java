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
import android.widget.Button;
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
public class ProfileFragment extends Fragment {

private TextView myName,myAge,myBloodGroup,myThana,myDistrict,myHospital,myContact,myDonation;
private Button updateButton;
private CircleImageView myImage;
private View myProfileView;

private FirebaseAuth mAuth;
private DatabaseReference CurrentUser;
private String currentUserId;
public String userBloodGroup;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myProfileView =  inflater.inflate(R.layout.fragment_profile, container, false);
        myName = (TextView) myProfileView.findViewById(R.id.myAccount_username);
        myAge = (TextView) myProfileView.findViewById(R.id.myAccount_Age);
        myBloodGroup = (TextView) myProfileView.findViewById(R.id.myAccount_bloodGroup);
        myThana = (TextView) myProfileView.findViewById(R.id.myAccount_thana);
        myDonation = (TextView)myProfileView.findViewById(R.id.myAccount_donation);
        myDistrict = (TextView) myProfileView.findViewById(R.id.myAccount_district);
        myContact = (TextView) myProfileView.findViewById(R.id.myAccount_Contact);
        myHospital = (TextView) myProfileView.findViewById(R.id.myAccount_hospital);
        myImage = (CircleImageView)myProfileView.findViewById(R.id.My_Account_Image);

     //   updateButton = (Button) myProfileView.findViewById(R.id.myAccount_Button);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        CurrentUser = FirebaseDatabase.getInstance().getReference().child("Users");



        retrieveMyInformation();

      /*  updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent updateIntent = new Intent(getActivity(),UpdateActivity.class);
                startActivity(updateIntent);


            }
        });*/



        return myProfileView;

    }

    private void retrieveMyInformation() {



        CurrentUser.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userImage = dataSnapshot.child("imageUrl").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userAge = dataSnapshot.child("age").getValue().toString();
                    String userThana = dataSnapshot.child("thana").getValue().toString();
                    String userDistrict = dataSnapshot.child("district").getValue().toString();
                    String userContact = dataSnapshot.child("contact").getValue().toString();
                    userBloodGroup = dataSnapshot.child("bloodGroup").getValue().toString();
                    String userHospital = dataSnapshot.child("hospital").getValue().toString();
                    String lastDonation = dataSnapshot.child("lastDate").getValue().toString();

                    Picasso.get().load(userImage).into(myImage);
                    myName.setText(userName);
                    myAge.setText(userAge);
                    myThana.setText(userThana);
                    myDistrict.setText(userDistrict);
                    myContact.setText(userContact);
                    myBloodGroup.setText(userBloodGroup);
                    myHospital.setText(userHospital);
                    myDonation.setText(lastDonation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
