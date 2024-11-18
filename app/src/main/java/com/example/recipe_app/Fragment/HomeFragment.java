package com.example.recipe_app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.CategoryHomeAdapter;
import com.example.recipe_app.Adapter.FoodHomeAdapter;
import com.example.recipe_app.Controller.Detail_suggest;
import com.example.recipe_app.Controller.DishRecipe;
import com.example.recipe_app.Controller.Planer;
import com.example.recipe_app.Controller.chat_community;
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.Model.categoryHome;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CategoryHomeAdapter.OnCategoryForwardClickListener, FoodHomeAdapter.OnFoodClickListener {

    private RecyclerView rcvCategory;
    private CategoryHomeAdapter categoryHomeAdapter;
    private DatabaseReference mDatabase;

    private ImageView imageView, imgCalen;
    private Button button;

    List<Recipe> listRecommended = new ArrayList<>();
    List<Recipe> listPopularRecipes = new ArrayList<>();
    List<Recipe> listYouMightLike = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView and Adapter
        rcvCategory = view.findViewById(R.id.rcv_category);
        categoryHomeAdapter = new CategoryHomeAdapter(getContext(), this, this);  // Pass FoodHomeAdapter.OnFoodClickListener and OnCategoryForwardClickListener

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);
        rcvCategory.setAdapter(categoryHomeAdapter);

        // Firebase Realtime Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Lấy dữ liệu từ Firebase
        fetchDataFromFirebase();

        // Set up button click listeners
        imageView = view.findViewById(R.id.Icon_calendar);
        imageView.setOnClickListener(v -> startActivity(new Intent(getActivity(), Planer.class)));

        button = view.findViewById(R.id.chat_community);
        button.setOnClickListener(v -> startActivity(new Intent(getActivity(), chat_community.class)));

        return view;
    }
        //lay du lieu
        private void fetchDataFromFirebase() {
            mDatabase.child("Recipes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listRecommended.clear();
                    listPopularRecipes.clear();
                    listYouMightLike.clear();

                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        String name = recipeSnapshot.child("name").getValue(String.class);
                        String imageUrl = recipeSnapshot.child("image").getValue(String.class);
                        String category = recipeSnapshot.child("category").getValue(String.class);
                        int calories = recipeSnapshot.child("calories").getValue(Integer.class);
                        String recipeId = recipeSnapshot.child("recipeId").getValue(String.class);
                        // Lấy trường like dưới dạng Long
                        Long likeLong = recipeSnapshot.child("like").getValue(Long.class);
                        int like = (likeLong != null) ? likeLong.intValue() : 0; // Kiểm tra nếu likeLong là null thì gán 0

                        Recipe food = new Recipe(
                                recipeId,               // recipeId
                                name,               // name
                                calories,           // calories
                                null,               // description
                                category,           // category
                                imageUrl,           // image
                                null,               // video
                                null,               // status
                                null,               // userId
                                null,               // categoryId
                                like                // like
                        );

                        // Phân loại món ăn theo các danh mục
                        if (calories >100 ) {
                            listRecommended.add(food);
                        }
                        if (like > 3) {
                            listPopularRecipes.add(food);
                        }
                        if ("Món ăn vặt".equals(category)) {
                            listYouMightLike.add(food);
                        }
                    }

                    // Cập nhật dữ liệu cho adapter
                    List<categoryHome> listCategory = new ArrayList<>();
                    listCategory.add(new categoryHome("Đề xuất cho bạn", R.drawable.baseline_arrow_forward_24, listRecommended));
                    listCategory.add(new categoryHome("Công thức phổ biến", R.drawable.baseline_arrow_forward_24, listPopularRecipes));
                    listCategory.add(new categoryHome("Có thể bạn sẽ thích", R.drawable.baseline_arrow_forward_24, listYouMightLike));


                    // Lưu danh mục vào bảng CategoryHome
                    saveCategoryHomeToFirebase(listCategory);

                    categoryHomeAdapter.setData(listCategory);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi khi lấy dữ liệu từ Firebase
                }
            });
        }


    // Handle category click
    @Override
    public void onCategoryForwardClick(String categoryTitle) {
        Intent intent = new Intent(getActivity(), Detail_suggest.class);
        intent.putExtra("category_title", categoryTitle);
        startActivity(intent);
    }
    // Handle food item click
    @Override
    public void onFoodClick(Recipe food) {
        Intent intent = new Intent(getActivity(), DishRecipe.class);
        intent.putExtra("recipeId", food.getRecipeId());
        intent.putExtra("food_title", food.getName());
        intent.putExtra("food_description", food.getDescription());
        intent.putExtra("food_image", food.getImage());  // Chuyển URL hình ảnh vào Intent
        startActivity(intent);
    }
    private void saveCategoryHomeToFirebase(List<categoryHome> categories) {
        DatabaseReference categoryHomeRef = mDatabase.child("CategoryHome");
        categoryHomeRef.removeValue(); // Xóa dữ liệu cũ trước khi thêm mới

        for (categoryHome category : categories) {
            String categoryId = categoryHomeRef.push().getKey(); // Tạo ID tự động cho danh mục
            if (categoryId != null) {
                // Thêm tên danh mục
                categoryHomeRef.child(categoryId).child("name").setValue(category.getNameCategory());

                // Thêm danh sách món ăn vào danh mục
                categoryHomeRef.child(categoryId).child("foods").setValue(category.getFoods());
            }
        }
    }
}
