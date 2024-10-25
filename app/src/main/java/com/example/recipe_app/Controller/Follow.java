package com.example.recipe_app.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_app.Adapter.ViewPagerAdaper;
import com.example.recipe_app.R;
import com.google.android.material.tabs.TabLayout;

public class Follow extends AppCompatActivity {
    ImageView imageView;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follower);

        // Thiết lập nút back
        imageView = findViewById(R.id.back_fl);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });

        // Thiết lập TabLayout và ViewPager
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdaper viewPagerAdapter = new ViewPagerAdaper(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        // Kết nối TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }
}
