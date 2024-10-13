package com.example.recipe_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_app.R;
import com.example.recipe_app.nav.ViewPagerAdapterNav;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import helper.NavigationHelper;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;  // Sử dụng CustomViewPager
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.navigation);
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Sử dụng NavigationHelper để thiết lập điều hướng
        NavigationHelper.setupViewPagerWithBottomNavigation(this, viewPager, bottomNavigationView);
    }
}