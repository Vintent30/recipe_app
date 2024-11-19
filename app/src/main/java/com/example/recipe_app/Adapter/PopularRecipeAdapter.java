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
import com.example.recipe_app.Model.Detail;
import com.example.recipe_app.R;

import java.util.List;

public class PopularRecipeAdapter extends RecyclerView.Adapter<PopularRecipeAdapter.ViewHolder> {

    private Context context;
    private List<Detail> details;

    public PopularRecipeAdapter(Context context, List<Detail> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popular_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Detail detail = details.get(position);

        holder.recipeName.setText(detail.getName()); // Set recipe name
        holder.recipeCalories.setText(detail.getCalories() + " kcal"); // Set recipe calories

        // Load image using Glide
        Glide.with(context)
                .load(detail.getImage() != null ? detail.getImage() : "https://via.placeholder.com/150")
                .into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeCalories; // Removed recipeSaves
        ImageView recipeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeCalories = itemView.findViewById(R.id.recipe_calories); // For calories
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }
    }
}
