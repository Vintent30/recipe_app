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

import com.example.recipe_app.Adapter.RecipeAdapter;
import com.example.recipe_app.Controller.Create_recipe;
import com.example.recipe_app.Controller.DishRecipe;
import com.example.recipe_app.Controller.Follow;
import com.example.recipe_app.Controller.Setting;
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    private ImageView settingIcon, profilePicture;
    private Button createRecipeButton;
    private TextView followerTextView, followingTextView, username, totalFollowers, totalFollowing;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Initialize the recipe list and RecyclerView
        recipeList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Pass the click listener to the adapter
        recipeAdapter = new RecipeAdapter(getContext(), recipeList, this::onFoodClick);
        recyclerView.setAdapter(recipeAdapter);

        // Initialize views
        settingIcon = view.findViewById(R.id.setting_icon);
        createRecipeButton = view.findViewById(R.id.btn_createRe);
        followerTextView = view.findViewById(R.id.follower);
        followingTextView = view.findViewById(R.id.follow);
        profilePicture = view.findViewById(R.id.profilePicture);
        username = view.findViewById(R.id.username);
        totalFollowers = view.findViewById(R.id.total2); // TextView for followers count
        totalFollowing = view.findViewById(R.id.total1); // TextView for following count

        // Handle follower and setting button clicks
        followerTextView.setOnClickListener(v -> startActivity(new Intent(getActivity(), Follow.class)));
        settingIcon.setOnClickListener(v -> startActivity(new Intent(getActivity(), Setting.class)));
        createRecipeButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), Create_recipe.class)));

        // Get the current logged-in user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);

            // Load user details
            loadUserInfo();
            loadFollowerFollowingCounts();
            loadUserRecipes(userId);
        } else {
            username.setText("Người dùng chưa đăng nhập");
            profilePicture.setImageResource(R.drawable.icon_intro1); // Default image if not logged in
        }

        return view;
    }

    private void loadUserInfo() {
        // Retrieve and display user's name
        userRef.child("name").get().addOnSuccessListener(dataSnapshot -> {
            String name = dataSnapshot.exists() ? dataSnapshot.getValue(String.class) : "Không có tên";
            username.setText(name);
        }).addOnFailureListener(e -> username.setText("Không thể tải tên"));

        // Retrieve and display user's avatar
        userRef.child("avatar").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String imageUrl = dataSnapshot.getValue(String.class);
                Picasso.get().load(imageUrl).into(profilePicture);
            } else {
                profilePicture.setImageResource(R.drawable.icon_intro1);
            }
        }).addOnFailureListener(e -> profilePicture.setImageResource(R.drawable.icon_intro1));
    }

    private void loadFollowerFollowingCounts() {
        // Get followers count
        userRef.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalFollowers.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });

        // Get following count
        userRef.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalFollowing.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });
    }

    private void loadUserRecipes(String userId) {
        // Fetch and display the list of recipes created by the current user
        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("Recipes");
        recipeRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Recipe recipe = data.getValue(Recipe.class);
                    if (recipe != null && "active".equals(recipe.getStatus())) {
                        recipeList.add(recipe);
                    }
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });
    }

    // Handle recipe item click
    public void onFoodClick(Recipe food) {
        Intent intent = new Intent(getActivity(), DishRecipe.class);
        intent.putExtra("recipeId", food.getRecipeId());
        intent.putExtra("food_title", food.getName());
        intent.putExtra("food_description", food.getDescription());
        intent.putExtra("food_image", food.getImage());
        startActivity(intent);
    }
}
