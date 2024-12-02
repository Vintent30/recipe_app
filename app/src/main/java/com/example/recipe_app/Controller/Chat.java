package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;
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
    private String currentUserId, chatKey;
    private ImageView recipeImageView;
    private TextView recipeNameTextView, recipeAuthorTextView;
    private EditText messageInput;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // Liên kết với các view
        recipeImageView = findViewById(R.id.recipeImage);
        recipeNameTextView = findViewById(R.id.recipeName);
        recipeAuthorTextView = findViewById(R.id.recipeAuthor);
        messageInput = findViewById(R.id.editTextMessage);
        chatRecyclerView = findViewById(R.id.recyclerViewChat);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages,FirebaseAuth.getInstance().getCurrentUser().getUid());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeId");
        recipeName = intent.getStringExtra("recipeName");
        recipeImage = intent.getStringExtra("recipeImage");
        authorId = intent.getStringExtra("authorId");

        // Tạo chatKey (id người gửi + id người nhận)
        if (currentUserId != null && authorId != null) {
            chatKey = currentUserId.compareTo(authorId) > 0 ? currentUserId + "_" + authorId : authorId + "_" + currentUserId;
        } else {
            // Xử lý lỗi nếu currentUserId hoặc authorId là null
            Toast.makeText(this, "User ID or Author ID is null", Toast.LENGTH_SHORT).show();
            finish(); // Hoặc xử lý theo yêu cầu
        }

        // Hiển thị thông tin công thức
        recipeNameTextView.setText(recipeName);
        Glide.with(this).load(recipeImage).into(recipeImageView);

        // Lấy tên tác giả
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Accounts").child(authorId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String authorName = snapshot.child("name").getValue(String.class);
                    recipeAuthorTextView.setText(authorName != null ? authorName : "Unknown");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Failed to load author information", Toast.LENGTH_SHORT).show();
            }
        });

        // Lấy tin nhắn từ Firebase
        loadMessages();

        // Xử lý sự kiện gửi tin nhắn
        findViewById(R.id.buttonSend).setOnClickListener(v -> sendMessage());

        // Xử lý nút quay lại
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void loadMessages() {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Messages").child(chatKey).child("messages");

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

                    if (message != null) {
                        // Kiểm tra nếu người nhận là currentUserId hoặc người gửi là currentUserId
                        if (message.getReceiverId().equals(currentUserId) || message.getSenderId().equals(currentUserId)) {
                            chatMessages.add(message);
                        }
                    }
                }
                // Cập nhật Adapter
                chatAdapter.notifyDataSetChanged();
                // Cuộn đến tin nhắn cuối cùng
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
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

        // Generate a unique message ID
        String messageId = FirebaseDatabase.getInstance().getReference("Messages")
                .child(chatKey)
                .child("messages")
                .push().getKey();

        // Create a ChatMessage object with message details
        ChatMessage chatMessage = new ChatMessage(
                currentUserId, // senderId
                authorId,      // receiverId
                messageText,   // messageText
                System.currentTimeMillis(), // timestamp
                recipeId,      // recipeId
                recipeName,    // recipeName
                recipeImage,   // recipeImage
                authorId       // authorId
        );

        // Get reference to Firebase Database for the chatKey
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Messages").child(chatKey);

        // Save the message in the "messages" child under the chatKey
        chatRef.child("messages").child(messageId).setValue(chatMessage)
                .addOnSuccessListener(aVoid -> {
                    // Clear the input field after successful send
                    messageInput.setText("");
                })
                .addOnFailureListener(e -> {
                    // Handle failure if any
                    Toast.makeText(Chat.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });
    }


}
