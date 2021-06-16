package com.example.evelyn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class FirstActivity extends AppCompatActivity {

    private CardView homeCard,organizationCard,searchCard,historyCard,AboutCard,logOutcard;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        init();

        homeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUser(1);
            }
        });

        logOutcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendUser(6);
            }


        });

        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUser(3);
            }
        });

        organizationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUser(2);
            }
        });
        AboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUser(5);
            }
        });
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        homeCard = (CardView)findViewById(R.id.homeCardview);
        organizationCard = (CardView)findViewById(R.id.organizationCardview);
        searchCard = (CardView)findViewById(R.id.searchCardview);
        historyCard = (CardView)findViewById(R.id.historyCardview);
        AboutCard = (CardView)findViewById(R.id.aboutCardview);
        logOutcard = (CardView)findViewById(R.id.logoutCardview);
    }
    public void sendUser(int option) {

        if(option == 6){
            Intent mainAcitivity = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(mainAcitivity);
            finish();
        }

        else if(option == 3){
            Intent mainAcitivity = new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(mainAcitivity);

        }
        else if(option == 5){
            Intent mainAcitivity = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(mainAcitivity);

        }
        else if(option == 2){
            Intent mainAcitivity = new Intent(getApplicationContext(),Organization.class);
            startActivity(mainAcitivity);

        }

        else if(option == 1){
            Intent mainAcitivity = new Intent(getApplicationContext(),ProfileChatActivity.class);
            startActivity(mainAcitivity);
        }

    }
}
