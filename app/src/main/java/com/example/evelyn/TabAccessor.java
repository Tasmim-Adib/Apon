package com.example.evelyn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessor extends FragmentPagerAdapter {
    public TabAccessor(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                NeedBloodFragment needBloodFragment = new NeedBloodFragment();
                return needBloodFragment;
            case 2:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case  3:
                ContactsFragment contactsFragment = new ContactsFragment();
                return contactsFragment;

             default:
                 return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Profile";
            case 1:
                return "Request";
            case 2:
                return "Friends";

            case 3:
                return "Chats";

            default:
                return null;

        }
    }
}
