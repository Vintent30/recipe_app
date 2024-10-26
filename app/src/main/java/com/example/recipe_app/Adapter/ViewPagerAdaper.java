package com.example.recipe_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.recipe_app.Fragment.FragmentFollower;
import com.example.recipe_app.Fragment.FragmentFollowing;

public class ViewPagerAdaper extends FragmentPagerAdapter {

    public ViewPagerAdaper(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentFollowing(); // Fragment cho tab "Followers"
            case 1:
                return new FragmentFollower(); // Fragment cho tab "Following"
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2; // Số lượng tab
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Following";
            case 1:
                return "Follower";
            default:
                return null;
        }
    }
}
