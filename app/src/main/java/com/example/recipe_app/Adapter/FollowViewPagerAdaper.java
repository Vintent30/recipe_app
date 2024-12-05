package com.example.recipe_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.recipe_app.Fragment.FragmentFollower;
import com.example.recipe_app.Fragment.FragmentFollowing;
import com.google.firebase.auth.FirebaseAuth;

public class FollowViewPagerAdaper extends FragmentPagerAdapter {

    public FollowViewPagerAdaper(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        switch (position) {
            case 0:
                return new FragmentFollowing(currentUserId); // Tab "Following"
            case 1:
                return new FragmentFollower(currentUserId); // Tab "Follower"
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
