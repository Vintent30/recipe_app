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
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String recipeId); // Callback khi item được click
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public DetailAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.name.setText(recipe.getName());
        holder.calory.setText("Calo: " + recipe.getCalories());
        holder.category.setText("Danh mục: " + recipe.getCategory());
        holder.like.setText("Lượt thích: " + recipe.getLike());

        Glide.with(context)
                .load(recipe.getImage())
                .into(holder.Image);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(recipe.getRecipeId()); // Gửi recipeId về qua listener
            }
        });
    }

    public void setData(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView name, calory, category, like;
        ImageView Image;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_title);
            calory = itemView.findViewById(R.id.tv_calo);
            category = itemView.findViewById(R.id.tv_category);
            like = itemView.findViewById(R.id.tv_like);
            Image = itemView.findViewById(R.id.food_image);
        }
    }
}
