package com.example.recipe_app.Controller;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.R;

public class Setting extends AppCompatActivity {
    ImageView imageView;
    RelativeLayout setting_show, feedback, share, relativeLayout;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        // Liên kết các view với ID
        setting_show = findViewById(R.id.setting_show);
        feedback = findViewById(R.id.setting_feedback);
        share = findViewById(R.id.setting_share);
        imageView = findViewById(R.id.back_setting);
        relativeLayout = findViewById(R.id.edit_info);
        button = findViewById(R.id.btn_logout);

        // Mở Google Play Store khi nhấn vào setting_show
        setting_show.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.example.recipe_app"));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.example.recipe_app"));
                startActivity(intent);
            }
        });

        // Gửi phản hồi qua email
        feedback.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));  // Chỉ định rằng Intent này chỉ dành cho email
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@example.com"}); // Địa chỉ email mặc định
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi về ứng dụng"); // Chủ đề mặc định
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Xin hãy ghi lại phản hồi của bạn tại đây..."); // Nội dung mặc định

            try {
                startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng Email")); // Mở ứng dụng Email
            } catch (ActivityNotFoundException e) {
                // Thông báo lỗi nếu không tìm thấy ứng dụng email
                Toast.makeText(Setting.this, "Không tìm thấy ứng dụng email", Toast.LENGTH_SHORT).show();
            }
        });

        // Chia sẻ ứng dụng
        share.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.example.recipe_app");
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ ứng dụng qua"));
        });

        // Nút quay lại
        imageView.setOnClickListener(view -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        });

        // Chuyển đến màn hình chi tiết người dùng
        relativeLayout.setOnClickListener(view -> startActivity(new Intent(Setting.this, Detail_user.class)));

        // Đăng xuất và chuyển đến màn hình đăng nhập
        button.setOnClickListener(view -> startActivity(new Intent(Setting.this, Sign_in.class)));
    }
}
