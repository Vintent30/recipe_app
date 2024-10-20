package com.example.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Sign_in extends AppCompatActivity {
    Button btnSignIn;
    TextView signup;
    TextView forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnSignIn;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btn_Sign_in);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign_in.this,MainActivity.class));
            }
        });
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
}