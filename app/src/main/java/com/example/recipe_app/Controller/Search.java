package com.example.recipe_app.Controller;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.SearchAdapter;
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private DatabaseReference mDatabase;
    private List<Recipe> searchResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.rcv_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Adapter
        searchAdapter = new SearchAdapter(this);
        recyclerView.setAdapter(searchAdapter);

        // Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get search query from Intent
        String searchQuery = getIntent().getStringExtra("search_query");
        searchResults = new ArrayList<>();

        // Fetch data from Firebase
        fetchSearchResults(searchQuery);
    }
    private void fetchSearchResults(String searchQuery) {
        mDatabase.child("Recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                searchResults.clear();

                // Loop through all recipes in Firebase and check if name matches search query
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    String name = recipeSnapshot.child("name").getValue(String.class);
                    if (name != null && name.toLowerCase().contains(searchQuery.toLowerCase())) {
                        // Create Recipe object and add to the search results list
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        searchResults.add(recipe);
                    }
                }

                // Update the adapter with the search results
                searchAdapter.setData(searchResults);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the database error
            }
        });
    }

}