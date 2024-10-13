package com.example.recipe_app.nav;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.recipe_app.DishRecipe;
import com.example.recipe_app.ListDetail;
import com.example.recipe_app.R;


public class HomeFragment extends Fragment {
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Thêm đoạn code xử lý sự kiện click cho imageView
        imageView = view.findViewById(R.id.imageView1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Điều hướng sang Setting activity
                startActivity(new Intent(getActivity(), DishRecipe.class));
            }
        });

        return view;
    }
}