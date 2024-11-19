package com.example.recipe_app.Controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.HorizontalRecipeAdapter;
import com.example.recipe_app.Model.Detail;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListDetail extends AppCompatActivity {

    private static final String TAG = "ListDetail";

    private ImageView imageView;
    private RecyclerView recyclerViewHorizontal;
    private HorizontalRecipeAdapter adapter;
    private List<Detail> horizontalList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve layoutId from Intent (default value is 1 if not provided)
        int layoutId = getIntent().getIntExtra("layoutId", 1); // Default to 1
        setLayoutById(layoutId);

        // Initialize UI components
        initializeUI();

        // Retrieve categoryId from Intent
        String categoryId = getIntent().getStringExtra("categoryId");
        Log.d(TAG, "Received categoryId: " + categoryId);

        // Check if categoryId is provided and fetch data from Firebase
        if (categoryId != null && !categoryId.isEmpty()) {
            fetchDetails(categoryId);
        } else {
            Toast.makeText(this, "Category ID not provided", Toast.LENGTH_SHORT).show();
        }
    }

    // Set layout based on layoutId received from Intent
    private void setLayoutById(int layoutId) {
        switch (layoutId) {
            case 1:
                setContentView(R.layout.list_detail); // Layout 1
                break;
            case 2:
                setContentView(R.layout.list_detail2); // Layout 2
                break;
            case 3:
                setContentView(R.layout.list_detail3); // Layout 3
                break;
            case 4:
                setContentView(R.layout.list_detail4); // Layout 4
                break;
            case 5:
                setContentView(R.layout.list_detail5); // Layout 5
                break;
            default:
                setContentView(R.layout.list_detail2); // Default layout
                break;
        }
    }

    // Initialize UI components
    private void initializeUI() {
        imageView = findViewById(R.id.logo);
        progressBar = findViewById(R.id.progress_bar);
        recyclerViewHorizontal = findViewById(R.id.recycler_view_horizontal);

        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        horizontalList = new ArrayList<>();
        adapter = new HorizontalRecipeAdapter(this, horizontalList);
        recyclerViewHorizontal.setAdapter(adapter);

        // Back button functionality
        imageView.setOnClickListener(view -> onBackPressed());
    }

    // Fetch recipe details from Firebase based on categoryId
    private void fetchDetails(String categoryId) {
        // Show ProgressBar while data is loading
        progressBar.setVisibility(View.VISIBLE);

        // Connect to Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Recipes");

        // Query Firebase using categoryId
        Query query = reference.orderByChild("categoryId").equalTo(categoryId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar once data is loaded

                horizontalList.clear(); // Clear old data
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Detail detail = dataSnapshot.getValue(Detail.class);
                        if (detail != null) {
                            horizontalList.add(detail); // Add detail to the list
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify adapter to refresh the data
                } else {
                    // Notify user if no data found
                    Toast.makeText(ListDetail.this, "No data found for this category.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE); // Hide ProgressBar if there's an error
                Log.e(TAG, "Firebase Error: " + error.getMessage());
                Toast.makeText(ListDetail.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
