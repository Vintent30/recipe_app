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


public class FragmentFollowing extends Fragment {
    private RecyclerView recyclerView;
    private FollowAdapter followAdapterAdapter;
    private List<UserFollow> userList;
    private String currentUserId;

    public FragmentFollowing(String currentUserId) {
        this.currentUserId = currentUserId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFollowing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách và adapter
        userList = new ArrayList<>();
        followAdapterAdapter = new FollowAdapter(userList, getContext(), currentUserId);
        recyclerView.setAdapter(followAdapterAdapter);

        // Gọi hàm fetch dữ liệu
        fetchFollowingUsers();

        return view; // Trả về view cuối cùng
    }

    private void fetchFollowingUsers() {
        DatabaseReference userFollowsRef = FirebaseDatabase.getInstance().getReference("UserFollows")
                .child(currentUserId).child("following");

        userFollowsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String followUserId = dataSnapshot.getKey();
                    fetchUserInfo(followUserId);
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
                    followAdapterAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}