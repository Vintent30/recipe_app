package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_in extends AppCompatActivity {
    TextView signup;
    TextView forgot;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_Sign_in);
        loginButton.setOnClickListener(v -> loginUser());

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
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Accounts");
                            String userId = user.getUid();

                            // Lấy mật khẩu từ Realtime Database
                            databaseRef.child(userId).child("password").get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful() && task1.getResult() != null) {
                                    String storedPassword = task1.getResult().getValue(String.class);
                                    // So sánh mật khẩu
                                    if (!password.equals(storedPassword)) {
                                        // Cập nhật mật khẩu trong Realtime Database nếu có thay đổi
                                        databaseRef.child(userId).child("password").setValue(password)
                                                .addOnSuccessListener(aVoid ->
                                                        Toast.makeText(Sign_in.this, "Login successful. Password updated.", Toast.LENGTH_SHORT).show())
                                                .addOnFailureListener(e ->
                                                        Toast.makeText(Sign_in.this, "Login successful, but failed to update password.", Toast.LENGTH_SHORT).show());
                                    } else {
                                        Toast.makeText(Sign_in.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        startActivity(new Intent(Sign_in.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Sign_in.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
