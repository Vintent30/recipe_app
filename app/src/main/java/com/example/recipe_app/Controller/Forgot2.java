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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Forgot2 extends AppCompatActivity {
    private FirebaseAuth auth;
    private String email;
    private EditText otpEditText;
    private TextView resendOtpTextView;
    private static final long RESEND_OTP_INTERVAL = 30000; // 30 giây
    private long lastOtpSentTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_2);

        otpEditText = findViewById(R.id.et_otp);
        Button verifyButton = findViewById(R.id.btn_verifyOtp);
        resendOtpTextView = findViewById(R.id.tv_resend_otp);

        auth = FirebaseAuth.getInstance();
        email = getIntent().getStringExtra("email");

        // Gửi mã OTP khi vào màn hình OTP
        sendOtp(email);

        // Xác minh OTP
        verifyButton.setOnClickListener(v -> verifyOtp());

        // Gửi lại mã OTP
        resendOtpTextView.setOnClickListener(v -> {
            if (System.currentTimeMillis() - lastOtpSentTime >= RESEND_OTP_INTERVAL) {
                sendOtp(email);
                Toast.makeText(this, "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vui lòng chờ trước khi gửi lại mã OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtp(String email) {
        lastOtpSentTime = System.currentTimeMillis();
        // TODO: Thực hiện gửi OTP đến email
        Toast.makeText(this, "Mã OTP đã được gửi đến " + email, Toast.LENGTH_SHORT).show();
    }

    private void verifyOtp() {
        String enteredOtp = otpEditText.getText().toString();

        if (enteredOtp.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: Kiểm tra mã OTP hợp lệ
            Toast.makeText(this, "OTP hợp lệ", Toast.LENGTH_SHORT).show();
            // Chuyển sang màn hình nhập mật khẩu mới nếu OTP đúng
            Intent intent = new Intent(this, Forgot3.class);
            startActivity(intent);
            finish();
        }
    }
}
