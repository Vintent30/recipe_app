package com.example.recipe_app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.UserAdapter;
import com.example.recipe_app.Controller.Create_recipe;
import com.example.recipe_app.Controller.Follow;
import com.example.recipe_app.Controller.Setting;
import com.example.recipe_app.Model.User;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    // Khai báo các thành phần UI
    private ImageView settingIcon, profilePicture;
    private Button createRecipeButton;
    private TextView followerTextView, username;
    private List<User> userList;  // Danh sách người dùng cho RecyclerView

    // Phương thức khởi tạo View của Fragment
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Lấy userId từ Bundle hoặc dùng ID mặc định
        String userId = getArguments() != null ? getArguments().getString("userId") : "US02";

        // Tham chiếu đến dữ liệu người dùng trong Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Khởi tạo các thành phần giao diện và thiết lập RecyclerView
        setupViews(view);
        setupRecyclerView(view);

        // Lấy thông tin người dùng từ Firebase và hiển thị
        loadUserData(databaseReference);

        return view;
    }

    // Phương thức thiết lập các thành phần giao diện và sự kiện
    private void setupViews(View view) {
        // Ánh xạ các View từ XML
        settingIcon = view.findViewById(R.id.setting_icon);
        createRecipeButton = view.findViewById(R.id.btn_createRe);
        followerTextView = view.findViewById(R.id.follower);
        profilePicture = view.findViewById(R.id.profilePicture);
        username = view.findViewById(R.id.username);

        // Thiết lập sự kiện click cho các View
        settingIcon.setOnClickListener(v -> startActivity(new Intent(getActivity(), Setting.class)));
        createRecipeButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), Create_recipe.class)));
        followerTextView.setOnClickListener(v -> startActivity(new Intent(getActivity(), Follow.class)));
    }

    // Phương thức thiết lập RecyclerView với danh sách người dùng mẫu
    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));  // Hiển thị theo lưới 2 cột

        // Dữ liệu mẫu cho RecyclerView
        userList = new ArrayList<>();
        userList.add(new User("Trà sữa", R.drawable.image_fv11));
        userList.add(new User("Trứng kho", R.drawable.image3));
        userList.add(new User("Trứng ngâm tương", R.drawable.image4));
        userList.add(new User("Bánh xèo", R.drawable.image_fv13));

        // Thiết lập Adapter và gán vào RecyclerView
        UserAdapter adapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);
    }

    // Phương thức lấy dữ liệu người dùng từ Firebase và hiển thị
    private void loadUserData(DatabaseReference databaseReference) {
        // Lấy tên người dùng từ Firebase
        databaseReference.child("Name").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String name = dataSnapshot.getValue(String.class);
                username.setText(name != null ? name : "Không có tên");
            } else {
                username.setText("Không có tên");
            }
        }).addOnFailureListener(e -> username.setText("Không thể tải tên"));

        // Lấy ảnh người dùng từ Firebase
        databaseReference.child("image").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String imageUrl = dataSnapshot.getValue(String.class);
                Picasso.get().load(imageUrl).into(profilePicture);
            } else {
                profilePicture.setImageResource(R.drawable.icon_intro1);  // Ảnh mặc định nếu không có ảnh
            }
        }).addOnFailureListener(e -> profilePicture.setImageResource(R.drawable.icon_intro1));
    }
}