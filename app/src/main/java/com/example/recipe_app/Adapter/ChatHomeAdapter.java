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

public class ChatHomeAdapter extends RecyclerView.Adapter<ChatHomeAdapter.ChatHomeViewHolder>  {
    private List<ChatMessage> chatMessages;
    private OnMessageClickListener onItemClickListener;

    public ChatHomeAdapter(List<ChatMessage> chatMessages, OnMessageClickListener listener) {
        this.chatMessages = chatMessages;
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
        ChatMessage chatMessage = chatMessages.get(position);

        // Lấy senderId từ tin nhắn
        String senderId = chatMessage.getSenderId();  // Giả sử getSenderId() trả về UserId

        // Cập nhật preview và thời gian của tin nhắn
        holder.messagePreview.setText(chatMessage.getMessageText());
        holder.messageTime.setText(formatTimestamp(chatMessage.getTimestamp()));

        // Truy vấn Firebase để lấy thông tin người gửi (tên và avatar)
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts");

        // Truy vấn người gửi theo userId
        userRef.orderByChild("userId").equalTo(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Vòng lặp để lấy thông tin người gửi
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String senderName = dataSnapshot.child("name").getValue(String.class);
                        String senderAvatar = dataSnapshot.child("avatar").getValue(String.class);

                        // Cập nhật tên người gửi vào view
                        if (senderName != null && !senderName.isEmpty()) {
                            holder.senderName.setText(senderName);
                        } else {
                            holder.senderName.setText("Unknown Sender");
                        }

                        // Cập nhật avatar người gửi nếu có
                        if (senderAvatar != null && !senderAvatar.isEmpty()) {
                            Glide.with(holder.itemView.getContext())
                                    .load(senderAvatar)
                                    .into(holder.senderAvatar);
                        } else {
                            // Sử dụng ảnh mặc định nếu không có avatar
                            Glide.with(holder.itemView.getContext())
                                    .load(R.drawable.hinh) // Thay ảnh mặc định của bạn
                                    .into(holder.senderAvatar);
                        }
                    }
                } else {
                    // Nếu không tìm thấy dữ liệu người gửi trong bảng Accounts
                    holder.senderName.setText("Unknown Sender");
                    Glide.with(holder.itemView.getContext())
                            .load(R.drawable.hinh) // Thay ảnh mặc định của bạn
                            .into(holder.senderAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi trong truy vấn Firebase
                Toast.makeText(holder.itemView.getContext(), "Failed to load sender info", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện click vào tin nhắn
        holder.itemView.setOnClickListener(v -> onItemClickListener.onMessageClick(chatMessage));
    }




    @Override
    public int getItemCount() {
        return chatMessages.size();
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
        void onMessageClick(ChatMessage chatMessage);
    }

    // Hàm định dạng thời gian (chỉ là ví dụ)
    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(date);
    }
}