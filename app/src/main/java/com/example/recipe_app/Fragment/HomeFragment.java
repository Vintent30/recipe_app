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
import com.example.recipe_app.Model.FoodHome;
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

    // Fetch data from Firebase
    private void fetchDataFromFirebase() {
        mDatabase.child("CategoryHome").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<categoryHome> listCategory = new ArrayList<>();

                // Duyệt qua từng category trong Firebase
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("name").getValue(String.class);
                    List<FoodHome> foodHomeList = new ArrayList<>();

                    // Duyệt qua các món ăn trong mỗi category
                    for (DataSnapshot foodSnapshot : categorySnapshot.child("foods").getChildren()) {
                        String title = foodSnapshot.child("title").getValue(String.class);
                        String save = foodSnapshot.child("saves").getValue(String.class); // Lấy giá trị kiểu String từ Firebase
                        if (save == null || save.isEmpty()) {
                            save = "0"; // Nếu không có giá trị, mặc định là 0
                        }
                        save += " lượt lưu";
                        String imageUrl = foodSnapshot.child("imageUrl").getValue(String.class);

                        // Tạo đối tượng FoodHome với dữ liệu lấy từ Firebase
                        FoodHome food = new FoodHome(imageUrl, title, save);  // Lưu imageUrl vào resourceId
                        foodHomeList.add(food);
                    }

                    // Tạo đối tượng categoryHome và thêm vào danh sách
                    listCategory.add(new categoryHome(categoryName, R.drawable.baseline_arrow_forward_24, foodHomeList));
                }

                // Cập nhật dữ liệu cho adapter
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
    public void onFoodClick(FoodHome food) {
        Intent intent = new Intent(getActivity(), DishRecipe.class);
        intent.putExtra("food_title", food.getTitle());
        intent.putExtra("food_description", food.getSave());
        intent.putExtra("food_image", food.getResourceId());  // Chuyển URL hình ảnh vào Intent
        startActivity(intent);
    }
}
