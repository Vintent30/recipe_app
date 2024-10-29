package com.example.recipe_app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.recipe_app.Adapter.CategoryHomeAdapter;
import com.example.recipe_app.Adapter.FoodHomeAdapter;
import com.example.recipe_app.Controller.Detail_suggest;
import com.example.recipe_app.Controller.DishRecipe;
import com.example.recipe_app.Model.FoodHome;
import com.example.recipe_app.Model.categoryHome;
import com.example.recipe_app.R;
import com.example.recipe_app.Controller.chat_community;
import com.example.recipe_app.Controller.Planer;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements FoodHomeAdapter.OnFoodClickListener,CategoryHomeAdapter.OnCategoryForwardClickListener{

    ImageView imageView,imgCalen;
    ImageView imgForward;
    Button button;
    private RecyclerView rcvCategory;
    private CategoryHomeAdapter categoryHomeAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView and Adapter
        rcvCategory = view.findViewById(R.id.rcv_category);
        categoryHomeAdapter = new CategoryHomeAdapter(getContext(), this, this);;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);

        categoryHomeAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryHomeAdapter);


        imageView = view.findViewById(R.id.Icon_calendar);
        imageView.setOnClickListener(v -> startActivity(new Intent(getActivity(), Planer.class)));

        button = view.findViewById(R.id.chat_community);
        button.setOnClickListener(v -> startActivity(new Intent(getActivity(), chat_community.class)));

        return view;

    }

    private List<categoryHome> getListCategory() {
        List<categoryHome> listCategory = new ArrayList<>();

        // Danh sách món ăn cho "Đề xuất cho bạn"
        List<FoodHome> listRecommended = new ArrayList<>();
        listRecommended.add(new FoodHome(R.drawable.img1_home, "food 1", "fff"));
        listRecommended.add(new FoodHome(R.drawable.img2_home, "food 2", "fff"));
        listRecommended.add(new FoodHome(R.drawable.img3_home, "food 3", "fff"));
        listRecommended.add(new FoodHome(R.drawable.img4_home, "food 4", "fff"));
        listRecommended.add(new FoodHome(R.drawable.img5_home, "food 5", "fff"));

        // Danh sách món ăn cho "Công thức phổ biến"
        List<FoodHome> listPopularRecipes = new ArrayList<>();
        listPopularRecipes.add(new FoodHome(R.drawable.img6_home, "food 6", "fff"));
        listPopularRecipes.add(new FoodHome(R.drawable.img7_home, "food 7", "fff"));
        listPopularRecipes.add(new FoodHome(R.drawable.img8_home, "food 8", "fff"));
        listPopularRecipes.add(new FoodHome(R.drawable.img9_home, "food 9", "fff"));
        listPopularRecipes.add(new FoodHome(R.drawable.img10_home, "food 10", "fff"));

        // Danh sách món ăn cho "Có thể bạn sẽ thích"
        List<FoodHome> listYouMightLike = new ArrayList<>();
        listYouMightLike.add(new FoodHome(R.drawable.img8_home, "food 10", "fff"));
        listYouMightLike.add(new FoodHome(R.drawable.img9_home, "food 11", "fff"));
        listYouMightLike.add(new FoodHome(R.drawable.img10_home, "food 12", "fff"));
        listYouMightLike.add(new FoodHome(R.drawable.img11_home, "food 13", "fff"));
        listYouMightLike.add(new FoodHome(R.drawable.img10_home, "food 14", "fff"));

        // Thêm các danh mục vào danh sách
        listCategory.add(new categoryHome("Đề xuất cho bạn", R.drawable.baseline_arrow_forward_24, listRecommended));
        listCategory.add(new categoryHome("Công thức phổ biến", R.drawable.baseline_arrow_forward_24, listPopularRecipes));
        listCategory.add(new categoryHome("Có thể bạn sẽ thích", R.drawable.baseline_arrow_forward_24, listYouMightLike));

        return listCategory;
    }
    @Override
    public void onFoodClick(FoodHome food) {
        Intent intent = new Intent(getActivity(), DishRecipe.class);
        intent.putExtra("food_title", food.getTitle());
        intent.putExtra("food_description", food.getSave());
        startActivity(intent);
    }
    @Override
    public void onCategoryForwardClick(String categoryTitle) {
        Intent intent = new Intent(getActivity(), Detail_suggest.class);
        intent.putExtra("category_title", categoryTitle);
        startActivity(intent);
    }

}