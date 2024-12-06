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
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    private Context context;
    private List<Recipe> recipeList;

    // Constructor nhận context và danh sách món ăn
    public SearchAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        // Thiết lập tên món ăn
        holder.name.setText(recipe.getName());

        // Hiển thị thông tin calo
        holder.calory.setText("Calo: " + String.valueOf(recipe.getCalories()));

        // Hiển thị danh mục món ăn
        holder.category.setText("Danh mục: " + recipe.getCategory());

        // Hiển thị lượt thích
        holder.like.setText("Like: " + String.valueOf(recipe.getLike()));

        // Lấy URL hình ảnh từ Firebase
        String imageUrl = recipe.getImage();

        // Tải ảnh từ URL vào ImageView sử dụng Glide
        Glide.with(context)
                .load(imageUrl)
                .into(holder.Image);

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

    // Cập nhật danh sách món ăn
    public void setData(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();  // Đảm bảo adapter cập nhật lại RecyclerView
    }

    @Override
    public int getItemCount() {
        if(recipeList != null) {
            return recipeList.size();
        }// Trả về số lượng món ăn trong danh sách
        return  0;
    }

    // Lớp ViewHolder để ánh xạ các View
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView name, calory, category, like;
        ImageView Image;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_title);
            calory = itemView.findViewById(R.id.tv_calo);
            category = itemView.findViewById(R.id.tv_category);
            like = itemView.findViewById(R.id.tv_like);
            Image = itemView.findViewById(R.id.food_image);
        }
    }
}