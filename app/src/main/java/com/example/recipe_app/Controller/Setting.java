package com.example.recipe_app.Controller;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe_app.R;

public class Setting extends AppCompatActivity {
    ImageView imageView;
    RelativeLayout relativeLayout, setting_show, feeback, share;
    Button button;
    ScriptGroup.Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting);

        setting_show = findViewById(R.id.setting_show);
        setting_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.example.recipe_app")); // Thay đổi với package name của ứng dụng bạn muốn mở
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Nếu không có Google Play, mở trình duyệt với liên kết đến Google Play
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.example.recipe_app"));
                    startActivity(intent);
                }
            }
        });
        feeback= findViewById(R.id.setting_feedback);
        feeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                } else {
                }
            }
        });
        share= findViewById(R.id.setting_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, " https://play.google.com/store/apps/details?id=com.example.recipe_app");

                // Mở hộp thoại chia sẻ
                startActivity(Intent.createChooser(shareIntent, "Share app using"));
            }
        });
        imageView = findViewById(R.id.back_setting);
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
        relativeLayout = findViewById(R.id.edit_info);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Setting.this, Detail_user.class));
            }
        });
        button = findViewById(R.id.btn_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Setting.this, Sign_in.class));
            }
        });
    }
}
