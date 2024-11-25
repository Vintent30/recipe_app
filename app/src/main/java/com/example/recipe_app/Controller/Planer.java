package com.example.recipe_app.Controller;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.recipe_app.Adapter.ViewPagerAdaperPlan;
import com.example.recipe_app.R;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Planer extends AppCompatActivity {
    ImageView imageView;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView textDateMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner);

        // Thiết lập nút back
        imageView = findViewById(R.id.back_Plan);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
            }
        });

        // Thiết lập TabLayout và ViewPager
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        ViewPagerAdaperPlan viewPagerAdapter = new ViewPagerAdaperPlan(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        // Kết nối TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Xử lý chọn ngày từ text_date_month
        textDateMonth = findViewById(R.id.text_date_month);
        textDateMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(textDateMonth);
            }
        });
    }

    /**
     * Hiển thị DatePickerDialog để chọn ngày
     */
    private void showDatePicker(TextView textView) {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Planer.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Sử dụng SimpleDateFormat với Locale tiếng Việt
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("vi"));

                    // Định dạng ngày và cập nhật TextView
                    textView.setText(dateFormat.format(selectedDate.getTime()));

                    // Xử lý logic sau khi chọn ngày (nếu cần)
                    // Ví dụ: cập nhật dữ liệu cho RecyclerView dựa vào ngày đã chọn
                },
                year,
                month,
                day
        );

        // Hiển thị dialog
        datePickerDialog.show();
    }
}
