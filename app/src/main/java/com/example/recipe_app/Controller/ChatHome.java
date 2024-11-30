package com.example.recipe_app.Controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.ChatHomeAdapter;
import com.example.recipe_app.Model.ChatMessage;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatHome extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatHomeAdapter adapter;
    private List<ChatMessage> chatMessages;
    private String authorId; // ID người đăng công thức (cần lấy từ Intent hoặc Firebase)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_home);

        recyclerView = findViewById(R.id.chathome_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatMessages = new ArrayList<>();
        adapter = new ChatHomeAdapter(chatMessages, chatMessage -> {
            // Mở giao diện chat khi nhấn vào một mục
            Intent intent = new Intent(ChatHome.this, Chat.class);
            intent.putExtra("receiverId", chatMessage.getSenderId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Giả sử "authorId" là ID người đăng công thức, sẽ lấy tất cả tin nhắn gửi tới người đó
        loadMessages();
    }

    private void loadMessages() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("Messages");

        // Lấy danh sách tin nhắn gửi tới người đăng công thức
        messagesRef.orderByChild("receiverId").equalTo(authorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    ChatMessage chatMessage = messageSnapshot.getValue(ChatMessage.class);
                    chatMessages.add(chatMessage);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }
}