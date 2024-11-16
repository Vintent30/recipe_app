package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Model.Favourite;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    private Context context;
    private List<Favourite> favouriteList;

    public FavouriteAdapter(Context context, List<Favourite> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Favourite favourite = favouriteList.get(position);

        // Hiển thị hình ảnh
        if (favourite.getImageUrl() != null && !favourite.getImageUrl().isEmpty()) {
            Glide.with(context).load(favourite.getImageUrl()).into(holder.imageView);
        } else {
            holder.imageView.setImageDrawable(null);
        }

        // Hiển thị thông tin
        holder.itemTitle.setText(favourite.getName());
        holder.itemSubtitle.setText(favourite.getCaloriesText());

        // Biểu tượng yêu thích
        holder.likeIcon.setColorFilter(
                ContextCompat.getColor(context, favourite.isFavorite() ? R.color.red : R.color.gray),
                android.graphics.PorterDuff.Mode.SRC_IN
        );

        // Xử lý khi nhấn vào biểu tượng yêu thích
        holder.likeIcon.setOnClickListener(v -> {
            if (!favourite.isFavorite()) {
                addToFavorites(favourite);
                favourite.setFavorite(true);
                notifyItemChanged(position);
            } else {
                removeFromFavorites(favourite);
                favourite.setFavorite(false);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    private void addToFavorites(Favourite favourite) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("favorites").child(userId).child(favourite.getRecipeId()).setValue(favourite);
        Toast.makeText(context, "Thêm vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites(Favourite favourite) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("favorites").child(userId).child(favourite.getRecipeId()).removeValue();
        Toast.makeText(context, "Xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, likeIcon;
        TextView itemTitle, itemSubtitle;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            likeIcon = itemView.findViewById(R.id.likeIcon);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemSubtitle = itemView.findViewById(R.id.itemSubtitle);
        }
    }
}
