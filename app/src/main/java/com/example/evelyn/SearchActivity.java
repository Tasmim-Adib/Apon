package com.example.evelyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView searchBloodGroupEdit,searchAreaEdit,searchHospitalEdit;
    private Button searchAreaButton,searchHospitalButton,searchSubmitButton,searchNearestYou;
    private int count;
    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    


    private DatabaseReference databaseReference;
    private String bloodGroup;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private Query query;
    String option;

    private ArrayAdapter<String> searchGroupAdapter,searchAreaAdapter,searchAdapterHospital;
    SettingsActivity settingsActivity = new SettingsActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar)findViewById(R.id.search_toolbar_id);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Donor");

        recyclerView = (RecyclerView)findViewById(R.id.find_donor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        searchAreaButton = (Button)findViewById(R.id.Search_area_button);
        searchHospitalButton = (Button)findViewById(R.id.Search_hospital_button);
        searchSubmitButton = (Button)findViewById(R.id.Search_submit_button);
        searchBloodGroupEdit = (AutoCompleteTextView) findViewById(R.id.search_blood_edit);
        searchAreaEdit = (AutoCompleteTextView) findViewById(R.id.search_area_edit);
        searchHospitalEdit = (AutoCompleteTextView) findViewById(R.id.search_hospital_edit);
        searchNearestYou = (Button)findViewById(R.id.Search_near_button);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        searchSubmitButton.setOnClickListener(this);
        searchHospitalButton.setOnClickListener(this);
        searchAreaButton.setOnClickListener(this);
        searchNearestYou.setOnClickListener(this);

        searchGroupAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,settingsActivity.groups);
        searchBloodGroupEdit.setThreshold(1);
        searchBloodGroupEdit.setAdapter(searchGroupAdapter);

        searchAreaAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,settingsActivity.districts);
        searchAreaEdit.setThreshold(1);
        searchAreaEdit.setAdapter(searchAreaAdapter);

        searchAdapterHospital = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,settingsActivity.hospitals);
        searchHospitalEdit.setThreshold(1);
        searchHospitalEdit.setAdapter(searchAdapterHospital);




    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.Search_area_button){
            searchAreaButton.setVisibility(View.GONE);
            searchHospitalButton.setVisibility(View.GONE);
            searchAreaEdit.setVisibility(View.VISIBLE);
            searchSubmitButton.setVisibility(View.VISIBLE);
            searchNearestYou.setVisibility(View.GONE);

            count = 1;
        }
        if(v.getId() == R.id.Search_hospital_button){
            searchHospitalEdit.setVisibility(View.VISIBLE);
            searchSubmitButton.setVisibility(View.VISIBLE);
            searchHospitalButton.setVisibility(View.GONE);
            searchAreaButton.setVisibility(View.GONE);
            searchNearestYou.setVisibility(View.GONE);
            count = 2;
        }
        if(v.getId() == R.id.Search_near_button){
            Intent intent;
            intent = new Intent(getApplicationContext(),Maping.class);
            startActivity(intent);
            finish();


        }
        if(v.getId() == R.id.Search_submit_button){
            bloodGroup = searchBloodGroupEdit.getText().toString();
            if(TextUtils.isEmpty(bloodGroup)){
                searchBloodGroupEdit.setError("Please mention Blood Group");
                searchBloodGroupEdit.requestFocus();
                return;
            }
            if(count == 1){
                option = searchAreaEdit.getText().toString();
                if(TextUtils.isEmpty(option)){
                    searchAreaEdit.setError("Please mention Area");
                    searchAreaEdit.requestFocus();
                    return;
                }
            }
            else{
                option = searchHospitalEdit.getText().toString();

                if(TextUtils.isEmpty(option)){
                    searchHospitalEdit.setError("Please mention hospital name");
                    searchHospitalEdit.requestFocus();
                    return;
                }
            }
            SearchForBlood();

            showRecyclerItem();

        }
    }



    private void SearchForBlood() {
        searchSubmitButton.setVisibility(View.GONE);
        searchAreaEdit.setVisibility(View.GONE);
        searchHospitalEdit.setVisibility(View.GONE);
        searchBloodGroupEdit.setVisibility(View.GONE);
        searchNearestYou.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerItem() {

        if(count == 1){
            query = FirebaseDatabase.getInstance().getReference(bloodGroup).orderByChild("district").equalTo(option);

        }
        else if(count == 2){
            query = FirebaseDatabase.getInstance().getReference(bloodGroup).orderByChild("hospital").equalTo(option);

        }
        FirebaseRecyclerOptions<Upload> options =
                new FirebaseRecyclerOptions.Builder<Upload>()
                        .setQuery(query,Upload.class)
                        .build();

        FirebaseRecyclerAdapter<Upload, SearchActivity.SearchResultViewHolder> adapter =
                new FirebaseRecyclerAdapter<Upload, SearchActivity.SearchResultViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SearchActivity.SearchResultViewHolder searchResultViewHolder, final int i, @NonNull Upload upload) {
                        final String uId = getRef(i).getKey();
                        databaseReference.child(uId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final String nam = dataSnapshot.child("name").getValue().toString();
                                final String chobi = dataSnapshot.child("imageUrl").getValue().toString();
                                final String kobeDese = dataSnapshot.child("lastDate").getValue().toString();
                                searchResultViewHolder.nameText.setText(nam);
                                searchResultViewHolder.bloodGroupText.setText(bloodGroup);
                                searchResultViewHolder.donationText.setText(kobeDese);
                                Picasso.get().load(chobi).into(searchResultViewHolder.image);


                                searchResultViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String visit_user_Id = getRef(i).getKey();
                                        Intent AccountIntent = new Intent(SearchActivity.this,AccountActivity.class);
                                        AccountIntent.putExtra("visit_user_Id",visit_user_Id);
                                        startActivity(AccountIntent);
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
                    public SearchActivity.SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
                        SearchActivity.SearchResultViewHolder viewHolder = new SearchActivity.SearchResultViewHolder(view);
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class SearchResultViewHolder extends RecyclerView.ViewHolder{
        TextView nameText,bloodGroupText,thanaText,districtText,hospitalText,donationText;
        CircleImageView image;
        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.view_name_id);
            bloodGroupText = itemView.findViewById(R.id.view_blood_Group_id);

            image = itemView.findViewById(R.id.View_profile_image);
            donationText = itemView.findViewById(R.id.view_donation_id);
        }
    }
}
