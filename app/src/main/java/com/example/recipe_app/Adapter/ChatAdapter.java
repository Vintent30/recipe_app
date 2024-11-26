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

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
//        ChatMessage message = chatMessages.get(position);
//
//        // Hiển thị tin nhắn
//        holder.messageText.setText(message.getMessageText());
//
//        // Hiển thị thông tin nguươi gưi
//        holder.senderName.setText(message.getRecipeName());
//        Glide.with(holder.itemView.getContext()).load(message.getRecipeImage()).into(holder.senderAvatar);
//    }
@Override
public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
    ChatMessage message = chatMessages.get(position);

    // Hiển thị tin nhắn
    holder.messageText.setText(message.getMessageText());

    // Truy vấn Firebase để lấy thông tin của người gửi
    String senderId = message.getSenderId(); // Lấy senderId từ message
    DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("Accounts").child(senderId);
    senderRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                // Lấy tên và avatar người gửi từ Firebase
                String senderName = snapshot.child("name").getValue(String.class);
                String senderAvatar = snapshot.child("avatar").getValue(String.class);

                // Hiển thị tên người gửi
                holder.senderName.setText(senderName);

                // Hiển thị avatar người gửi bằng Glide
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

    // Hiển thị thời gian tin nhắn (nếu có)
    long timestamp = message.getTimestamp();
    String time = new SimpleDateFormat("HH:mm").format(new Date(timestamp));
    holder.timestamp.setText(time);
}
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView messageText, senderName,timestamp;
        ImageView senderAvatar;

        public ChatViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textViewMessage);
            senderName = itemView.findViewById(R.id.tvSenderName);
            timestamp =itemView.findViewById(R.id.tvTimestamp);
            senderAvatar = itemView.findViewById(R.id.imageViewAvatar);
        }
    }
}
