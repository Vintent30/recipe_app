package com.example.recipe_app.Controller;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Sign_in extends AppCompatActivity {

    TextView signup;
    TextView forgot;
    private EditText etPhone, etPassword;
    private Button btnLogin;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        signup = findViewById(R.id.sign_up_by_new_acc);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign_in.this, Sign_up.class));
            }
        });
        forgot = findViewById(R.id.tv_forgopasas_pass);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign_in.this, Forgot1.class));
            }
        });

        etPhone = findViewById(R.id.et_number);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_Sign_in);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");

        btnLogin.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                Toast.makeText(Sign_in.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(phone, password);
            }
        });
    }

    private void loginUser(String phone, String password) {
        // Duyệt qua tất cả người dùng
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean userFound = false; // Biến kiểm tra xem có người dùng không
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String storedPhone = userSnapshot.child("Phone").getValue(String.class);
                    String storedPassword = userSnapshot.child("Password").getValue(String.class);

                    if (storedPhone != null && storedPhone.equals(phone)) {
                        userFound = true; // Tìm thấy người dùng
                        if (storedPassword != null && storedPassword.equals(password)) {
                            Toast.makeText(Sign_in.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            // Điều hướng đến màn hình chính của ứng dụng
                            startActivity(new Intent(Sign_in.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Sign_in.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                        break; // Thoát khỏi vòng lặp khi đã tìm thấy người dùng
                    }
                }
                if (!userFound) {
                    Toast.makeText(Sign_in.this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Sign_in.this, "Lỗi truy cập dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
