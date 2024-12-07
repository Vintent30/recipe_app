package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Model.Post;
import com.example.recipe_app.R;
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
        holder.postUserName.setText(post.getUserId());  // Fetch user name based on userId (you can query the user from your database)

        // Display like and comment counts
        holder.likeCount.setText(String.valueOf(post.getLike()));
        holder.commentCount.setText(String.valueOf(post.getComment()));
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

            postImage = itemView.findViewById(R.id.postImage);
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
