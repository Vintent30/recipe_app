package com.example.recipe_app.Controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Adapter.CommentAdapter;
import com.example.recipe_app.Model.CommentMd;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Comment extends AppCompatActivity {
    ImageView imageView,postImage;
    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;

    private CommentAdapter adapter;
    private List<CommentMd> commentList;

    private DatabaseReference commentRef;
    private String postId;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.back_chat);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });

        // Ánh xạ các View
        postImage = findViewById(R.id.postImage);
        recyclerView = findViewById(R.id.comment_rcv);
        editTextMessage = findViewById(R.id.editTextMessagecmt);
        buttonSend = findViewById(R.id.buttonSendcmt);

        // Nhận postId từ Intent
        if (getIntent().hasExtra("postId")) {
            postId = getIntent().getStringExtra("postId");
            loadPostImage(postId);
        } else {
            Toast.makeText(this, "Post ID not found!", Toast.LENGTH_SHORT).show();
            finish(); // Kết thúc nếu không nhận được postId
            return;
        }

        // Cấu hình RecyclerView
        commentList = new ArrayList<>();
        adapter = new CommentAdapter(this, commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Tham chiếu tới nút Comments trên Firebase
        commentRef = FirebaseDatabase.getInstance().getReference("Comments");

        // Tải danh sách bình luận
        loadComments();

        // Xử lý sự kiện gửi bình luận
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    private void loadComments() {
        commentRef.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CommentMd comment = dataSnapshot.getValue(CommentMd.class);
                    if (comment != null) {
                        commentList.add(comment);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Comment.this, "Failed to load comments: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendComment() {
        String commentText = editTextMessage.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Please enter a comment!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to comment!", Toast.LENGTH_SHORT).show();
            return;
        }

        String commenterId = currentUser.getUid();
        String commentId = commentRef.child(postId).push().getKey(); // Lưu commentId dưới postId
        long timestamp = System.currentTimeMillis();
        int likecmt = 0;

        CommentMd comment = new CommentMd(postId, commentId, commenterId, commentText, timestamp, likecmt);

        if (commentId != null) {
            commentRef.child(postId).child(commentId).setValue(comment)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            editTextMessage.setText("");
                            Toast.makeText(Comment.this, "Comment sent!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Comment.this, "Failed to send comment!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void loadPostImage(String postId) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts");
        postRef.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String postImageUrl = snapshot.child("image").getValue(String.class);
                    Glide.with(Comment.this)
                            .load(postImageUrl != null ? postImageUrl : R.drawable.img) // Hiển thị ảnh mặc định nếu null
                            .into(postImage);
                } else {
                    Toast.makeText(Comment.this, "Post not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Comment.this, "Failed to load post image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
