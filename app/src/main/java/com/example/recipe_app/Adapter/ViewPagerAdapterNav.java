
package com.example.recipe_app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.recipe_app.Fragment.FavouriteFragment;
import com.example.recipe_app.Fragment.HomeFragment;
import com.example.recipe_app.Fragment.ListFragment;
import com.example.recipe_app.Fragment.UserFragment;

public class ViewPagerAdapterNav extends FragmentStatePagerAdapter {
    public ViewPagerAdapterNav(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new ListFragment();
            case 2:
                return new FavouriteFragment();
            case 3:
                return new UserFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
