package com.example.recipe_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;


public class Favourite extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite);

        // Khởi tạo AutoCompleteTextView
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        // Tạo ArrayAdapter sử dụng string array từ resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.combo_items, android.R.layout.simple_dropdown_item_1line);

        // Gắn Adapter vào AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter);

        // Thiết lập số lượng item hiển thị
        autoCompleteTextView.setThreshold(1);
    }
}
