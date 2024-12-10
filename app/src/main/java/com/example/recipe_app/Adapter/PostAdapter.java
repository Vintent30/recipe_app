package com.example.recipe_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Controller.Comment;
import com.example.recipe_app.Model.Post;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // Load the post image using Glide
        Glide.with(context)
                .load(post.getImage())
                .into(holder.postImage);

        String userId = post.getUserId();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts");
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String senderName = snapshot.child("name").getValue(String.class);
                    String senderAvatar = snapshot.child("avatar").getValue(String.class);

                    holder.postUserName.setText(senderName != null ? senderName : "Unknown Sender");

                    Glide.with(holder.itemView.getContext())
                            .load(senderAvatar != null ? senderAvatar : R.drawable.avatar_macdinh) // Default image if avatar is null
                            .into(holder.posterImage);
                } else {
                    holder.postUserName.setText("Unknown Sender");
                    Glide.with(holder.itemView.getContext())
                            .load(R.drawable.avatar_macdinh) // Default image
                            .into(holder.posterImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.itemView.getContext(), "Failed to load sender info", Toast.LENGTH_SHORT).show();
            }
        });

        // Set content, user name, like, and comment count
        holder.postContent.setText(post.getContent());


        // Display like and comment counts
        holder.likeCount.setText(String.valueOf(post.getLike()));
        // Fetch and display comment count

        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("Comments");
        commentRef.child(post.getPostId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Đếm số lượng commentId trong postId
                    int commentCount = (int) snapshot.getChildrenCount();
                    holder.commentCount.setText(String.valueOf(commentCount)); // Hiển thị số lượng

                    // Cập nhật số lượng comment vào bảng Post
                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostId());
                    postRef.child("comment").setValue(commentCount); // Cập nhật số lượng comment vào node "comment" trong Post

                } else {
                    holder.commentCount.setText("0"); // Nếu không có comment

                    // Cập nhật số lượng comment là 0 vào bảng Post
                    DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostId());
                    postRef.child("comment").setValue(0); // Cập nhật số lượng comment vào node "comment" trong Post
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to fetch comments count: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        holder.commentIcon.setOnClickListener(v -> {
            // Chuyển sang CommentActivity và gửi postId
            Intent intent = new Intent(context, Comment.class);
            intent.putExtra("postId", post.getPostId()); // Truyền postId sang CommentActivity
            context.startActivity(intent);
        });

        // Get currentUserId from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid(); // Lấy ID người dùng

            // Lắng nghe sự kiện nhấn vào heartIcon
            holder.heartIcon.setOnClickListener(v -> {
                // Lấy bài đăng cần thay đổi like
                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostId());

                // Kiểm tra trạng thái like của bài đăng (Nếu người dùng đã thích)
                postRef.child("likes").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Nếu người dùng đã thích, bỏ thích
                            postRef.child("likes").child(currentUserId).removeValue();  // Xóa like của user
                            postRef.child("like").setValue(post.getLike() - 1); // Giảm số lượng like

                            // Thay đổi hình ảnh heartIcon thành trái tim trắng
                            holder.heartIcon.setImageResource(R.drawable.heart_nomal);
                        } else {
                            // Nếu người dùng chưa thích, thích bài đăng
                            postRef.child("likes").child(currentUserId).setValue(true);  // Thêm like của user
                            postRef.child("like").setValue(post.getLike() + 1); // Tăng số lượng like

                            // Thay đổi hình ảnh heartIcon thành trái tim đỏ
                            holder.heartIcon.setImageResource(R.drawable.heart_red);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to update like status", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        } else {
            // Nếu người dùng chưa đăng nhập, hiển thị thông báo
            Toast.makeText(context, "Please log in to like the post", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage, heartIcon, commentIcon, posterImage;
        TextView postContent, postUserName, likeCount, commentCount;

        public PostViewHolder(View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.postImagecmt);
            postContent = itemView.findViewById(R.id.text_post);
            postUserName = itemView.findViewById(R.id.posterName);
            posterImage = itemView.findViewById(R.id.PosterImage);
            likeCount = itemView.findViewById(R.id.nb_heart1);
            commentCount = itemView.findViewById(R.id.nb_cmt);
            heartIcon = itemView.findViewById(R.id.heart1);
            commentIcon = itemView.findViewById(R.id.cmt);
        }
    }
}
