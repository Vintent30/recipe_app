package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Model.UserFollow;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {

    private List<UserFollow> userList;
    private Context context;
    private String currentUserId;

    public FollowAdapter(List<UserFollow> userList, Context context, String currentUserId) {
        this.userList = userList;
        this.context = context;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {
        UserFollow user = userList.get(position);
        holder.userNameTextView.setText(user.getUserName());

        // Load avatar image using Glide or any other image loading library
        Glide.with(context)
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.hinh)
                .error(R.drawable.hinh)
                .into(holder.userAvatarImageView);

        // Check if the current user is already following this user
        checkIfFollowed(user.getUserId(), holder.followButton);

        // Set up Follow/Unfollow button logic
        holder.followButton.setOnClickListener(v -> {
            DatabaseReference userFollowsRef = FirebaseDatabase.getInstance().getReference("UserFollows").child(currentUserId);
            DatabaseReference followedUserRef = FirebaseDatabase.getInstance().getReference("UserFollows").child(user.getUserId());

            // Check the current button state (Follow/Unfollow)
            if (holder.followButton.getText().equals("Follow")) {
                // Follow the user
                userFollowsRef.child("following").child(user.getUserId()).setValue(true);
                followedUserRef.child("follower").child(currentUserId).setValue(true);

                // Update the follower count for both users
                updateFollowCount(currentUserId, "following", 1);
                updateFollowCount(user.getUserId(), "followers", 1);

                holder.followButton.setText("Unfollow");
                Toast.makeText(context, "You are now following " + user.getUserName(), Toast.LENGTH_SHORT).show();
            } else if (holder.followButton.getText().equals("Unfollow")) {
                // Unfollow the user
                userFollowsRef.child("following").child(user.getUserId()).removeValue();
                followedUserRef.child("follower").child(currentUserId).removeValue();

                // Update the follower count for both users
                updateFollowCount(currentUserId, "following", -1);
                updateFollowCount(user.getUserId(), "followers", -1);

                holder.followButton.setText("Follow");
                Toast.makeText(context, "You have unfollowed " + user.getUserName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfFollowed(String userId, Button followButton) {
        DatabaseReference userFollowsRef = FirebaseDatabase.getInstance().getReference("UserFollows")
                .child(currentUserId).child("following").child(userId);

        userFollowsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    followButton.setText("Unfollow");
                } else {
                    followButton.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void updateFollowCount(String userId, String field, int change) {
        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);
        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer count = snapshot.child(field).getValue(Integer.class);
                    if (count == null) {
                        count = 0; // Nếu giá trị là null, đặt mặc định là 0
                    }
                    accountRef.child(field).setValue(count + change);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class FollowViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatarImageView;
        TextView userNameTextView;
        Button followButton;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatarImageView = itemView.findViewById(R.id.userAvatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            followButton = itemView.findViewById(R.id.followButton);
        }
    }
}
