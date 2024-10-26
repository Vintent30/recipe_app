package com.example.recipe_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.recipe_app.Fragment.Fragment_NoPlan;
import com.example.recipe_app.Fragment.Fragment_Today;
import com.example.recipe_app.Fragment.Fragment_Week;

public class ViewPagerAdaperPlan extends FragmentPagerAdapter {
    public ViewPagerAdaperPlan(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Fragment_Today(); // Fragment cho tab "Followers"
            case 1:
                return new Fragment_Week();
            case 2:
                return new Fragment_NoPlan();// Fragment cho tab "Following"
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3; // Số lượng tab
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Hôm nay";
            case 1:
                return "Trong tuần";
            case 2:
                return "Không theo lịch";
            default:
                return null;
        }

    }
}
