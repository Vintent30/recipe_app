package com.example.recipe_app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.FollowAdapter;
import com.example.recipe_app.Model.UserFollow;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentFollower extends Fragment {

    private RecyclerView recyclerView;
    private FollowAdapter followAdapter;
    private List<UserFollow> userList;
    private String currentUserId;

    public FragmentFollower(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follower, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFollower);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        followAdapter = new FollowAdapter(userList, getContext(), currentUserId);
        recyclerView.setAdapter(followAdapter);

        fetchFollowerUsers();

        return view;
    }



    private void fetchFollowerUsers() {
        DatabaseReference userFollowsRef = FirebaseDatabase.getInstance().getReference("UserFollows")
                .child(currentUserId).child("follower");

        userFollowsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String followerUserId = dataSnapshot.getKey();
                    fetchUserInfo(followerUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void fetchUserInfo(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userName = snapshot.child("name").getValue(String.class);
                    String avatarUrl = snapshot.child("avatar").getValue(String.class);
                    userList.add(new UserFollow(userId, userName, avatarUrl));
                    followAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


}