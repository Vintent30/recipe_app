package com.example.recipe_app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.UserAdapter;
import com.example.recipe_app.Controller.Create_recipe;
import com.example.recipe_app.Controller.Follow;
import com.example.recipe_app.Model.User;
import com.example.recipe_app.R;
import com.example.recipe_app.Controller.Setting;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    private ImageView settingIcon;
    private Button createRecipeButton;
    private TextView followerTextView;
    private List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Initialize User List
        userList = new ArrayList<>();
        userList.add(new User("Alice", R.drawable.image_fv11));
        userList.add(new User("Bob", R.drawable.image3));
        userList.add(new User("Charlie", R.drawable.image4));
        userList.add(new User("Diana", R.drawable.image_fv13));

        // RecyclerView Setup
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        UserAdapter adapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);

        // Initialize and set click listeners
        settingIcon = view.findViewById(R.id.setting_icon);
        settingIcon.setOnClickListener(v -> startActivity(new Intent(getActivity(), Setting.class)));

        createRecipeButton = view.findViewById(R.id.btn_createRe);
        createRecipeButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), Create_recipe.class)));

        followerTextView = view.findViewById(R.id.follower);
        followerTextView.setOnClickListener(v -> startActivity(new Intent(getActivity(), Follow.class)));

        return view;
    }
}
