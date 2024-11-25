package com.example.recipe_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.R;
import com.example.recipe_app.Model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> chatMessages; // Danh sách tin nhắn
    private final String currentUserId; // ID của người dùng hiện tại

    public ChatAdapter(List<ChatMessage> chatMessages, String currentUserId) {
        this.chatMessages = chatMessages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);

        // Kiểm tra nếu tin nhắn là của người dùng hiện tại
        if (message.getSenderId().equals(currentUserId)) {
            // Nếu người gửi là người dùng hiện tại, hiển thị tin nhắn đã gửi
            holder.textViewMessage.setBackgroundResource(R.drawable.bg_message_sent);
            holder.textViewMessage.setText("Message from: " + message.getSenderId()); // Hiển thị thông tin người gửi
            holder.textViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END); // Đưa tin nhắn sang phải
        } else {
            // Nếu người gửi là người nhận, hiển thị tin nhắn đã nhận
            holder.textViewMessage.setBackgroundResource(R.drawable.bg_message_received);
            holder.textViewMessage.setText("Message from: " + message.getSenderId()); // Hiển thị thông tin người gửi
            holder.textViewMessage.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START); // Đưa tin nhắn sang trái
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size(); // Trả về số lượng tin nhắn
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
