package com.example.recipe_app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.FavouriteAdapter;
import com.example.recipe_app.Model.Favourite;
import com.example.recipe_app.R;
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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().getReference();

        // Initialize Spinner
        spinner = view.findViewById(R.id.spinner);

        // Fetch data from Firebase
        fetchDataFromFirebase();

        return view;
    }

    private void setupSpinner() {
        // Sorting options
        String[] sortingOptions = {"Sắp xếp theo tên", "Sắp xếp theo calo"};

        // Adapter for Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortingOptions) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setText(isSpinnerInitialized ? sortingOptions[position] : "Sắp xếp");
                return label;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Handle sorting logic
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerInitialized) {
                    switch (position) {
                        case 0: // Sort by name
                            Collections.sort(favouriteList, new Comparator<Favourite>() {
                                @Override
                                public int compare(Favourite f1, Favourite f2) {
                                    return f1.getName().compareTo(f2.getName());
                                }
                            });
                            break;

                        case 1: // Sort by calories
                            Collections.sort(favouriteList, new Comparator<Favourite>() {
                                @Override
                                public int compare(Favourite f1, Favourite f2) {
                                    return Integer.compare(f1.getCalories(), f2.getCalories());
                                }
                            });
                            break;
                    }
                    favouriteAdapter.notifyDataSetChanged(); // Refresh adapter
                } else {
                    isSpinnerInitialized = true; // Skip first trigger
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void fetchDataFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child("favorites").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouriteList.clear();

                // Parse data from Firebase
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String recipeId = itemSnapshot.getKey(); // Get recipeId from key
                    String name = itemSnapshot.child("name").getValue(String.class);
                    String imageUrl = itemSnapshot.child("imageUrl").getValue(String.class);
                    Integer calories = itemSnapshot.child("calories").getValue(Integer.class);

                    // Handle null data
                    if (name == null) name = "Unknown";
                    if (imageUrl == null) imageUrl = "";
                    if (calories == null) calories = 0;

                    Favourite favourite = new Favourite(recipeId, name, imageUrl, calories);
                    favourite.setFavorite(true);
                    favouriteList.add(favourite);
                }

                // Initialize adapter after data is loaded
                favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
                recyclerView.setAdapter(favouriteAdapter);

                // Set up Spinner after adapter is initialized
                setupSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
