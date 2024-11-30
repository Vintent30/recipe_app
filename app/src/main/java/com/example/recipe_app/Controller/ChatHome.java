package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.ChatHomeAdapter;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatHome extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatHomeAdapter adapter;
    private List<com.example.recipe_app.Model.ChatHome> chatHomes;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_home);

        recyclerView = findViewById(R.id.chathome_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatHomes = new ArrayList<>();
        adapter = new ChatHomeAdapter(chatHomes, chatHome -> {
            // Open chat interface when clicking a message
            Intent intent = new Intent(ChatHome.this, Chat2.class);
            intent.putExtra("receiverId", chatHome.getSenderId());  // receiverId là người nhận (người chat với người dùng hiện tại)
            intent.putExtra("name", chatHome.getSenderName());  // Tên người nhận
            intent.putExtra("imageUrl", chatHome.getAvatarUrl());  // Ảnh đại diện người nhận
            intent.putExtra("recipeId", chatHome.getRecipeId());  // Truyền ID món ăn
            intent.putExtra("recipeName", chatHome.getRecipeName());  // Truyền tên món ăn
            intent.putExtra("recipeImage", chatHome.getRecipeImage());  // Truyền ảnh món ăn
            intent.putExtra("authorId", chatHome.getSenderId());  // ID người gửi là tác giả món ăn
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Lấy currentUserId từ FirebaseAuth
        currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (currentUserId == null) {
            Toast.makeText(this, "Please log in to view messages.", Toast.LENGTH_SHORT).show();
            return;
        }

        loadMessages(); // Lấy các tin nhắn
    }

    private void loadMessages() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages");

        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Set<String> senderIds = new HashSet<>();

                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot messageSnapshot : chatSnapshot.getChildren()) {
                        com.example.recipe_app.Model.ChatHome chatHome = messageSnapshot.getValue(com.example.recipe_app.Model.ChatHome.class);

                        if (chatHome != null && chatHome.getReceiverId().equals(currentUserId)) {
                            if (!chatHome.getSenderId().equals(currentUserId)) {
                                senderIds.add(chatHome.getSenderId());
                            }
                        }
                    }
                }

                fetchUsersInfo(senderIds); // Lấy thông tin người gửi
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Unable to load messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUsersInfo(Set<String> senderIds) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Accounts");

        for (String senderId : senderIds) {
            usersRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String avatar = snapshot.child("avatar").getValue(String.class);

                        com.example.recipe_app.Model.ChatHome chatHome = new com.example.recipe_app.Model.ChatHome();
                        chatHome.setSenderId(senderId);
                        chatHome.setSenderName(name);  // Lấy tên người gửi
                        chatHome.setAvatarUrl(avatar); // Lấy ảnh đại diện của người gửi

                        // Lấy thông tin món ăn từ Firebase
                        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages");
                        messagesRef.child(senderId).orderByChild("receiverId").equalTo(currentUserId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            // Lấy thông tin món ăn từ tin nhắn
                                            String recipeId = dataSnapshot.child("recipeId").getValue(String.class);
                                            String recipeName = dataSnapshot.child("recipeName").getValue(String.class);
                                            String recipeImage = dataSnapshot.child("recipeImage").getValue(String.class);

                                            chatHome.setRecipeId(recipeId);
                                            chatHome.setRecipeName(recipeName);
                                            chatHome.setRecipeImage(recipeImage);

                                            chatHomes.add(chatHome);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), "Error loading messages.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Failed to load sender info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

