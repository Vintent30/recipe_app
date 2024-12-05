package com.example.recipe_app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.FavouriteAdapter;
import com.example.recipe_app.Model.Favourite;
import com.example.recipe_app.R;
import com.example.recipe_app.Controller.DishRecipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private DatabaseReference database;
    private List<Favourite> favouriteList = new ArrayList<>();
    private Spinner spinner;
    private boolean isSpinnerInitialized = false;

    public FavouriteFragment() {
        // Default constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().getReference();

        // Initialize Spinner
        spinner = view.findViewById(R.id.spinner);

        // Fetch data from Firebase
        fetchDataFromFirebase();

        return view;
    }

    private void setupSpinner() {
        String[] sortingOptions = {"Sắp xếp theo tên", "Sắp xếp theo calo"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sortingOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerInitialized) {
                    switch (position) {
                        case 0:
                            Collections.sort(favouriteList, Comparator.comparing(Favourite::getName));
                            break;
                        case 1:
                            Collections.sort(favouriteList, Comparator.comparingInt(Favourite::getCalories));
                            break;
                    }
                    favouriteAdapter.notifyDataSetChanged();
                } else {
                    isSpinnerInitialized = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchDataFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("UserLikes").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouriteList.clear();

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    String recipeId = recipeSnapshot.getKey();
                    String name = recipeSnapshot.child("name").getValue(String.class);
                    String imageUrl = recipeSnapshot.child("image").getValue(String.class);
                    Integer calories = recipeSnapshot.child("calories").getValue(Integer.class);
                    Boolean likeStatus = recipeSnapshot.child("likeStatus").getValue(Boolean.class);

                    if (name == null) name = "Unknown";
                    if (imageUrl == null) imageUrl = "";
                    if (calories == null) calories = 0;
                    if (likeStatus == null) likeStatus = false;

                    Favourite favourite = new Favourite(recipeId, name, imageUrl, calories);
                    favourite.setFavorite(likeStatus);
                    favouriteList.add(favourite);
                }

                favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
                recyclerView.setAdapter(favouriteAdapter);

                favouriteAdapter.setOnItemClickListener(position -> {
                    Favourite selectedFavourite = favouriteList.get(position);
                    Intent intent = new Intent(getContext(), DishRecipe.class);
                    intent.putExtra("recipeId", selectedFavourite.getRecipeId());
                    startActivity(intent);
                });

                setupSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
