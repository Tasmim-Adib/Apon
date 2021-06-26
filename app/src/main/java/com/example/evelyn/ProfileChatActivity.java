package com.example.evelyn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileChatActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_chat);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Container_layout,
                new MyProfileFragment()).commit();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    if (item.getItemId() == R.id.dashboard_menu_id) {
                        Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                   }
                   else if (item.getItemId() == R.id.profile_menu_id) {
                       selectedFragment = new MyProfileFragment();
                   }
                   else if (item.getItemId() == R.id.chat_menu_id) {
                       selectedFragment = new MyChatFragment();
                    }
                   else if(item.getItemId() == R.id.update_menu_id){
                       selectedFragment = new UpdateFragment();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_Container_layout,
                            selectedFragment).commit();

                    return true;
                }
            };
}
