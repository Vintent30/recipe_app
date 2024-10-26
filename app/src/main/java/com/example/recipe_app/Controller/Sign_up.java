package com.example.recipe_app.Controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Sign_up extends AppCompatActivity {

    private EditText etName, etPhone, etPassword;
    private Button btnSignup;
    private DatabaseReference mDatabase;
    TextView btnback1;
    private static int userCount = 1; // Biến đếm người dùng

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        btnback1 = findViewById(R.id.Sign_in_by_acc);
        btnback1.setOnClickListener(view -> startActivity(new Intent(Sign_up.this, Sign_in.class)));

        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_number);
        etPassword = findViewById(R.id.et_password);
        btnSignup = findViewById(R.id.btn_signup);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        btnSignup.setOnClickListener(v -> {
            String fullName = etName.getText().toString().trim();
            String phoneNumber = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
                Toast.makeText(Sign_up.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(fullName, phoneNumber, password);
            }
        });
    }

    private void registerUser(String fullName, String phoneNumber, String password) {
        // Tạo hashmap để lưu dữ liệu người dùng
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("Name", fullName);
        userMap.put("Phone", phoneNumber);
        userMap.put("Password", password);  // Lưu mật khẩu thô (không an toàn)

        // Tạo ID theo định dạng US01, US02,...
        String userId = "US" + String.format("%02d", userCount); // Tạo ID
        userCount++; // Tăng biến đếm cho người dùng tiếp theo

        // Lưu vào Realtime Database
        mDatabase.child(userId).setValue(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Sign_up.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Sign_up.this, Sign_in.class)); // Điều hướng đến màn hình đăng nhập
                finish();
            } else {
                Toast.makeText(Sign_up.this, "Lỗi khi lưu dữ liệu vào Database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
