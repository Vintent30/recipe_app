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
import com.example.recipe_app.Model.Category;
import com.example.recipe_app.R;
import com.example.recipe_app.Adapter.FoodCategoryAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FoodCategoryAdapter foodCategoryAdapter;
    private List<Category> categoryList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize category list
        categoryList = new ArrayList<>();

        // Set up the adapter and handle click events
        foodCategoryAdapter = new FoodCategoryAdapter(categoryList, new FoodCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                // Determine layoutId based on category name
                int layoutId = getLayoutIdByCategoryName(category.getId()); // Use category ID for more reliable mapping

                // Create an Intent to pass data to ListDetail
                Intent intent = new Intent(getActivity(), ListDetail.class);
                intent.putExtra("categoryId", category.getId());  // Pass category ID
                intent.putExtra("categoryName", category.getName()); // Pass category name
                intent.putExtra("layoutId", layoutId); // Pass layout ID based on category ID
                startActivity(intent); // Start ListDetail activity
            }
        });

        recyclerView.setAdapter(foodCategoryAdapter);

        // Fetch data from Firebase
        fetchCategoriesFromFirebase();

        return view;
    }

    // Method to determine the layoutId based on category ID
    private int getLayoutIdByCategoryName(String categoryId) {
        switch (categoryId) {
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            default:
                return 1; // Default layout ID
        }
    }

    private void fetchCategoriesFromFirebase() {
        // Using Firebase Database to fetch categories
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");

        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> listCategory = new ArrayList<>();

                // Iterate through each category in Firebase
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryId = categorySnapshot.getKey();
                    String categoryName = categorySnapshot.child("name").getValue(String.class);
                    String imageUrl = categorySnapshot.child("image").getValue(String.class);

                    Category category = new Category(categoryId, categoryName, imageUrl);
                    listCategory.add(category);
                }

                // Update the data in the adapter
                foodCategoryAdapter.setData(listCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors when fetching data from Firebase
            }
        });
    }
}
