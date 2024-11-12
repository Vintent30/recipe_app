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
import com.example.recipe_app.Model.Item;
import com.example.recipe_app.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private FirebaseFirestore db;
    private DatabaseReference database;

    private List<Favourite> favouriteList = new ArrayList<>(); // Khai báo danh sách yêu thích

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference();

        // Fetch data from Firebase Realtime Database
        fetchDataFromFirestore();

        return view;
    }

    private void fetchDataFromFirestore() {
        // Lấy dữ liệu từ "favorites" node
        database.child("favorites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the list before adding new data
                favouriteList.clear();

                // Duyệt qua từng mục yêu thích trong Firebase
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    // Lấy thông tin từ mỗi item (item1, item2, ...)
                    String name = itemSnapshot.child("name").getValue(String.class);
                    String imageUrl = itemSnapshot.child("imageUrl").getValue(String.class);
                    String calories = itemSnapshot.child("calories").getValue(String.class);

                    // Kiểm tra giá trị null và gán giá trị mặc định nếu cần thiết
                    if (name == null) name = "Unknown";
                    if (imageUrl == null) imageUrl = ""; // Có thể là URL hoặc tên ảnh
                    if (calories == null) calories = "N/A";

                    // Tạo đối tượng Favourite và thêm vào danh sách
                    Favourite favourite = new Favourite(name, imageUrl, calories);
                    favouriteList.add(favourite);
                }

                // Cập nhật dữ liệu cho adapter
                favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
                recyclerView.setAdapter(favouriteAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi lấy dữ liệu từ Firebase
                Log.e("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }
}

