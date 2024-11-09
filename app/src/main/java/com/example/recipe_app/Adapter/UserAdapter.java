package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Model.Account;
import com.example.recipe_app.R;

import java.util.List;

public class UserAdapter{ //extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//    private Context context;
//    private List<Account> userList;
//
//    public UserAdapter(Context context, List<Account> userList) {
//        this.context = context;
//        this.userList = userList;
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        Account user = userList.get(position);
//        holder.userName.setText(user.getName());
//
//        // Use Glide to load the avatar from URL
//        Glide.with(context)
//                .load(user.getAvatar())
//                .placeholder(R.drawable.icon_intro1) // Replace with your default image resource
//                .into(holder.userAvatar);
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
//    public static class UserViewHolder extends RecyclerView.ViewHolder {
//        ImageView userAvatar;
//        TextView userName;
//
//        public UserViewHolder(View itemView) {
//            super(itemView);
//            userAvatar = itemView.findViewById(R.id.imageView2);
//            userName = itemView.findViewById(R.id.userName);
//        }
//    }
}
