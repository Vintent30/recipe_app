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
    private String currentUserId;  // Khai báo biến currentUserId ở phạm vi lớp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_home);

        recyclerView = findViewById(R.id.chathome_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatHomes = new ArrayList<>();
        adapter = new ChatHomeAdapter(chatHomes, chatHome -> {
            // Open chat interface when clicking a message
            Intent intent = new Intent(ChatHome.this, Chat.class);
            intent.putExtra("receiverId", chatHome.getSenderId());
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

        // Load chat messages for the current user
        loadMessages();
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
                            // Add senderId if the receiver is the current user
                            senderIds.add(chatHome.getSenderId());
                        }
                    }
                }

                // Fetch user information for each sender
                fetchUsersInfo(senderIds);
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

                        // Create a new chatHome object
                        com.example.recipe_app.Model.ChatHome chatHome = new com.example.recipe_app.Model.ChatHome();
                        chatHome.setSenderId(senderId);  // Inherited from ChatMessage
                        chatHome.setReceiverId(currentUserId);  // Inherited from ChatMessage

                        // Set senderName instead of lastMessage
                        chatHome.setSenderName(name); // Set sender's name here
                        chatHome.setLastMessageTimestamp(System.currentTimeMillis()); // Timestamp for the preview
                        chatHome.setAvatarUrl(avatar); // Use avatar as recipe image

                        // Optionally, you can set a recipe ID, name, etc., if necessary
                        chatHome.setRecipeId(""); // Can be set if needed
                        chatHome.setRecipeName(""); // Can be set if needed
                        chatHome.setRecipeImage(""); // Can be set if needed

                        // Add the new chatHome object to the list
                        chatHomes.add(chatHome);

                        // Notify adapter that data has changed
                        adapter.notifyDataSetChanged();
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
