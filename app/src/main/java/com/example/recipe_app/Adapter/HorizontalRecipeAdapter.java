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

public class HorizontalRecipeAdapter extends RecyclerView.Adapter<HorizontalRecipeAdapter.ViewHolder> {

    private Context context;
    private List<Detail> recipeList;

    // Constructor
    public HorizontalRecipeAdapter(Context context, List<Detail> recipeList) {
        this.context = context;
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
        Detail detail = recipeList.get(position);

        // Hiển thị tên món ăn
        holder.recipeName.setText(detail.getName());

        // Hiển thị lượng calories
        holder.recipeCalories.setText(context.getString(R.string.calories_format, detail.getCalories()));

        // Kiểm tra và tải ảnh nếu URL hợp lệ
        String image = detail.getImage();
        if (image != null && !image.isEmpty()) {
            Glide.with(context)
                    .load(image)  // Lấy URL từ Firebase Storage hoặc URL đã lưu trong Realtime Database
                    .error(R.drawable.img1_home)  // Đặt ảnh mặc định nếu lỗi
                    .into(holder.recipeImage);  // Tải ảnh vào ImageView
        } else {
            // Nếu không có URL, có thể đặt ảnh mặc định hoặc ẩn ImageView
            holder.recipeImage.setImageResource(R.drawable.img1_home); // Đặt ảnh mặc định
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeCalories;
        ImageView recipeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Khởi tạo các thành phần trong item layout
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeCalories = itemView.findViewById(R.id.recipe_calories);
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }
    }
}
