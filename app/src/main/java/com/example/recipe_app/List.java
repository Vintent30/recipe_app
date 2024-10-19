package com.example.recipe_app;

import android.annotation.SuppressLint;
<<<<<<<< HEAD:app/src/main/java/com/example/recipe_app/ListDetail.java
import android.os.Bundle;
========
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
>>>>>>>> origin/lethanh:app/src/main/java/com/example/recipe_app/List.java

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

<<<<<<<< HEAD:app/src/main/java/com/example/recipe_app/ListDetail.java
public class ListDetail extends AppCompatActivity {
========
public class List extends AppCompatActivity {
    FrameLayout frameLayout;
>>>>>>>> origin/lethanh:app/src/main/java/com/example/recipe_app/List.java
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.list_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_detail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        frameLayout = findViewById(R.id.frame_1);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(List.this, ListDetail.class));
            }
        });
    }
}
