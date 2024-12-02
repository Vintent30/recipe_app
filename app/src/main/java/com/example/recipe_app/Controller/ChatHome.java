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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

        // Set để lưu trữ các senderId đã được hiển thị để tránh trùng lặp
        Set<String> processedSenders = new HashSet<>();

        for (String chatKey : chatKeys) {
            DatabaseReference chatRef = messagesRef.child(chatKey).child("messages");

            // Lấy tất cả tin nhắn từ cuộc trò chuyện
            chatRef.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Dùng HashMap để lưu tin nhắn mới nhất từ mỗi người gửi trong cuộc trò chuyện
                    Map<String, ChatList> senderLastMessages = new HashMap<>();

                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        String senderId = messageSnapshot.child("senderId").getValue(String.class);
                        String receiverId = messageSnapshot.child("receiverId").getValue(String.class);
                        String messageText = messageSnapshot.child("messageText").getValue(String.class);
                        long timestamp = messageSnapshot.child("timestamp").getValue(Long.class);
                        String recipeId = messageSnapshot.child("recipeId").getValue(String.class);
                        String recipeImage = messageSnapshot.child("recipeImage").getValue(String.class);
                        String recipeName = messageSnapshot.child("recipeName").getValue(String.class);

                        // Chỉ xử lý tin nhắn nếu receiverId là người dùng hiện tại
                        if (receiverId != null && receiverId.equals(currentUserId)) {
                            // Nếu senderId chưa được xử lý và chưa tồn tại trong processedSenders
                            if (!processedSenders.contains(senderId)) {
                                // Cập nhật tin nhắn mới nhất từ senderId
                                if (!senderLastMessages.containsKey(senderId) || senderLastMessages.get(senderId).getTimestamp() < timestamp) {
                                    ChatList lastMessage = new ChatList(
                                            senderId,       // senderId
                                            receiverId,     // receiverId
                                            messageText,    // messageText
                                            timestamp,      // timestamp
                                            recipeId,       // recipeId
                                            recipeName,     // recipeName
                                            recipeImage,    // recipeImage
                                            senderId,       // authorId (lastSenderId)
                                            null,           // avatarUrl (sẽ được cập nhật sau)
                                            null,           // senderName (sẽ được cập nhật sau)
                                            messageText,    // lastMessage
                                            timestamp       // lastMessageTimestamp
                                    );
                                    senderLastMessages.put(senderId, lastMessage);
                                }
                            }
                        }
                    }

                    // Sau khi xử lý các tin nhắn trong chatKey hiện tại
                    for (ChatList chatList : senderLastMessages.values()) {
                        // Đảm bảo không xử lý lại senderId
                        if (!processedSenders.contains(chatList.getSenderId())) {
                            // Lấy thông tin người gửi và thêm vào danh sách hiển thị
                            fetchUserInfo(chatList.getSenderId(), chatList);
                            processedSenders.add(chatList.getSenderId()); // Đánh dấu senderId đã được xử lý
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
