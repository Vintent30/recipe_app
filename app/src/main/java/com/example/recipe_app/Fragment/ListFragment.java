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


import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FoodCategoryAdapter foodCategoryAdapter;
    private FirebaseHelper firebaseHelper; // Firebase helper instance

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize FirebaseHelper
        firebaseHelper = new FirebaseHelper();

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load sample data and push it to Firebase
        List<Category> categoryList = loadSampleData();
        pushCategoriesToFirebase(categoryList);

        // Set up adapter and item click handling
        foodCategoryAdapter = new FoodCategoryAdapter(categoryList, new FoodCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                // Navigate to ListDetail activity when an item is clicked
                startActivity(new Intent(getActivity(), ListDetail.class));
            }

            @Override
            public void onItemClick(org.tensorflow.lite.support.label.Category category) {

            }
        });

        recyclerView.setAdapter(foodCategoryAdapter);

        return view;
    }

    // Sample data for demonstration purposes
    private List<Category> loadSampleData() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Món Ăn Dinh Dưỡng", R.drawable.image_list1));
        categories.add(new Category("Món Ăn Gia Đình", R.drawable.image_list2));
        categories.add(new Category("Món Ăn Vặt", R.drawable.image_list3));
        categories.add(new Category("Món Ăn Chay", R.drawable.image_list4));
        categories.add(new Category("Đồ uống", R.drawable.image_list5));
        return categories;
    }

    // Push each category in the list to Firebase
    private void pushCategoriesToFirebase(List<Category> categories) {
        for (Category category : categories) {
            firebaseHelper.addCategoryToFirebase(category);
        }
    }
}
