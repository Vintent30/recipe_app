package com.example.recipe_app.Controller;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_app.Adapter.ViewPagerAdapterNav;
import com.example.recipe_app.Fragment.UserFragment;
import com.example.recipe_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;  // Sử dụng CustomViewPager
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav), (v, insets) -> {
            WindowInsetsCompat windowInsetsCompat = insets;
            boolean isKeyboardVisible = windowInsetsCompat.isVisible(WindowInsetsCompat.Type.ime());

            if (isKeyboardVisible) {
                // Bàn phím xuất hiện, ẩn BottomNavigationView
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                // Bàn phím ẩn, hiện lại BottomNavigationView
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

            return insets;
        });
        // Nút logout và điều hướng sang Register Activity
//        Button btn_log = findViewById(R.id.btn_logout);
//        btn_log.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myintent = new Intent(MainActivity.this, Register.class);
//                startActivity(myintent);
//            }
//        });
        viewPager = findViewById(R.id.view_pager);  // Tìm CustomViewPager
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Khởi tạo ViewPagerAdapter với FragmentManager và hành vi của adapter
        ViewPagerAdapterNav adapter = new ViewPagerAdapterNav(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter); // Đặt adapter cho ViewPager

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.homeIcon).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.categoryIcon).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.favouriteIcon).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.userIcon).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.homeIcon) {
                    viewPager.setCurrentItem(0);
                } else if (itemId == R.id.categoryIcon) {
                    viewPager.setCurrentItem(1);
                } else if (itemId == R.id.favouriteIcon) {
                    viewPager.setCurrentItem(2);
                } else if (itemId == R.id.userIcon) {
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }

}