//package com.example.recipe_app.Controller;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.recipe_app.Model.ChatMessage;
//import com.example.recipe_app.R;
//import com.example.recipe_app.Adapter.ChatAdapter;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Chat extends AppCompatActivity {
//    ImageView backButton;
//    RecyclerView recyclerViewChat;
//    EditText editTextMessage;
//    Button buttonSend;
//    List<ChatMessage> chatMessageList;
//    ChatAdapter chatAdapter;
//
//    private String currentUserId = "User1"; // ID người dùng hiện tại
//    private String otherUserId = "User2";  // ID người nhận
//
//    // Firebase Database reference
//    private DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.chat);
//
//        // Firebase initialization
//        databaseReference = FirebaseDatabase.getInstance().getReference("Chats/messages");
//
//        // UI elements
//
//        recyclerViewChat = findViewById(R.id.recyclerViewChat);
//        editTextMessage = findViewById(R.id.editTextMessage);
//        buttonSend = findViewById(R.id.buttonSend);
//
//        chatMessageList = new ArrayList<>();
//        chatAdapter = new ChatAdapter(chatMessageList, currentUserId);
//
//        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewChat.setAdapter(chatAdapter);
//
//        backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                    getSupportFragmentManager().popBackStack();
//                } else {
//                    finish();
//                }
//            }
//        });
//
//        buttonSend.setOnClickListener(v -> {
//            String messageContent = editTextMessage.getText().toString();
//            if (!messageContent.isEmpty()) {
//                sendMessage(messageContent);  // Gửi tin nhắn và lưu vào Firebase
//                editTextMessage.setText("");  // Xóa nội dung nhập vào
//            }
//        });
//    }
//
//    private void sendMessage(String messageContent) {
//        // Tạo đối tượng ChatMessage với chỉ senderId và receiverId
//        ChatMessage newMessage = new ChatMessage(currentUserId, otherUserId);
//
//        // Lưu tin nhắn vào Firebase
//        String messageId = databaseReference.push().getKey();  // Tạo ID cho tin nhắn
//        if (messageId != null) {
//            databaseReference.child(messageId).setValue(newMessage);  // Lưu tin nhắn vào Firebase
//        }
//
//        // Thêm tin nhắn vào danh sách để hiển thị
//        chatMessageList.add(newMessage);
//        chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
//        recyclerViewChat.scrollToPosition(chatMessageList.size() - 1);  // Cuộn đến tin nhắn mới nhất
//    }
//}
