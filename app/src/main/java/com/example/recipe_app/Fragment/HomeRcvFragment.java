package com.example.recipe_app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.CategoryHomeAdapter;
import com.example.recipe_app.Controller.FoodHome;
import com.example.recipe_app.Controller.categoryHome;
import com.example.recipe_app.R;

import java.util.ArrayList;
import java.util.List;

public class HomeRcvFragment extends Fragment {

    private RecyclerView rcvCategory;
    private CategoryHomeAdapter categoryHomeAdapter;

    public HomeRcvFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home, container, false);

        // Initialize RecyclerView and Adapter
        rcvCategory = view.findViewById(R.id.rcv_category);
        categoryHomeAdapter = new CategoryHomeAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);

        categoryHomeAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryHomeAdapter);

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
        listCategory.add(new categoryHome("Có thể bạn sẽ thích", R.drawable.baseline_arrow_forward_24, listYouMightLike));

        return listCategory;
    }
}
