package com.example.evelyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference,historyRef;
    private String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        mToolbar = (Toolbar)findViewById(R.id.history_toolbar_id);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("History").child(currentUser);
        historyRef = FirebaseDatabase.getInstance().getReference();


        recyclerView = findViewById(R.id.history_recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        loadData();

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<HistoryUpload>()
                .setQuery(databaseReference,HistoryUpload.class)
                .build();

        FirebaseRecyclerAdapter<HistoryUpload,HistoryActivity.HistoryViewHolder> adapter =
                new FirebaseRecyclerAdapter<HistoryUpload, HistoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final HistoryViewHolder historyViewHolder, int i, @NonNull HistoryUpload historyUpload) {
                        String key = getRef(i).getKey();

                        historyRef.child("History").child(currentUser).child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String date = dataSnapshot.child("date").getValue().toString();
                                String hospital = dataSnapshot.child("hospital").getValue().toString();
                                historyViewHolder.dateTextView.setText("Date : "+date);
                                historyViewHolder.hospitalTextView.setText("Hospital : "+hospital);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public HistoryActivity.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_recycler,parent,false);
                       HistoryActivity.HistoryViewHolder viewHolder = new HistoryViewHolder(view);
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView,hospitalTextView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.history_list_date);
            hospitalTextView = itemView.findViewById(R.id.history_list_hospital);
        }
    }
}
