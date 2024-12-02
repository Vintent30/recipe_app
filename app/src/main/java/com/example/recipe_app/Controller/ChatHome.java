package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.ChatHomeAdapter;
import com.example.recipe_app.Model.ChatList;
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
    private List<ChatList> chatLists;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_home);

        recyclerView = findViewById(R.id.chathome_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatLists = new ArrayList<>();
        adapter = new ChatHomeAdapter(chatLists, chatList -> {
            Intent intent = new Intent(ChatHome.this, Chat.class);
            intent.putExtra("receiverId", chatList.getSenderId());
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

        findViewById(R.id.backButtonHome).setOnClickListener(v -> finish());
    }

    private void loadMessages() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages");

        messagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Set<String> chatKeys = new HashSet<>();

                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    String chatKey = chatSnapshot.getKey();

                    if (chatKey != null && (chatKey.contains(currentUserId))) {
                        chatKeys.add(chatKey);
                    }
                }

                // Now, load each chat's details based on chatKey
                fetchChatsInfo(chatKeys);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Unable to load messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchChatsInfo(Set<String> chatKeys) {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages");
        Set<String> processedSenders = new HashSet<>(); // Set để kiểm tra trùng lặp senderId

        for (String chatKey : chatKeys) {
            DatabaseReference chatRef = messagesRef.child(chatKey).child("messages");

            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String lastMessage = null;
                    long lastMessageTimestamp = 0;
                    String lastSenderId = null;

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        // Lấy thông tin tin nhắn từ messageSnapshot
                        String senderId = messageSnapshot.child("senderId").getValue(String.class);
                        String receiverId = messageSnapshot.child("receiverId").getValue(String.class);
                        String messageText = messageSnapshot.child("messageText").getValue(String.class);
                        long timestamp = messageSnapshot.child("timestamp").getValue(Long.class);
                        String recipeId = messageSnapshot.child("recipeId").getValue(String.class);
                        String recipeImage = messageSnapshot.child("recipeImage").getValue(String.class);
                        String recipeName = messageSnapshot.child("recipeName").getValue(String.class);

                        // Cập nhật tin nhắn cuối cùng và thời gian tin nhắn
                        if (timestamp > lastMessageTimestamp) {
                            lastMessage = messageText;
                            lastMessageTimestamp = timestamp;
                            lastSenderId = senderId;
                        }

                        // Chỉ thêm vào danh sách nếu người nhận là currentUserId và người gửi không phải currentUserId
                        if (receiverId != null && receiverId.equals(currentUserId)
                                && senderId != null && !senderId.equals(currentUserId)
                                && !processedSenders.contains(senderId)) {

                            processedSenders.add(senderId); // Đánh dấu senderId đã xử lý

                            ChatList chatList = new ChatList(
                                    senderId,       // senderId
                                    receiverId,     // receiverId
                                    lastMessage,    // messageText (lastMessage)
                                    lastMessageTimestamp, // timestamp (lastMessageTimestamp)
                                    recipeId,       // recipeId
                                    recipeName,     // recipeName
                                    recipeImage,    // recipeImage
                                    lastSenderId,   // authorId (lastSenderId)
                                    null,           // avatarUrl (sẽ được cập nhật sau)
                                    null,            // senderName (sẽ được cập nhật sau)
                                    lastMessage,    // lastMessage (new field)
                                    lastMessageTimestamp // lastMessageTimestamp (new field)
                            );

                            // Lấy thông tin người gửi (senderId)
                            fetchUserInfo(senderId, chatList);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Failed to load chat info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }






    private void fetchUserInfo(String userId, ChatList chatList) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String avatar = snapshot.child("avatar").getValue(String.class);

                    chatList.setSenderName(name);
                    chatList.setAvatarUrl(avatar);

                    chatLists.add(chatList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load user info: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
