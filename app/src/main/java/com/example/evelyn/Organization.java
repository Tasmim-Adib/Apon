package com.example.evelyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Organization extends AppCompatActivity implements View.OnClickListener{
    public static int badhanRegistered = 0;
    public static int sandhaniRegistered = 0;
    public static int redCrescentRegistered = 0;
    public static int medicineClubRegistered = 0;

    DatabaseReference databaseReference,saveRef;
    private Toolbar mtoolbar;
    private RecyclerView recyclerView;
    private Query redCrescentQuery,badhanQuery,sandhaniQuery,medicineClubQuery;
    FirebaseAuth mAuth;
    private CardView badhanCard,sandhaniCard,redcrescentCard,medicineCard;
    private String currentUserId;
    Button register_btn,find_donor_btn,
            next1_btn,next2_btn,badhanDone,sandhaniDone,redCrescentDone,medicineClubDone,redCrescentFindDone;

    HashMap sandhaniHash,badhanHash;

    int choice;
    AutoCompleteTextView institutionText,deptText,hallNameText,areaText,badhanInstitutionText,sandhaniInstitutionText,bloodGroupText;
    TextView completeRegistration,organizationName,organizationTextView;

    String badhanInstitution,sandhaniInstitution,medicineInstituion,department,hall,area,bloodGroup,name,lastDonate,image;
    SettingsActivity settingsActivity = new SettingsActivity();
    String searchArea,searchGroup;



    String[] University = {"University of Dhaka","Bangaldesh University for Engineering and Technology (BUET)","Khulna University for Engineering and Technology (KUET)","Rajshahi University for Engineering and Technology (RUET)"
    ,"Chittagong University for Engineering and Technology (CUET)","University of Rajshahi","University of Chittagong","Bangladesh Agriculture University","Jahangirnagar University","Islamic University","Shahjalal University of Science & Technology(SUST)","Khulna University"
    ,"Bangabandhu Sheikh Mujibur Rahman Agricultural University","Hajee Mohammad Danesh Science & Technology University","Mawlana Bhashani Science & Technology University","Patuakhali Science And Technology University","Sher-e-Bangla Agricultural University"
    ,"Dhaka University of Engineering & Technology","Noakhali Science & Technology University","Jagannath University","Comilla University","Jatiya Kabi Kazi Nazrul Islam University","Chittagong Veterinary and Animal Sciences University","Sylhet Agricultural University",
    "Jessore University of Science & Technology","Pabna University of Science and Technology","Begum Rokeya University, Rangpur","Bangladesh University of Professionals","Bangabandhu Sheikh Mujibur Rahman Science & Technology University","Bangladesh University of Textiles (BUTEX)"
    ,"University of Barisal","Rangamati Science and Technology University","Bangabandhu Sheikh Mujibur Rahman Maritime University, Bangladesh","Rabindra University, Bangladesh","Bangabandhu Sheikh Mujibur Rahman Digital University, Bangladesh","Sheikh Hasina University","Khulna Agricultural University",
    "Bangamata Sheikh Fojilatunnesa Mujib Science and Technology University","Chandpur Science and Technology University","Bangabandhu Sheikh Mujibur Rahman Univerisity, Kishoreganj","Hobiganj Agricultural University","Dhaka College,Dhaka","Govt. Titumir College, Dhaka","Eden Mohila College, Dhaka",
    "Home Economics College, Dhaka","Tejgoan College, Dhaka","Mohammadpur Central University College","Carmichael College, Rangpur","Rangpur Govt. College, Rangpur","Dinajpur Govt. College, Dinajpur","Kurigram Govt. College, Kurigram","Thakurgaon Govt. College, Thakurgaon","Moqbular Rahman Govt. College",
            "Lalmonirhat Government College, Lalmonirhat","Nilphamari Government College, Nilphamari","Govt. Begum Rokeya College","Govt. Edward College","Rajshahi College","Nawabganj Govt. College","Nawab Siraj-Ud-Daulah Govt. College","Naogaon Government College, Naogaon"
    ,"Govt. Saadat College","Govt. Azizul Huque College","Govt. M M College","Comilla Victoria College","Ananda Mohan College","Rajbari Govt. College","Govt. Rajendra College","Govt. BM College","Joypurhat Govt. College",
            "Kustia Govt. College","Govt. Keshob Chandra College","Gurudayal Govt. College","Sirajganj Government College","Noakhali Government College","Chuadanga Govt. College","Brahmanbaria Govt. College","Narail Govt. Victoria College Unit","Narsingdi Govt. College","Gaibandha Govt. College","Sunamganj Govt. College","Government Huseyn Shaheed Suhrawardy College",
            "Brindaban Government College","Satkhira Government College","Govt P. C. College","Bhawal Badre Alam Govt. College","Netrokona Govt. College","Bhola Govt College","Shariatpur Government College","Feni Government College","Govt Tolaram College","Govt. Nazimudin College"};



    String[] halls ={"Nabab Foyzunnessa Chowdhurani Hostel,DU","Sir Salimullah Muslim Hall,DU","Dr. Muhammad Shahidullah Hall,DU","Jagannath Hall,DU","Fazlul Huq Muslim Hall,DU","Zahurul Haq Hall,DU","Ruqayyah Hall,DU","Surja Sen Hall,DU","Haji Muhammad Mohsin Hall,DU","Shamsunnahar Hall,DU","Kabi Jasimuddin Hall,DU","A.F. Rahman Hall,DU",
            "Muktijoddha Ziaur Rahman Hall,DU","Bangabandhu Sheikh Mujibur Rahman Hall,DU","Bangladesh-Kuwait Maitree Hall,DU","Sir P.J. Hartog International Hall,DU","Bangamata Sheikh Fazilatunnesa Mujib Hall,DU","Amar Ekushey Hall,DU","Bijoy Ekattar Hall,DU","Kabi Sufia Kamal Hall,DU","IBA Hostel,DU","Dr. Qudrat_E-Khuda Hostel,DU",
            "Nawab Faizunnessa Chowdhurani Chhatrinibash,DU","Shahid Athlet Sultana Kamal Hostel,DU","Sher-e Bangla Fazlul Haque Hall,RU","Shah Mukhdum Hall,RU","Nawab Abdul Latif Hall,RU","Syed Amer Ali Hall,RU", "Shaheed Shamsuzzoha Hall,RU","Shaheed Habibur Rahman Hall,RU","Matihar Hall,RU","Madar Bux Hall,RU","Shaheed Suhrawardy Hall,RU","Shaheed Ziaur Rahman Hall,RU",
            "Bangabandhu Sheikh Mujibur Rahman Hall,RU","Mannujan Hall,RU","Rokeya Hall,RU","Tapashi Rabeya Hall,RU","Begum Khaleda Zia Hall,RU","Rahamatunnesa Hall,RU","Bangamata  Fazilatunnesa  Hall,RU","Ahsanullah Hall,BUET","Titumir Hall,BUET","Chatri Hall,BUET","Dr. M. A. Rashid Hall,BUET","Kazi Nazrul Islam Hall,BUET","Sher-e-Bangla Hall,BUET","Suhrawardy Hall,BUET","Shahid Smriti Hall,BUET"
    ,"Isha Khan Hall,BAU","Shahjalal Hall,BAU","Shaheed Shamsul Haque Hall,BAU","Shaheed Nazmul Ahsan Hall,BAU","Ashraful Haque Hall,BAU","Shahid Jamal Hossain Hall,BAU","Hossain Shaheed Suhrawardy Hall,BAU","Fazlul Haque Hall,BAU","Bangabandhu Sheikh Mujib Hall,BAU","Sultana Razia Hall,BAU","Taposi Rabia Hall,BAU","Sheikh Fazilatunnesa Mujib Hall,BAU","Begum Rokeya Hall,BAU",
    "Al Beruni Hall,JU","Meer Mosharraf Hossain Hall,JU","Shaheed Salam-Barkat Hall,JU","A.F.M. Kamaluddin Hall,JU","Moulana Bhasani Hall,JU","Bangabondhu Sheikh Majibur Rahman Hall,JU","Shaheed Rafiq-Jabbar Hall,JU","Nawab Faizunnesa Hall,JU","Fazilatunnesa Hall,JU","Jahanara Imam Hall,JU", "Preetilata Hall,JU","Begum Khaleda Zia Hall,JU","Sheikh Hasina Hall,JU"};


    String[] sandhaniUnit = {"Abdul Malek Ukil Medical College","Bangladesh Medical College","Chattogram Medical College","Cumilla Medical College","Cox’s bazar Medical College","Dhaka Medical College","Dhaka Dental College","M.Abdur Rahim Medical College","East West Medical & Update Dental College","Bangabandhu Sheikh Mujib Medical College, Faridpur","Gonoshasthaya Samaj Vittik Medical College","Jalalabad Ragib-Rabeya Medical College","Jashore  Medical College","Kusthia Medical College","Mymensingh Medical College","Mugda Medical College","Patuakhali Medical College","Rajshahi Medical College",
            "Rangpur Medical College","Sher E Bangla Medical College","Sylhet M.A.G. Osmani Medical College","Sir Salimullah Medical College","Shaheed M. Monsur Ali Medical College","Shaahed Suhrawardi Medical College","Syed Nazrul Islam Medical College","Shahid Ziaur Rahman Medical College"};

    String[] medicineClub = {"Abdul Malek Ukil Medical College","Brahminbaria Medical College","Central Medical College","Cox's Bazar Medical College","Cumilla Medical College","Dhaka Medical College","Delta Medical College","Bangabandhu Sheikh Mujib Medical College, Faridpur","Gonoshasthaya Samaj Vittik Medical College","Mymensingh Medical College",
            "M Abdur Rahim Medical College","Mainamoti Medical College","Patuakhali Medical College","Pabna Medical College","Rajshahi Medical College","Rangpur Medical College","Sher-E-Bangla Medical College","Sheikh Hasina Medical College","Shaheed M Mansur Ali Medical College","Sylhet Osmani Medical College",
            "Shaheed Syed Nazrul Islam Medical College ","Shaheed Tajuddin Ahmad Medical College","US-Bangla Medical College ","Parkview Medical College","Rangamati Medical College","Satkhira Medical College","Universal Medical College"};

    ArrayAdapter<String> areaAdapter,institutionAdapter,hallAdapter,sandhaniAdapter,medicineClubAdapter,bloodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);




        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        saveRef = FirebaseDatabase.getInstance().getReference();

        mtoolbar = (Toolbar)findViewById(R.id.organization_toolbar_id);



        organizationTextView = (TextView)findViewById(R.id.organization_textView);
        badhanCard = (CardView)findViewById(R.id.badhanCardview);
        sandhaniCard = (CardView)findViewById(R.id.sandhaniCardview);
        redcrescentCard = (CardView)findViewById(R.id.redcrescnetCardview);
        medicineCard =(CardView)findViewById(R.id.medicineClubCardview);

        recyclerView = (RecyclerView)findViewById(R.id.organization_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);




        register_btn = (Button)findViewById(R.id.register_button);
        find_donor_btn = (Button)findViewById(R.id.Find_donor_organization_button);
        next1_btn = (Button)findViewById(R.id.next1_button);
        next2_btn = (Button)findViewById(R.id.next2_button);

        institutionText = (AutoCompleteTextView)findViewById(R.id.institution_edittext);
        areaText = (AutoCompleteTextView)findViewById(R.id.area_edittext);
        deptText = (AutoCompleteTextView)findViewById(R.id.department_edittext);
        hallNameText = (AutoCompleteTextView)findViewById(R.id.hall_edittext);
        completeRegistration = (TextView)findViewById(R.id.completeRegistrationTextview);
        organizationName = (TextView)findViewById(R.id.organization_name_Textview);
        badhanInstitutionText = (AutoCompleteTextView)findViewById(R.id.badhan_institution_edittext);
        sandhaniInstitutionText = (AutoCompleteTextView)findViewById(R.id.sandhani_institution_edittext);
        bloodGroupText = (AutoCompleteTextView)findViewById(R.id.organization_blood_group_edittext);


        badhanDone = (Button)findViewById(R.id.badhan_done_button);
        sandhaniDone = (Button)findViewById(R.id.sandhani_done_button);
        redCrescentDone = (Button)findViewById(R.id.redCrescent_done_button);
        medicineClubDone = (Button)findViewById(R.id.medicine_club_done_button);
        redCrescentFindDone = (Button)findViewById(R.id.redCrescent_find_done_button);

        badhanCard.setOnClickListener(this);
        sandhaniCard.setOnClickListener(this);
        redcrescentCard.setOnClickListener(this);
        medicineCard.setOnClickListener(this);


        register_btn.setOnClickListener(this);
        find_donor_btn.setOnClickListener(this);
        next2_btn.setOnClickListener(this);
        next1_btn.setOnClickListener(this);

        areaAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,settingsActivity.districts);
        areaText.setThreshold(1);
        areaText.setAdapter(areaAdapter);

        institutionAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,University);
        badhanInstitutionText.setThreshold(1);
        badhanInstitutionText.setAdapter(institutionAdapter);


        hallAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,halls);
        hallNameText.setThreshold(1);
        hallNameText.setAdapter(hallAdapter);

        sandhaniAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,sandhaniUnit);
        sandhaniInstitutionText.setThreshold(1);
        sandhaniInstitutionText.setAdapter(sandhaniAdapter);


        medicineClubAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,medicineClub);
        institutionText.setThreshold(1);
        institutionText.setAdapter(medicineClubAdapter);

        bloodAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,settingsActivity.groups);
        bloodGroupText.setThreshold(1);
        bloodGroupText.setAdapter(bloodAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.badhanCardview){

            goneButton();
            organizationName.setText("BADHAN");
            if(badhanRegistered == 0){
                register_btn.setVisibility(View.VISIBLE);
            }
            find_donor_btn.setVisibility(View.VISIBLE);
            choice = 3;

        }

        if(v.getId() == R.id.redcrescnetCardview){
            goneButton();
            organizationName.setText("Red Crescent Society");

            if(redCrescentRegistered == 0){
                register_btn.setVisibility(View.VISIBLE);
            }

            find_donor_btn.setVisibility(View.VISIBLE);
            choice = 1;
        }

        if(v.getId() == R.id.sandhaniCardview){
            goneButton();
            organizationName.setText("SANDHANI");
            if(sandhaniRegistered == 0){
                register_btn.setVisibility(View.VISIBLE);
            }

            find_donor_btn.setVisibility(View.VISIBLE);
            choice = 2;
        }
        if(v.getId() == R.id.medicineClubCardview){
            goneButton();
            organizationName.setText("Medicine Club");
            if(medicineClubRegistered == 0){
                register_btn.setVisibility(View.VISIBLE);
            }
            find_donor_btn.setVisibility(View.VISIBLE);
            choice = 4;
        }

        if(v.getId() == R.id.register_button){
            register_btn.setVisibility(View.GONE);
            completeRegistration.setVisibility(View.VISIBLE);
            find_donor_btn.setVisibility(View.GONE);
            databaseReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    bloodGroup = dataSnapshot.child("bloodGroup").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    lastDonate = dataSnapshot.child("lastDate").getValue().toString();
                    image = dataSnapshot.child("imageUrl").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if(choice == 3){
               badhanInstitutionText.setVisibility(View.VISIBLE);
               next1_btn.setVisibility(View.VISIBLE);
               next1_btn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        badhanInstitution = badhanInstitutionText.getText().toString();
                        if(TextUtils.isEmpty(badhanInstitution)){
                            badhanInstitutionText.requestFocus();
                            badhanInstitutionText.setError("please enter your institution name");
                            return;
                        }

                        else{
                            next1_btn.setVisibility(View.GONE);
                            hallNameText.setVisibility(View.VISIBLE);
                            badhanDone.setVisibility(View.VISIBLE);
                            badhanDone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hall = hallNameText.getText().toString();
                                    if(TextUtils.isEmpty(hall)){
                                        sandhaniHash = new HashMap();
                                        sandhaniHash.put("institution",badhanInstitution);
                                        saveRef.child("Badhan").child(bloodGroup).child(currentUserId).setValue(sandhaniHash);
                                    }

                                    else{
                                        badhanHash = new HashMap();
                                        badhanHash.put("institution",badhanInstitution);
                                        badhanHash.put("hallName",hall);
                                        saveRef.child("Badhan").child(bloodGroup).child(currentUserId).setValue(badhanHash);
                                    }
                                    Toast.makeText(getApplicationContext(),"Welcome to BADHAN Family",Toast.LENGTH_SHORT).show();
                                    badhanRegistered = 1;
                                    visibleButtons();
                                    badhanDone.setVisibility(View.GONE);
                                }
                            });

                        }

                   }
               });
            }

            if(choice == 1){

                areaText.setVisibility(View.VISIBLE);
                redCrescentDone.setVisibility(View.VISIBLE);
                redCrescentDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        area = areaText.getText().toString();
                        if(TextUtils.isEmpty(area)){
                            areaText.requestFocus();
                            areaText.setError("please enter your Area Name");
                            return;
                        }
                        else{

                            sandhaniHash = new HashMap();
                            sandhaniHash.put("institution",area);
                            redCrescentRegistered = 1;
                            visibleButtons();
                            saveRef.child("RedCrescent").child(bloodGroup).child(currentUserId).setValue(sandhaniHash);
                            redCrescentDone.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Welcome to Red Crescent Family",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }

            if(choice == 2){
                sandhaniInstitutionText.setVisibility(View.VISIBLE);
                sandhaniDone.setVisibility(View.VISIBLE);
                sandhaniDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sandhaniInstitution = sandhaniInstitutionText.getText().toString();
                        if(TextUtils.isEmpty(sandhaniInstitution)){
                            sandhaniInstitutionText.requestFocus();
                            sandhaniInstitutionText.setError("please enter your institution name");
                            return;
                        }
                        else{
                            sandhaniHash = new HashMap();
                            sandhaniHash.put("institution",sandhaniInstitution);

                            visibleButtons();
                            saveRef.child("Sandhani").child(bloodGroup).child(currentUserId).setValue(sandhaniHash);
                            sandhaniRegistered = 1;
                            visibleButtons();
                            sandhaniDone.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Welcome to SANDHANI Family",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }

            if(choice == 4){
                institutionText.setVisibility(View.VISIBLE);
                medicineClubDone.setVisibility(View.VISIBLE);
                medicineClubDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medicineInstituion = institutionText.getText().toString();
                        if(TextUtils.isEmpty(medicineInstituion)){
                            institutionText.requestFocus();
                            institutionText.setError("please enter your institution name");
                            return;
                        }
                        else{
                            sandhaniHash = new HashMap();
                            sandhaniHash.put("institution",medicineInstituion);
                            saveRef.child("MedicineClub").child(bloodGroup).child(currentUserId).setValue(sandhaniHash);
                            medicineClubRegistered = 1;
                            visibleButtons();
                            medicineClubDone.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Welcome to MEDICINE CLUB Family",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }

        }


        if(v.getId() == R.id.Find_donor_organization_button){
            register_btn.setVisibility(View.GONE);
            find_donor_btn.setVisibility(View.GONE);
            completeRegistration.setVisibility(View.VISIBLE);
            completeRegistration.setText("Find Donor in this organization...");


            if(choice == 1){
               bloodGroupText.setVisibility(View.VISIBLE);
               areaText.setVisibility(View.VISIBLE);
               redCrescentFindDone.setVisibility(View.VISIBLE);
               redCrescentFindDone.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       showItem();
                   }
               });

            }

             if(choice == 2){
                 bloodGroupText.setVisibility(View.VISIBLE);
                  sandhaniInstitutionText.setVisibility(View.VISIBLE);
                  sandhaniDone.setVisibility(View.VISIBLE);
                  sandhaniDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showItem();
                        }
                    });
                }
                if(choice == 3){
                    bloodGroupText.setVisibility(View.VISIBLE);
                    badhanInstitutionText.setVisibility(View.VISIBLE);
                    badhanDone.setVisibility(View.VISIBLE);
                    badhanDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showItem();
                        }
                    });
                }
                if(choice == 4){
                    bloodGroupText.setVisibility(View.VISIBLE);
                    institutionText.setVisibility(View.VISIBLE);
                    medicineClubDone.setVisibility(View.VISIBLE);
                    medicineClubDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showItem();
                        }
                    });
                }



        }
    }

    private void showItem() {
        completeRegistration.setVisibility(View.GONE);
        organizationName.setVisibility(View.VISIBLE);
        areaText.setVisibility(View.GONE);
        institutionText.setVisibility(View.GONE);
        badhanInstitutionText.setVisibility(View.GONE);
        sandhaniInstitutionText.setVisibility(View.GONE);
        sandhaniDone.setVisibility(View.GONE);
        badhanDone.setVisibility(View.GONE);
        medicineClubDone.setVisibility(View.GONE);
        bloodGroupText.setVisibility(View.GONE);
        redCrescentFindDone.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);


        if(choice == 1){
            searchArea = areaText.getText().toString();
            searchGroup = bloodGroupText.getText().toString();
            redCrescentQuery = FirebaseDatabase.getInstance().getReference("RedCrescent").child(searchGroup).orderByChild("institution").equalTo(searchArea);

        }
        if(choice == 2){
            searchArea = sandhaniInstitutionText.getText().toString();
            searchGroup = bloodGroupText.getText().toString();
            redCrescentQuery = FirebaseDatabase.getInstance().getReference("Sandhani").child(searchGroup).orderByChild("institution").equalTo(searchArea);

        }
        if(choice == 3){
            searchArea = badhanInstitutionText.getText().toString();
            searchGroup = bloodGroupText.getText().toString();
            redCrescentQuery = FirebaseDatabase.getInstance().getReference("Badhan").child(searchGroup).orderByChild("institution").equalTo(searchArea);

        }
        if(choice == 4){
            searchArea = institutionText.getText().toString();
            searchGroup = bloodGroupText.getText().toString();
            redCrescentQuery = FirebaseDatabase.getInstance().getReference("MedicineClub").child(searchGroup).orderByChild("institution").equalTo(searchArea);

        }

        FirebaseRecyclerOptions<Upload> redCrescentOptions =
                new FirebaseRecyclerOptions.Builder<Upload>()
                .setQuery(redCrescentQuery,Upload.class)
                .build();

       FirebaseRecyclerAdapter<Upload, Organization.organizationResultViewHolder> redCrescentAdapter
               = new FirebaseRecyclerAdapter<Upload, organizationResultViewHolder>(redCrescentOptions) {
           @Override
           protected void onBindViewHolder(@NonNull final organizationResultViewHolder organizationResultViewHolder, final int it, @NonNull Upload upload) {

               final String user = getRef(it).getKey();
               databaseReference.child("Users").child(user).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       final String organ_name = dataSnapshot.child("name").getValue().toString();
                       final String organ_image = dataSnapshot.child("imageUrl").getValue().toString();
                       final String organ_lastDate = dataSnapshot.child("lastDate").getValue().toString();

                       organizationResultViewHolder.nameTextView.setText(organ_name);
                       organizationResultViewHolder.lastDonateTextView.setText(organ_lastDate);
                       organizationResultViewHolder.groupTextView.setText("(" + searchGroup + ")");
                       organizationResultViewHolder.institutionTextView.setText(searchArea);
                       Picasso.get().load(organ_image).into(organizationResultViewHolder.circleImageView);

                       organizationResultViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               String visit_user_Id = getRef(it).getKey();
                               Intent AccountIntent = new Intent(Organization.this,AccountActivity.class);
                               AccountIntent.putExtra("visit_user_Id",visit_user_Id);
                               startActivity(AccountIntent);
                           }
                       });

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
                organizationResultViewHolder.nameTextView.setText(upload.getName());
                organizationResultViewHolder.lastDonateTextView.setText(upload.getLastDate());
                organizationResultViewHolder.groupTextView.setText("(" + searchGroup + ")");
                organizationResultViewHolder.institutionTextView.setText(searchArea);
                Picasso.get().load(upload.getImageUrl()).into(organizationResultViewHolder.circleImageView);

                organizationResultViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_Id = getRef(it).getKey();
                        Intent AccountIntent = new Intent(Organization.this,AccountActivity.class);
                        AccountIntent.putExtra("visit_user_Id",visit_user_Id);
                        //AccountIntent.putExtra("Institution",searchArea);
                        startActivity(AccountIntent);
                    }
                });

           }



           @NonNull
           @Override
           public organizationResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organization_recycler,parent,false);
               Organization.organizationResultViewHolder holder = new Organization.organizationResultViewHolder(view);
               return holder;
           }
       };

       recyclerView.setAdapter(redCrescentAdapter);
       redCrescentAdapter.startListening();

    }

    public static class organizationResultViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView,groupTextView,lastDonateTextView,institutionTextView;
        CircleImageView circleImageView;
        public organizationResultViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.recycler_name_id);
            groupTextView = itemView.findViewById(R.id.recycler_blood_Group_id);
            lastDonateTextView = itemView.findViewById(R.id.recycler_donation_id);
            institutionTextView = itemView.findViewById(R.id.recycler_institution_id);
            circleImageView = itemView.findViewById(R.id.recycler_profile_image);

        }
    }

    private void goneButton() {

        organizationName.setVisibility(View.VISIBLE);
        organizationTextView.setVisibility(View.GONE);
        badhanCard.setVisibility(View.GONE);
        sandhaniCard.setVisibility(View.GONE);
        redcrescentCard.setVisibility(View.GONE);
        medicineCard.setVisibility(View.GONE);
        mtoolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Organizations");
    }

    private void visibleButtons() {
        areaText.setVisibility(View.GONE);
        organizationTextView.setVisibility(View.VISIBLE);
        mtoolbar.setVisibility(View.GONE);
        badhanCard.setVisibility(View.VISIBLE);
        sandhaniCard.setVisibility(View.VISIBLE);
        redcrescentCard.setVisibility(View.VISIBLE);
        medicineCard.setVisibility(View.VISIBLE);
        institutionText.setVisibility(View.GONE);
        deptText.setVisibility(View.GONE);
        hallNameText.setVisibility(View.GONE);
        redCrescentDone.setVisibility(View.GONE);
        completeRegistration.setVisibility(View.GONE);
        organizationName.setVisibility(View.GONE);
        badhanInstitutionText.setVisibility(View.GONE);
        sandhaniInstitutionText.setVisibility(View.GONE);
    }
}
