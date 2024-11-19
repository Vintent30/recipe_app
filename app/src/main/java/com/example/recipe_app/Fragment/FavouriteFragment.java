package com.example.recipe_app.Fragment;

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
    private boolean isSpinnerInitialized = false; // Tránh kích hoạt spinner khi khởi tạo lần đầu

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
        // Các tùy chọn sắp xếp
        String[] sortingOptions = {"Sắp xếp theo tên", "Sắp xếp theo calo"};

        // Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sortingOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Xử lý sự kiện khi chọn mục trong Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerInitialized) {
                    switch (position) {
                        case 0: // Sắp xếp theo tên
                            Collections.sort(favouriteList, new Comparator<Favourite>() {
                                @Override
                                public int compare(Favourite f1, Favourite f2) {
                                    return f1.getName().compareTo(f2.getName());
                                }
                            });
                            break;

                        case 1: // Sắp xếp theo calo
                            Collections.sort(favouriteList, new Comparator<Favourite>() {
                                @Override
                                public int compare(Favourite f1, Favourite f2) {
                                    return Integer.compare(f1.getCalories(), f2.getCalories());
                                }
                            });
                            break;
                    }
                    // Cập nhật lại adapter sau khi sắp xếp
                    favouriteAdapter.notifyDataSetChanged();
                } else {
                    isSpinnerInitialized = true; // Bỏ qua lần đầu Spinner khởi tạo
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không chọn
            }
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

                // Khởi tạo adapter sau khi dữ liệu được tải
                favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
                recyclerView.setAdapter(favouriteAdapter);

                // Cài đặt Spinner sau khi dữ liệu đã được tải
                setupSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
