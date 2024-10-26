package com.example.recipe_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Controller.categoryHome;
import com.example.recipe_app.R;

import java.util.List;

public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.CategoryViewHolder>{

    private Context mContext;
    List<categoryHome> mListCategory;

    public CategoryHomeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<categoryHome> list){
        this.mListCategory = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_category,parent,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        categoryHome category = mListCategory.get(position);
        if(category == null){
            return;
        }
        holder.tvNameCategory.setText(category.getNameCategory());
        holder.imgForward.setImageResource(category.getImgForward());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);
        holder.rcvFood.setLayoutManager(linearLayoutManager);

        FoodHomeAdapter foodHomeAdapter = new FoodHomeAdapter();
        foodHomeAdapter.setData(category.getFoods());
        holder.rcvFood.setAdapter(foodHomeAdapter);
    }

    @Override
    public int getItemCount() {

        if(mListCategory != null ){
            return mListCategory.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameCategory;
        private ImageView imgForward;
        private RecyclerView rcvFood;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameCategory = itemView.findViewById(R.id.tv_name_category);
            imgForward = itemView.findViewById(R.id.arrow_forward_1);
            rcvFood = itemView.findViewById(R.id.rcv_food);

        }
    }
}
