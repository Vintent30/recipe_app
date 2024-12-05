package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    ImageView imageView,imgLike;
    LinearLayout linearLayout;
    private DetailAdapter detailAdapter;
    private RecyclerView recyclerView;  // Khai báo RecyclerView

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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });
        // Khởi tạo các View
        recyclerView = findViewById(R.id.rcv_detail_sg);  // Khởi tạo RecyclerView
        detailAdapter = new DetailAdapter(this, this::onFoodClick);
        // Khởi tạo RecyclerView và LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);  // Gán LayoutManager cho RecyclerView

        // Khởi tạo Adapter với danh sách rỗng ban đầu
        recyclerView.setAdapter(detailAdapter);  // Gán adapter cho RecyclerView

        // Lấy dữ liệu từ Firebase cho danh mục cụ thể
        String categoryTitle = getIntent().getStringExtra("category_title");
        if (categoryTitle != null) {
            fetchDataFromFirebase(categoryTitle);
        }
    }

    private void fetchDataFromFirebase(String categoryTitle) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CategoryHome");

        // Truy vấn Firebase để lấy dữ liệu của tất cả các danh mục
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Detail_suggest", "Data exists: " + dataSnapshot.exists());
                if (dataSnapshot.exists()) {
                    // Tạo danh sách món ăn
                    List<Recipe> recipeList = new ArrayList<>();

                    // Lặp qua tất cả danh mục trong CategoryHome
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        // Lấy tên danh mục
                        String categoryName = categorySnapshot.child("name").getValue(String.class);

                        // Kiểm tra xem danh mục có tên phù hợp với categoryTitle không
                        if (categoryName != null && categoryName.equals(categoryTitle)) {
                            // Lấy tất cả món ăn trong danh mục này
                            for (DataSnapshot recipeSnapshot : categorySnapshot.child("foods").getChildren()) {
                                Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                                if (recipe != null) {
                                    recipeList.add(recipe);  // Thêm món ăn vào danh sách
                                    Log.d("Detail_suggest", "Recipe added: " + recipe.getName());  // Log thêm món ăn
                                }
                            }
                        }
                    }

                    // Debug log để kiểm tra số lượng món ăn lấy được
                    Log.d("Detail_suggest", "Data fetched: " + recipeList.size());

                    // Hiển thị danh sách món ăn lên RecyclerView
                    detailAdapter.setData(recipeList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
                Log.e("Detail_suggest", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }

    public void onFoodClick(Recipe food) {
        Intent intent = new Intent(Detail_suggest.this, DishRecipe.class);
        intent.putExtra("recipeId", food.getRecipeId());
        intent.putExtra("food_title", food.getName());
        intent.putExtra("food_description", food.getDescription());
        intent.putExtra("food_image", food.getImage());
        startActivity(intent);
    }
}
