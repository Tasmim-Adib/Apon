package com.example.evelyn;

import android.app.DatePickerDialog;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.evelyn.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends Fragment {

    private TextView myName,mybloodGroup,myDistrict,myLastDonate,myNearestHospital,myContact,
    myBadhan,mySandhani,myRedCrescent,myMedicineClub;
    private EditText mycontactEditTExt,mythanaEditText;
    private ImageView myOrganizationImage;
    private FloatingActionButton myEdit;
    private Button myProfileDoneButton;
    private View myProfileView;
    private CircleImageView image;
    private CardView myCaredView;
    private AutoCompleteTextView mydistrictEditText,myHospitalEditText;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,organizationRef,disthospitalRef;
    private String currentUSer;
    DatePickerDialog.OnDateSetListener setListener;
    String userDistrict,userHospital,userContact,bg,userthana;
    private ArrayAdapter<String> disAdapter,hospitalAdapter;
    SettingsActivity settingsActivity = new SettingsActivity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myProfileView = inflater.inflate(R.layout.fragment_my_profile,container,false);
        init();
        retriveMyInfo();

        myEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCaredView.setVisibility(View.GONE);
                myEdit.setVisibility(View.GONE);
                myHospitalEditText.setVisibility(View.VISIBLE);
                mydistrictEditText.setVisibility(View.VISIBLE);
                mycontactEditTExt.setVisibility(View.VISIBLE);
                myProfileDoneButton.setVisibility(View.VISIBLE);
                mythanaEditText.setVisibility(View.VISIBLE);
            }
        });

        myProfileDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(getBloodGroup());
            }
        });

        disAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,settingsActivity.districts);
        mydistrictEditText.setThreshold(1);
        mydistrictEditText.setAdapter(disAdapter);

        hospitalAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,settingsActivity.hospitals);
        myHospitalEditText.setThreshold(1);
        myHospitalEditText.setAdapter(hospitalAdapter);


        return myProfileView;
    }
    private void retriveOrganizationInfo(String userbg) {
        organizationRef.child("Badhan").child(userbg).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUSer)){
                    myOrganizationImage.setVisibility(View.VISIBLE);
                    myBadhan.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        organizationRef.child("Sandhani").child(userbg).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUSer)){
                    myOrganizationImage.setVisibility(View.VISIBLE);
                    mySandhani.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        organizationRef.child("RedCrescent").child(userbg).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUSer)){
                    myOrganizationImage.setVisibility(View.VISIBLE);
                    myRedCrescent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        organizationRef.child("MedicineClub").child(userbg).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUSer)){
                    myOrganizationImage.setVisibility(View.VISIBLE);
                    myMedicineClub.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateProfile(String bg) {

        String updatedHospital = myHospitalEditText.getText().toString();
        String updatedDistrict = mydistrictEditText.getText().toString();
        String updatedContact = mycontactEditTExt.getText().toString();
        String updatedThana = mythanaEditText.getText().toString();

        if(!userHospital.equals(updatedHospital)){
            databaseReference.child(currentUSer).child("hospital").setValue(updatedHospital);
            disthospitalRef.child(bg).child(currentUSer).child("hospital").setValue(updatedHospital);

        }
        if(!userDistrict.equals(updatedDistrict)){
            databaseReference.child(currentUSer).child("district").setValue(updatedDistrict);
            disthospitalRef.child(bg).child(currentUSer).child("district").setValue(updatedDistrict);


        }
        if(!userContact.equals(updatedContact)){
            databaseReference.child(currentUSer).child("contact").setValue(updatedContact);

        }
        if(!userthana.equals(updatedThana)){
            databaseReference.child(currentUSer).child("thana").setValue(updatedThana);

        }
        myCaredView.setVisibility(View.VISIBLE);
        myEdit.setVisibility(View.VISIBLE);
        myHospitalEditText.setVisibility(View.GONE);
        mydistrictEditText.setVisibility(View.GONE);
        mycontactEditTExt.setVisibility(View.GONE);
        myProfileDoneButton.setVisibility(View.GONE);
        mythanaEditText.setVisibility(View.GONE);

    }

    private void init() {

        mAuth  = FirebaseAuth.getInstance();
        currentUSer = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        organizationRef = FirebaseDatabase.getInstance().getReference();
        disthospitalRef = FirebaseDatabase.getInstance().getReference();

        myName = (TextView)myProfileView.findViewById(R.id.my_account_name);
        mybloodGroup = (TextView)myProfileView.findViewById(R.id.my_account_bloodGroup);
        myDistrict = (TextView)myProfileView.findViewById(R.id.my_account_district);
        myLastDonate = (TextView)myProfileView.findViewById(R.id.my_account_lastDonate);
        myNearestHospital = (TextView)myProfileView.findViewById(R.id.my_account_hospital);
        image = (CircleImageView)myProfileView.findViewById(R.id.my_account_Image);
        myContact = (TextView)myProfileView.findViewById(R.id.my_account_contact);
        myOrganizationImage = (ImageView)myProfileView.findViewById(R.id.my_account_organization_image);
        myEdit = (FloatingActionButton)myProfileView.findViewById(R.id.my_account_editButton);
        myBadhan = (TextView)myProfileView.findViewById(R.id.my_account_badhan);
        mySandhani = (TextView)myProfileView.findViewById(R.id.my_account_sandhani);
        myRedCrescent = (TextView)myProfileView.findViewById(R.id.my_account_redCrescent);
        myMedicineClub = (TextView)myProfileView.findViewById(R.id.my_account_medicineClub);
        myCaredView = (CardView)myProfileView.findViewById(R.id.my_account_cardView);
        mydistrictEditText = (AutoCompleteTextView) myProfileView.findViewById(R.id.my_account_district_editText);
        myHospitalEditText = (AutoCompleteTextView) myProfileView.findViewById(R.id.my_account_hospital_editText);
        mycontactEditTExt = (EditText)myProfileView.findViewById(R.id.my_account_contact_editText);
        myProfileDoneButton = (Button)myProfileView.findViewById(R.id.my_account_done);
        mythanaEditText = (EditText)myProfileView.findViewById(R.id.my_account_thana_editText);

    }


    private void retriveMyInfo(){
        databaseReference.child(currentUSer).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String userImage = dataSnapshot.child("imageUrl").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    userDistrict = dataSnapshot.child("district").getValue().toString();
                    userContact = dataSnapshot.child("contact").getValue().toString();
                    String userBloodGroup = dataSnapshot.child("bloodGroup").getValue().toString();
                    userHospital = dataSnapshot.child("hospital").getValue().toString();
                    String lastDonation = dataSnapshot.child("lastDate").getValue().toString();
                    userthana = dataSnapshot.child("thana").getValue().toString();


                    Picasso.get().load(userImage).into(image);
                    myName.setText(userName);
                    myDistrict.setText(userDistrict);
                    mybloodGroup.setText(userBloodGroup);
                    myNearestHospital.setText(userHospital);
                    myLastDonate.setText(lastDonation);
                    myContact.setText(userContact);
                    myHospitalEditText.setText(userHospital);
                    mycontactEditTExt.setText(userContact);
                    mydistrictEditText.setText(userDistrict);
                    mythanaEditText.setText(userthana);
                    retriveOrganizationInfo(userBloodGroup);
                    setBloodGroup(userBloodGroup);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setBloodGroup(String userBloodGroup) {
        bg = userBloodGroup;
    }
    private String getBloodGroup(){
        return bg;
    }
}
