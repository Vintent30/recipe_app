package com.example.recipe_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdaper extends FragmentStateAdapter {
    public ViewPagerAdaper(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentFollowing();
            case 1:
                return new FragmentFollower();
            default:
                return new FragmentFollowing();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
