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
import com.example.recipe_app.Model.ChatHome;
import com.example.recipe_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatHomeAdapter extends RecyclerView.Adapter<ChatHomeAdapter.ChatHomeViewHolder> {
    private List<ChatHome> chatHomes;
    private OnMessageClickListener onItemClickListener;

    public ChatHomeAdapter(List<ChatHome> chatHomes, OnMessageClickListener listener) {
        this.chatHomes = chatHomes;
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
        ChatHome chatHome = chatHomes.get(position);

        // Lấy thông tin người gửi
        String senderId = chatHome.getSenderId(); // senderId được lưu trong ChatHome

        // Cập nhật tên người gửi, avatar và preview tin nhắn
        holder.messagePreview.setText(chatHome.getLastMessage());
        holder.messageTime.setText(formatTimestamp(chatHome.getLastMessageTimestamp()));

        // Truy vấn Firebase để lấy thông tin người gửi (tên và avatar)
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts");

        // Truy vấn người gửi theo userId
        userRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String senderName = snapshot.child("name").getValue(String.class);
                    String senderAvatar = snapshot.child("avatar").getValue(String.class);

                    // Cập nhật tên người gửi vào view
                    holder.senderName.setText(senderName != null ? senderName : "Unknown Sender");

                    // Cập nhật avatar người gửi
                    Glide.with(holder.itemView.getContext())
                            .load(senderAvatar != null ? senderAvatar : R.drawable.hinh) // Sử dụng ảnh mặc định nếu không có avatar
                            .into(holder.senderAvatar);
                } else {
                    holder.senderName.setText("Unknown Sender");
                    Glide.with(holder.itemView.getContext())
                            .load(R.drawable.hinh) // Ảnh mặc định
                            .into(holder.senderAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.itemView.getContext(), "Failed to load sender info", Toast.LENGTH_SHORT).show();
            }
        });


        // Xử lý sự kiện click vào tin nhắn
        holder.itemView.setOnClickListener(v -> onItemClickListener.onMessageClick(chatHome));
    }

    @Override
    public int getItemCount() {
        return chatHomes.size();
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
        void onMessageClick(ChatHome chatHome);
    }

    // Hàm định dạng thời gian (chỉ là ví dụ)
    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(date);
    }
}
