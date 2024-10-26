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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Forgot3 extends AppCompatActivity {
    private EditText etOtp;
    private Button btnConfirmOtp;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private String number;
    private String newPassword;
    private String otp;
    TextView changephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgot_password_3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fg_p3), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        changephone = findViewById(R.id.change_number);
        changephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Forgot3.this, Forgot1.class));
            }
        });

        etOtp = findViewById(R.id.et_otp);
        btnConfirmOtp = findViewById(R.id.btn_confirm);

        // Lấy thông tin từ Intent
        number = getIntent().getStringExtra("PHONE_NUMBER");
        newPassword = getIntent().getStringExtra("NEW_PASSWORD");
        otp = getIntent().getStringExtra("OTP");

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");

        btnConfirmOtp.setOnClickListener(v -> {
            String enteredOtp = etOtp.getText().toString().trim();

            if (TextUtils.isEmpty(enteredOtp)) {
                Toast.makeText(Forgot3.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            } else if (!enteredOtp.equals(otp)) {
                Toast.makeText(Forgot3.this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
            } else {
                updatePassword(number, newPassword);
            }
        });
    }

    private void updatePassword(String phone, String newPassword) {
        // Sử dụng số điện thoại để tìm user trong cơ sở dữ liệu
        mDatabase.orderByChild("Phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy ID người dùng đầu tiên
                    String userId = dataSnapshot.getChildren().iterator().next().getKey();

                    // Cập nhật mật khẩu
                    mDatabase.child(userId).child("Password").setValue(newPassword).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgot3.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Forgot3.this, Sign_in.class));
                            finish();
                        } else {
                            Toast.makeText(Forgot3.this, "Lỗi cập nhật mật khẩu!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Forgot3.this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Forgot3.this, "Lỗi truy cập dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
