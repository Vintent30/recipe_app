package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Model.CommentMd;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<CommentMd> commentList;

    public CommentAdapter(Context context, List<CommentMd> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentMd comment = commentList.get(position);
        holder.commentText.setText(comment.getCommentText());
        holder.commentTime.setText(formatTimestamp(comment.getTimestamp()));

        loadCommenterInfo(comment.getCommenterId(), holder);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView commenterImage;
        TextView commenterName, commentText, commentTime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commenterImage = itemView.findViewById(R.id.commenterImage);
            commenterName = itemView.findViewById(R.id.commenterName);
            commentText = itemView.findViewById(R.id.commentText);
            commentTime = itemView.findViewById(R.id.commentTime);
        }
    }
    private void loadCommenterInfo(String commenterId, CommentViewHolder holder) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Accounts");
        usersRef.child(commenterId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String avatarUrl = snapshot.child("avatar").getValue(String.class);

                    // Gán giá trị vào ViewHolder
                    holder.commenterName.setText(name != null ? name : "Unknown");
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        Picasso.get().load(avatarUrl).placeholder(R.drawable.avatar_macdinh).into(holder.commenterImage);
                    } else {
                        holder.commenterImage.setImageResource(R.drawable.avatar_macdinh);
                    }
                } else {
                    holder.commenterName.setText("Unknown");
                    holder.commenterImage.setImageResource(R.drawable.avatar_macdinh);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }
    private String formatTimestamp(long timestamp) {
        // Convert timestamp to readable format
        return android.text.format.DateFormat.format("hh:mm dd/MM/yyyy", timestamp).toString();
    }
}