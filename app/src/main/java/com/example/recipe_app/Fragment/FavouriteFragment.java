package com.example.recipe_app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.FavouriteAdapter;
import com.example.recipe_app.Model.Item;
import com.example.recipe_app.R;


import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        // Initialize the Spinner
        Spinner spinner = view.findViewById(R.id.spinner);
        List<String> options = new ArrayList<>();
        options.add("Sắp xếp"); // Placeholder option
        options.add("Bánh mì");
        options.add("Bánh chuối");
        options.add("Chả giò");
        options.add("Gà sốt thái");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle selection
                if (position == 0) {
                    // Do nothing
                } else {
                    String selectedOption = options.get(position);
                    // Handle the selected option
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected
            }
        });

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        // Sample data for RecyclerView
        List<Item> items = new ArrayList<>();
        items.add(new Item(R.drawable.image_fv1, "Bánh mì", "150-300 calo", R.drawable.favorite));
        items.add(new Item(R.drawable.image_fv2, "Bánh chuối", "200-300 calo", R.drawable.favorite));
        items.add(new Item(R.drawable.image_fv3, "Chả giò", "100-150 calo", R.drawable.favorite));
        items.add(new Item(R.drawable.image_fv4, "Gà sốt thái", "250-400 calo", R.drawable.favorite));
        items.add(new Item(R.drawable.image_fv5, "Bánh tráng", "200-350 calo", R.drawable.favorite));  // Item 5
        items.add(new Item(R.drawable.image_fv6, "Pizza", "300-400 calo", R.drawable.favorite));  // Item 6
        items.add(new Item(R.drawable.image_fv7, "Bánh xèo", "150-250 calo", R.drawable.favorite));  // Item 7
        items.add(new Item(R.drawable.image_fv8, "Hamburger", "350-500 calo", R.drawable.favorite));  // Item 8

        // Set up adapter for RecyclerView
        favouriteAdapter = new FavouriteAdapter(getContext(), items);
        recyclerView.setAdapter(favouriteAdapter);

        return view;
    }
}
