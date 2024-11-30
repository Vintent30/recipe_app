package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context context;
    private final List<Recipe> recipeList;
    private final OnRecipeClickListener listener; // Listener for click events

    public interface OnRecipeClickListener {
        void onFoodClick(Recipe recipe);
    }

    public RecipeAdapter(Context context, List<Recipe> recipes, OnRecipeClickListener listener) {
        this.context = context;
        this.recipeList = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        // Set recipe data to UI components
        holder.recipeName.setText(recipe.getName());
        holder.recipeCategory.setText(recipe.getCategory());

        // Load image using Picasso or set default image if null
        if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
            Picasso.get()
                    .load(recipe.getImage())
                    .placeholder(R.drawable.image_fv3) // Placeholder image
                    .into(holder.recipeImage);
        } else {
            holder.recipeImage.setImageResource(R.drawable.image_fv3); // Default image
        }

        // Set click listener for the entire item view
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFoodClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeName, recipeCategory;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize UI components
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeCategory = itemView.findViewById(R.id.recipe_category);
        }
    }
}
