package com.example.recipe_app.Adapter;

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
import com.example.recipe_app.Model.ChatList;
import com.example.recipe_app.R;
import com.example.recipe_app.Controller.Chat; // Import Activity Chat của bạn
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatHomeAdapter extends RecyclerView.Adapter<ChatHomeAdapter.ChatHomeViewHolder> {
    private List<ChatList> chatLists;
    private OnMessageClickListener onItemClickListener;

    public ChatHomeAdapter(List<ChatList> chatLists, OnMessageClickListener listener) {
        this.chatLists = chatLists;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ChatHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHomeViewHolder holder, int position) {
        ChatList chatList = chatLists.get(position);

        // Set sender name and avatar
        String senderId = chatList.getSenderId();

        // Fetch sender details (name and avatar) from Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts");
        userRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String senderName = snapshot.child("name").getValue(String.class);
                    String senderAvatar = snapshot.child("avatar").getValue(String.class);

                    holder.senderName.setText(senderName != null ? senderName : "Unknown Sender");

                    Glide.with(holder.itemView.getContext())
                            .load(senderAvatar != null ? senderAvatar : R.drawable.hinh) // Default image if avatar is null
                            .into(holder.senderAvatar);
                } else {
                    holder.senderName.setText("Unknown Sender");
                    Glide.with(holder.itemView.getContext())
                            .load(R.drawable.hinh) // Default image
                            .into(holder.senderAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.itemView.getContext(), "Failed to load sender info", Toast.LENGTH_SHORT).show();
            }
        });

        // Set last message and time
        holder.messagePreview.setText(chatList.getLastMessage() != null ? chatList.getLastMessage() : "No message yet");

        // Format and display last message timestamp
        holder.messageTime.setText(formatTimestamp(chatList.getLastMessageTimestamp()));

        // Handle message item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), Chat.class);
            intent.putExtra("messageText", chatList.getMessageText());
            intent.putExtra("senderId", chatList.getSenderId());
            intent.putExtra("receiverId", chatList.getReceiverId());
            intent.putExtra("recipeId", chatList.getRecipeId());
            intent.putExtra("recipeImage", chatList.getRecipeImage());
            intent.putExtra("recipeName", chatList.getRecipeName());
            intent.putExtra("authorId", chatList.getAuthorId());
            intent.putExtra("timestamp", chatList.getTimestamp());
            holder.itemView.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public static class ChatHomeViewHolder extends RecyclerView.ViewHolder {
        TextView senderName, messagePreview, messageTime;
        ImageView senderAvatar;

        public ChatHomeViewHolder(View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.tv_name);
            messagePreview = itemView.findViewById(R.id.tv_message);
            messageTime = itemView.findViewById(R.id.tv_time);
            senderAvatar = itemView.findViewById(R.id.avatar);
        }
    }

    public interface OnMessageClickListener {
        void onMessageClick(ChatList chatList);
    }

    // Hàm định dạng thời gian
    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(date);
    }
}
