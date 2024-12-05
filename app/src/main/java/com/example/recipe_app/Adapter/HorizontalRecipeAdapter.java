package com.example.recipe_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe_app.Controller.DishRecipe;
import com.example.recipe_app.Model.Detail;
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;

import java.util.List;

public class HorizontalRecipeAdapter extends RecyclerView.Adapter<HorizontalRecipeAdapter.ViewHolder> {

    private Context context;
    private List<Detail> detailList;
    private List<Recipe> recipeList;

    // Constructor
    public HorizontalRecipeAdapter(Context context, List<Detail> detailList, List<Recipe> recipeList) {
        this.context = context;
        this.detailList = detailList;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Xử lý dữ liệu từ danh sách tương ứng
        if (position < detailList.size()) {
            // Lấy dữ liệu từ detailList
            Detail detail = detailList.get(position);
            Recipe recipe = recipeList.get(position);

            holder.recipeName.setText(detail.getName());
            holder.recipeCalories.setText(context.getString(R.string.calories_format, detail.getCalories()));

            // Tải ảnh từ Detail
            Glide.with(context)
                    .load(detail.getImage())
                    .error(R.drawable.img1_home)
                    .into(holder.recipeImage);

            // Sự kiện click
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DishRecipe.class);
                intent.putExtra("recipeId", recipe.getRecipeId());
                intent.putExtra("food_title", recipe.getName());
                intent.putExtra("food_description", recipe.getDescription());
                intent.putExtra("food_image", recipe.getImage());
                context.startActivity(intent);
            });

        } else {
            // Lấy dữ liệu từ recipeList
            int recipePosition = position - detailList.size();
            Recipe recipe = recipeList.get(recipePosition);

            holder.recipeName.setText(recipe.getName());
            holder.recipeCalories.setText(context.getString(R.string.calories_format, recipe.getCalories()));

            // Tải ảnh từ Recipe
            Glide.with(context)
                    .load(recipe.getImage())
                    .error(R.drawable.img1_home)
                    .into(holder.recipeImage);

            // Sự kiện click
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DishRecipe.class);
                intent.putExtra("recipeId", recipe.getRecipeId());
                intent.putExtra("food_title", recipe.getName());
                intent.putExtra("food_description", recipe.getDescription());
                intent.putExtra("food_image", recipe.getImage());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return detailList.size() + recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeCalories;
        ImageView recipeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeCalories = itemView.findViewById(R.id.recipe_calories);
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }
    }
}
