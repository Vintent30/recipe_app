package com.example.recipe_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Model.ChatMessage;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;
    private String currentUserId;

    public ChatAdapter(List<ChatMessage> chatMessages, String currentUserId) {
        this.chatMessages = chatMessages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Depending on whether it's a sent or received message, inflate the correct layout
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false); // Right for sender
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false); // Left for receiver
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);

        // Display message text
        holder.messageText.setText(message.getMessageText());

        // Query Firebase to get sender info
        String senderId = message.getSenderId();
        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("Accounts").child(senderId);
        senderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get sender's name and avatar from Firebase
                    String senderName = snapshot.child("name").getValue(String.class);
                    String senderAvatar = snapshot.child("avatar").getValue(String.class);

                    // Set sender name
                    holder.senderName.setText(senderName);

                    // Load sender's avatar using Glide
                    Glide.with(holder.itemView.getContext())
                            .load(senderAvatar)
                            .into(holder.senderAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.itemView.getContext(), "Failed to load sender info", Toast.LENGTH_SHORT).show();
            }
        });

        // Display message timestamp
        long timestamp = message.getTimestamp();
        String time = new SimpleDateFormat("HH:mm").format(new Date(timestamp));
        holder.timestamp.setText(time);

        // Set message alignment based on the sender
        if (message.getSenderId().equals(currentUserId)) {
            // Sender's message (align to the right)
            holder.messageText.setBackgroundResource(R.drawable.bg_message_sent);
            holder.messageText.setGravity(View.TEXT_ALIGNMENT_TEXT_END); // Align right
            holder.senderName.setVisibility(View.GONE);
            holder.senderAvatar.setVisibility(View.GONE);
        } else {
            // Receiver's message (align to the left)
            holder.messageText.setBackgroundResource(R.drawable.bg_message_received);
            holder.messageText.setGravity(View.TEXT_ALIGNMENT_TEXT_START); // Align left
            holder.senderName.setVisibility(View.VISIBLE);
            holder.senderAvatar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Check if the message is sent by the current user
        if (chatMessages.get(position).getSenderId().equals(currentUserId)) {
            return 1; // Right layout for sender's message
        } else {
            return 0; // Left layout for receiver's message
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView messageText, senderName, timestamp;
        ImageView senderAvatar;

        public ChatViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textViewMessage);
            senderName = itemView.findViewById(R.id.tvSenderName);
            timestamp = itemView.findViewById(R.id.tvTimestamp);
            senderAvatar = itemView.findViewById(R.id.imageViewAvatar);
        }
    }
}