package com.example.recipe_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import view.Register;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);  // layout chính của MainActivity

        // Cài đặt màu cho Status Bar nếu phiên bản Android >= Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.orange));
        }

        // Thiết lập Insets cho layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.setting), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nút logout và điều hướng sang Register Activity
        Button btn_log = findViewById(R.id.btn_logout);
        btn_log.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent); // Chuyển hướng sang Register Activity
            finish();  // Đóng MainActivity sau khi chuyển hướng
        });
    }
}
