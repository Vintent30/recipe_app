package com.example.recipe_app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.CategoryHomeAdapter;
import com.example.recipe_app.Adapter.FoodHomeAdapter;
import com.example.recipe_app.Controller.ChatHome;
import com.example.recipe_app.Controller.Detail_suggest;
import com.example.recipe_app.Controller.DishRecipe;
import com.example.recipe_app.Controller.Planer;
import com.example.recipe_app.Controller.Search;
import com.example.recipe_app.Controller.chat_community;
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.Model.categoryHome;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements CategoryHomeAdapter.OnCategoryForwardClickListener, FoodHomeAdapter.OnFoodClickListener {

    private RecyclerView rcvCategory;
    private CategoryHomeAdapter categoryHomeAdapter;
    private DatabaseReference mDatabase;
    private EditText edtSearch;
    private ImageView imageView, imgCalen;
    private Button button;
    private ImageView imgViewChat;

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

        imgViewChat = view.findViewById(R.id.Icon_chat);
        imgViewChat.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChatHome.class)));

        button = view.findViewById(R.id.chat_community);
        button.setOnClickListener(v -> startActivity(new Intent(getActivity(), chat_community.class)));

        // Initialize EditText for search
        edtSearch = view.findViewById(R.id.et_search);

        // Set listener for the Enter key in EditText
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                String searchQuery = edtSearch.getText().toString().trim();
                if (!searchQuery.isEmpty()) {
                    // Open Search Results Activity with the search query
                    Intent intent = new Intent(getActivity(), Search.class);
                    intent.putExtra("search_query", searchQuery);
                    startActivity(intent);
                }
                return true;
            }
            return false;
        });

        return view;
    }
    private void fetchDataFromFirebase() {
        mDatabase.child("Recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listRecommended.clear();
                listPopularRecipes.clear();
                listYouMightLike.clear();

                // Lấy userId của người dùng hiện tại
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Lấy danh sách UserLikes để xác định các category có ít nhất 2 món giống nhau
                mDatabase.child("UserLikes").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userLikesSnapshot) {

                        // Tạo một map để đếm số lượng món ăn theo category
                        Map<String, List<String>> categoryMap = new HashMap<>();

                        // Đếm số lượng món ăn theo category mà người dùng đã thích
                        for (DataSnapshot likeSnapshot : userLikesSnapshot.getChildren()) {
                            String category = likeSnapshot.child("category").getValue(String.class);
                            String recipeId = likeSnapshot.child("recipeId").getValue(String.class);

                            if (!categoryMap.containsKey(category)) {
                                categoryMap.put(category, new ArrayList<>());
                            }

                            categoryMap.get(category).add(recipeId);
                        }

                        // Xử lý dữ liệu trong Recipes
                        for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                            String name = recipeSnapshot.child("name").getValue(String.class);
                            String imageUrl = recipeSnapshot.child("image").getValue(String.class);
                            String category = recipeSnapshot.child("category").getValue(String.class);
                            Integer caloriesObj = recipeSnapshot.child("calories").getValue(Integer.class);
                            int calories = (caloriesObj != null) ? caloriesObj : 0; // Gán 0 nếu giá trị null
                            String recipeId = recipeSnapshot.child("recipeId").getValue(String.class);
                            String recipeUserId = recipeSnapshot.child("userId").getValue(String.class);
                            String status = recipeSnapshot.child("status").getValue(String.class);

                            if (!"active".equalsIgnoreCase(status)) {
                                continue;
                            }
                            if (currentUserId.equals(recipeUserId)) {
                                continue;
                            }

                            // Lấy trường like dưới dạng Long
                            Long likeLong = recipeSnapshot.child("like").getValue(Long.class);
                            int like = (likeLong != null) ? likeLong.intValue() : 0; // Kiểm tra nếu likeLong là null thì gán 0

                            Recipe food = new Recipe(
                                    recipeId,               // recipeId
                                    name,                   // name
                                    calories,               // calories
                                    null,                   // description
                                    category,               // category
                                    imageUrl,               // image
                                    null,                   // video
                                    null,                   // status
                                    null,                   // userId
                                    null,                   // categoryId
                                    like                    // like
                            );

                            // Phân loại món ăn theo các danh mục
                            // Thêm vào listRecommended nếu calories > 30
                            if (calories > 30) {
                                listRecommended.add(food);
                            }

                            // Thêm vào listPopularRecipes nếu like > 3
                            if (like > 3) {
                                listPopularRecipes.add(food);
                            }

                            // Thêm vào listYouMightLike nếu có ít nhất 2 món cùng category trong UserLikes
                            if (categoryMap.containsKey(category) && categoryMap.get(category).size() >= 1) {
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

                        // Cập nhật adapter
                        categoryHomeAdapter.setData(listCategory);
                        categoryHomeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi khi lấy dữ liệu từ Firebase
                    }
                });
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