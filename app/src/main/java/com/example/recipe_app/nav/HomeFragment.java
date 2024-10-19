package com.example.recipe_app.nav;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.recipe_app.Detail_suggest;
import com.example.recipe_app.DishRecipe;
import com.example.recipe_app.R;
import com.example.recipe_app.chat_community;
import com.example.recipe_app.Planer;


public class HomeFragment extends Fragment {
    ImageView imageView,imgCalen;
    ImageView imageView1;
    Button button;
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
        imageView1 = view.findViewById(R.id.arrow_forward_1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Điều hướng sang Setting activity
                startActivity(new Intent(getActivity(), Detail_suggest.class));
            }
        });
        button = view.findViewById(R.id.chat_community);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Điều hướng sang Setting activity
                startActivity(new Intent(getActivity(), chat_community.class));
            }
        });
        imgCalen = view.findViewById(R.id.Icon_calendar);
        imgCalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Điều hướng sang Setting activity
                startActivity(new Intent(getActivity(), Planer.class));
            }
        });
        return view;
    }

}