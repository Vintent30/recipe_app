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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    private ImageView settingIcon, profilePicture;
    private Button createRecipeButton;
    private TextView followerTextView, username;
    private List<User> userList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Khởi tạo danh sách người dùng
        userList = new ArrayList<>();
        userList.add(new User("Alice", R.drawable.image_fv11));
        userList.add(new User("Bob", R.drawable.image3));
        userList.add(new User("Charlie", R.drawable.image4));
        userList.add(new User("Diana", R.drawable.image_fv13));

        // Thiết lập RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        UserAdapter adapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);

        // Ánh xạ các view
        settingIcon = view.findViewById(R.id.setting_icon);
        createRecipeButton = view.findViewById(R.id.btn_createRe);
        followerTextView = view.findViewById(R.id.follower);
        profilePicture = view.findViewById(R.id.profilePicture);
        username = view.findViewById(R.id.username);

        // Xử lý sự kiện click
        settingIcon.setOnClickListener(v -> startActivity(new Intent(getActivity(), Setting.class)));
        createRecipeButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), Create_recipe.class)));
        followerTextView.setOnClickListener(v -> startActivity(new Intent(getActivity(), Follow.class)));

        // Lấy ID người dùng đã đăng nhập
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Tham chiếu đến dữ liệu của người dùng trong Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);

        // Lấy tên người dùng từ Firebase và hiển thị
        databaseReference.child("name").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String name = dataSnapshot.getValue(String.class);
                username.setText(name != null ? name : "Không có tên");
            } else {
                username.setText("Không có tên");
            }
        }).addOnFailureListener(e -> username.setText("Không thể tải tên"));

        // Lấy ảnh người dùng từ Firebase và hiển thị
        databaseReference.child("avatarResourceId").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String imageUrl = dataSnapshot.getValue(String.class);
                Picasso.get().load(imageUrl).into(profilePicture);
            } else {
                profilePicture.setImageResource(R.drawable.icon_intro1); // Ảnh mặc định nếu không có ảnh
            }
        }).addOnFailureListener(e -> profilePicture.setImageResource(R.drawable.icon_intro1));

        return view;
    }
}