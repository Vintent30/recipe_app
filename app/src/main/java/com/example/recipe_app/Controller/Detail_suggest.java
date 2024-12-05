package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.DetailAdapter;
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Detail_suggest extends AppCompatActivity {
    private ImageView imageView;
    private DetailAdapter detailAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.detail_suggest);

        // Áp dụng WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_suggest), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.btnback);
        imageView.setOnClickListener(view -> finish());

        // Khởi tạo RecyclerView và Adapter
        recyclerView = findViewById(R.id.rcv_detail_sg);
        detailAdapter = new DetailAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(detailAdapter);

        // Lấy dữ liệu từ Firebase
        String categoryTitle = getIntent().getStringExtra("category_title");
        if (categoryTitle != null) {
            fetchDataFromFirebase(categoryTitle);
        }

        // Cài đặt sự kiện click cho Adapter
        detailAdapter.setOnItemClickListener(recipeId -> {
            Intent intent = new Intent(Detail_suggest.this, DishRecipe.class);
            intent.putExtra("recipeId", recipeId); // Truyền recipeId qua Intent
            startActivity(intent);
        });
    }

    private void fetchDataFromFirebase(String categoryTitle) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CategoryHome");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Recipe> recipeList = new ArrayList<>();

                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        String categoryName = categorySnapshot.child("name").getValue(String.class);

                        if (categoryName != null && categoryName.equals(categoryTitle)) {
                            for (DataSnapshot recipeSnapshot : categorySnapshot.child("foods").getChildren()) {
                                Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                                if (recipe != null) {
                                    recipeList.add(recipe);
                                }
                            }
                        }
                    }

                    detailAdapter.setData(recipeList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Detail_suggest", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}
