package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.Model.Account;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Sign_up extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    TextView btnback1;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Accounts");

        nameEditText = findViewById(R.id.et_name);
        emailEditText = findViewById(R.id.et_email);
        passwordEditText = findViewById(R.id.et_password);
        registerButton = findViewById(R.id.btn_register);

        registerButton.setOnClickListener(v -> registerUser());

        btnback1 = findViewById(R.id.Sign_in_by_acc);
        btnback1.setOnClickListener(view -> startActivity(new Intent(Sign_up.this, MainActivity.class)));
    }

    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser acc = auth.getCurrentUser();
                        if (acc != null) {
                            String userId = acc.getUid();

                            // Tạo đối tượng Account mới với followers và following là các map trống
                            Account newAcc = new Account(name, email, password);
                            newAcc.setFollowers(new HashMap<>()); // Khởi tạo followers trống
                            newAcc.setFollowing(new HashMap<>()); // Khởi tạo following trống

                            databaseRef.child(userId).setValue(newAcc)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(Sign_up.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Sign_up.this, Sign_in.class)); // Điều hướng đến màn hình đăng nhập
                                            finish();
                                        } else {
                                            Toast.makeText(Sign_up.this, "Failed to save user info.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(Sign_up.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



}