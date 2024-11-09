package com.example.recipe_app.Adapter;
import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Model.Item;
import com.example.recipe_app.R;

import java.util.List;
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private Context context;
    private List<Item> items;

    public FavouriteAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());
        holder.likeIcon.setImageResource(item.getLikeIconResId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView subtitle;
        ImageView likeIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image1);
            title = itemView.findViewById(R.id.itemTitle);
            subtitle = itemView.findViewById(R.id.itemSubtitle);
            likeIcon = itemView.findViewById(R.id.likeIcon);
        }
    }
}
