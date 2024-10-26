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

public class Forgot1 extends AppCompatActivity {
    private EditText etNumber;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgot_password_1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fg_p1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etNumber = findViewById(R.id.et_number);
        btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> {
            String phone = etNumber.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(Forgot1.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Forgot1.this, Forgot2.class);
                intent.putExtra("PHONE_NUMBER", phone);
                startActivity(intent);
            }
        });
    }
}
