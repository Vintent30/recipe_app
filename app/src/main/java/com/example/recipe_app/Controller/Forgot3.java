package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;
public class Forgot3 extends AppCompatActivity {
    private EditText newPasswordEditText, confirmPasswordEditText;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_3);

        newPasswordEditText = findViewById(R.id.et_new_pass);
        confirmPasswordEditText = findViewById(R.id.et_confirm_pass);
        Button confirmButton = findViewById(R.id.btn_confirm);
        auth = FirebaseAuth.getInstance();

        confirmButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Mật khẩu đã được đặt lại thành công", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, Sign_in.class));
                                    finish();
                                } else {
                                    Toast.makeText(this, "Lỗi đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}