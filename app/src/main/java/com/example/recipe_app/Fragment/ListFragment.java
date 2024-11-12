package com.example.recipe_app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Controller.ListDetail;
import com.example.recipe_app.Model.FirebaseHelper;
import com.example.recipe_app.R;
import com.example.recipe_app.Adapter.FoodCategoryAdapter;
import com.example.recipe_app.Model.Category;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FoodCategoryAdapter foodCategoryAdapter;
    private FirebaseHelper firebaseHelper; // Đối tượng FirebaseHelper
    private List<Category> categoryList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Khởi tạo FirebaseHelper
        firebaseHelper = new FirebaseHelper();

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách
        categoryList = new ArrayList<>();

        // Cài đặt adapter và xử lý click vào từng mục
        foodCategoryAdapter = new FoodCategoryAdapter(categoryList, new FoodCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                // Chuyển đến activity ListDetail khi click vào mục
                startActivity(new Intent(getActivity(), ListDetail.class));
            }
        });

        recyclerView.setAdapter(foodCategoryAdapter);

        // Lấy dữ liệu từ Firebase
        fetchCategoriesFromFirebase();

        return view;
    }

    private void fetchCategoriesFromFirebase() {
        DatabaseReference categoriesRef = firebaseHelper.getCategoriesFromFirebase();

        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> listCategory = new ArrayList<>();

                // Duyệt qua từng category trong Firebase
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("name").getValue(String.class);
                    String imageUrl = categorySnapshot.child("image").getValue(String.class);

                    // Tạo đối tượng Category với tên và hình ảnh
                    Category category = new Category(categoryName, imageUrl);

                    // Thêm vào danh sách
                    listCategory.add(category);
                }

                // Cập nhật dữ liệu cho adapter
                foodCategoryAdapter.setData(listCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi lấy dữ liệu từ Firebase
            }
        });
    }
}
