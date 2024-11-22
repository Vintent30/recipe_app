package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot1 extends AppCompatActivity {
    private EditText emailEditText;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_1);

        emailEditText = findViewById(R.id.et_email);
        Button sendOtpButton = findViewById(R.id.btn_send_otp);
        auth = FirebaseAuth.getInstance();

        sendOtpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            if (!email.isEmpty()) {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Điều hướng sang giao diện nhập OTP
                                Intent intent = new Intent(this, Sign_in.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Lỗi gửi email", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
