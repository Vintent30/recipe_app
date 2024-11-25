package com.example.recipe_app.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_app.Adapter.FollowViewPagerAdaper;
import com.example.recipe_app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Follow extends AppCompatActivity {
    ImageView imageView;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView profileNameTextView;

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

        FollowViewPagerAdaper viewPagerAdapter = new FollowViewPagerAdaper(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        // Kết nối TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Lấy TextView profile_name để hiển thị tên người dùng
        profileNameTextView = findViewById(R.id.profile_name);

        // Lấy userId hiện tại từ FirebaseAuth (hoặc cách khác nếu bạn lưu trữ userId riêng)
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Lấy tên người dùng từ Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(currentUserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Giả sử tên người dùng được lưu dưới trường "fullName"
                    String fullName = dataSnapshot.child("name").getValue(String.class);
                    profileNameTextView.setText(fullName);  // Set tên vào TextView
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
}
