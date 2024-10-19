package com.example.recipe_app.nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.recipe_app.Create_recipe;
import com.example.recipe_app.Follow;
import com.example.recipe_app.R;
import com.example.recipe_app.RecipeActivity;
import com.example.recipe_app.Setting;


public class UserFragment extends Fragment {
    ImageView imageView, imageView1;
    Button button;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Thêm đoạn code xử lý sự kiện click cho imageView
        imageView = view.findViewById(R.id.setting_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Điều hướng sang Setting activity
                startActivity(new Intent(getActivity(), Setting.class));
            }
        });
        button = view.findViewById(R.id.btn_createRe);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Điều hướng sang Setting activity
                startActivity(new Intent(getActivity(), Create_recipe.class));
            }
        });
        textView = view.findViewById(R.id.follower);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Điều hướng sang Setting activity
                startActivity(new Intent(getActivity(), Follow.class));
            }
        });
        imageView1 = view.findViewById(R.id.user_image_1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Điều hướng sang Setting activity
                startActivity(new Intent(getActivity(), RecipeActivity.class));
            }
        });
        return view;

    }

}