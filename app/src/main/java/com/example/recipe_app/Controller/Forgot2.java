package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;
public class Forgot2 extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnSendOtp;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private String phone;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgot_password_2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fg_p2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etNewPassword = findViewById(R.id.et_new_pass);
        etConfirmPassword = findViewById(R.id.et_confirm_pass);
        btnSendOtp = findViewById(R.id.btn_send_otp);

        // Lấy số điện thoại từ Intent
        phone = getIntent().getStringExtra("PHONE_NUMBER");

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");

        btnSendOtp.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(Forgot2.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(Forgot2.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                sendOtp(phone);
            }
        });
    }
    private void sendOtp(String phone) {
        // Tạo mã OTP ngẫu nhiên
        otp = String.valueOf(new Random().nextInt(999999));
        // Gửi OTP đến số điện thoại (thay thế bằng cách gửi SMS thực tế)
        Toast.makeText(this, "Mã OTP: " + otp, Toast.LENGTH_SHORT).show(); // Chỉ là ví dụ, thay bằng SMS API thực tế

        Intent intent = new Intent(Forgot2.this, Forgot3.class);
        intent.putExtra("PHONE_NUMBER", phone);
        intent.putExtra("NEW_PASSWORD", etNewPassword.getText().toString().trim());
        intent.putExtra("OTP", otp);
        startActivity(intent);
    }
}