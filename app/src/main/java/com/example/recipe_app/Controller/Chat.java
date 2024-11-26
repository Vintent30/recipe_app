package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Adapter.ChatAdapter;
import com.example.recipe_app.Model.ChatMessage;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private String recipeId, recipeName, recipeImage, authorId;
    private String currentUserId;
    private ImageView recipeImageView;
    private TextView recipeNameTextView, recipeAuthorTextView;
    private EditText messageInput;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private Button sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        ImageView imageView = findViewById(R.id.backButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    // Quay lại Fragment trước đó và load lại trạng thái
                    getSupportFragmentManager().popBackStack();
                } else {
                    // Nếu không có fragment nào trong backstack, kết thúc Activity hiện tại
                    finish();
                }
            }
        });

        // Liên kết với các view
        recipeImageView = findViewById(R.id.recipeImage);
        recipeNameTextView = findViewById(R.id.recipeName);
        recipeAuthorTextView = findViewById(R.id.recipeAuthor);
        messageInput = findViewById(R.id.editTextMessage);
        chatRecyclerView = findViewById(R.id.recyclerViewChat);


        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        recipeName = intent.getStringExtra("name");
        recipeImage = intent.getStringExtra("imageUrl");
        authorId = intent.getStringExtra("authorId");
        recipeId = intent.getStringExtra("recipeId");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Hiển thị thông tin công thức
        recipeNameTextView.setText(recipeName);
        recipeAuthorTextView.setText(authorId); // Ban đầu hiển thị authorId, sau đó cập nhật sau khi lấy tên

    // Lấy tên tác giả từ Firebase Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Accounts").child(authorId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String authorName = snapshot.child("name").getValue(String.class);
                    if (authorName != null) {
                        // Hiển thị tên tác giả
                        recipeAuthorTextView.setText(authorName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Failed to load author information", Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(this).load(recipeImage).into(recipeImageView);

        // Lấy tin nhắn từ Firebase
        loadMessages();

        // Xử lý sự kiện gửi tin nhắn
        findViewById(R.id.buttonSend).setOnClickListener(v -> sendMessage());
    }

    private void loadMessages() {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Messages").child(recipeId);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    if (message != null) {
                        // Kiểm tra nếu tin nhắn thuộc về người dùng hiện tại
                        if (message.getSenderId().equals(currentUserId) || message.getReceiverId().equals(currentUserId)) {
                            chatMessages.add(message);
                        }
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (messageText.isEmpty()) return;

        String messageId = FirebaseDatabase.getInstance().getReference("Messages").child(recipeId).push().getKey();

        // Tạo tin nhắn
        ChatMessage chatMessage = new ChatMessage(
                currentUserId, authorId, messageText, System.currentTimeMillis(),
                recipeId, recipeName, recipeImage, authorId
        );

        // Lưu vào Firebase
        FirebaseDatabase.getInstance().getReference("Messages").child(recipeId).child(messageId)
                .setValue(chatMessage)
                .addOnSuccessListener(aVoid -> {
                    messageInput.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Chat.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });
    }
}
