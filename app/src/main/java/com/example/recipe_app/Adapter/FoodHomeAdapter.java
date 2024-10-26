package com.example.recipe_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Controller.FoodHome;
import com.example.recipe_app.R;

import java.util.List;

public class FoodHomeAdapter extends  RecyclerView.Adapter<FoodHomeAdapter.FoodViewHoder>{
    public List<FoodHome> mFoodHome;

    public void setData(List<FoodHome> list){
        this.mFoodHome = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_food, parent,false);


        return new FoodViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHoder holder, int position) {
        FoodHome foodHome = mFoodHome.get(position);
        if(foodHome ==null){
            return;
        }
        holder.imgFood.setImageResource(foodHome.getResourceId());
        holder.tvTitle.setText(foodHome.getTitle());
        holder.tvSave.setText(foodHome.getSave());
    }

    @Override
    public int getItemCount() {
        if(mFoodHome != null) {
            return  mFoodHome.size();
        }
        return 0;
    }

    public class FoodViewHoder extends RecyclerView.ViewHolder {

        private ImageView imgFood;
        private TextView tvTitle;
        private TextView tvSave;
        public FoodViewHoder(@NonNull View itemView) {
            super(itemView);

            imgFood = itemView.findViewById(R.id.imageView1);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSave = itemView.findViewById(R.id.tv_save);
        }
    }
}
