package com.example.recipe_app.Controller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Adapter.PlanAdapter;
import com.example.recipe_app.Model.Plan;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Planer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAddPlan;
    private TextView textDateMonth;
    private ImageView backPlan, ivCalendarNext, ivCalendarPrevious;

    private List<Plan> planList;
    private PlanAdapter adapter;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner);

        // Ánh xạ các thành phần
        recyclerView = findViewById(R.id.recyclerView);
        btnAddPlan = findViewById(R.id.btn_createRe_Plan);
        textDateMonth = findViewById(R.id.text_date_month);
        backPlan = findViewById(R.id.back_Plan);
        ivCalendarNext = findViewById(R.id.iv_calendar_next);
        ivCalendarPrevious = findViewById(R.id.iv_calendar_previous);

        // Khởi tạo các biến
        auth = FirebaseAuth.getInstance();
        calendar = Calendar.getInstance();
        planList = new ArrayList<>();
        adapter = new PlanAdapter(this, planList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Plans");

        // Hiển thị danh sách kế hoạch cho ngày hiện tại
        updateDateText();
        loadPlansByDate(getSelectedDate());

        // Bắt sự kiện thêm kế hoạch
        btnAddPlan.setOnClickListener(v -> addNewPlan());

        // Bắt sự kiện chọn ngày
        textDateMonth.setOnClickListener(v -> showDatePicker());

        // Bắt sự kiện nút quay lại
        backPlan.setOnClickListener(v -> onBackPressed());

        // Bắt sự kiện nút tiếp theo
        ivCalendarNext.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Chuyển sang ngày kế tiếp
            updateDateText();
            loadPlansByDate(getSelectedDate());
        });

        // Bắt sự kiện nút trước đó
        ivCalendarPrevious.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Quay lại ngày trước đó
            updateDateText();
            loadPlansByDate(getSelectedDate());
        });
    }

    private void updateDateText() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
        textDateMonth.setText(currentDate);
    }

    private String getSelectedDate() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
    }

    private void loadPlansByDate(String date) {
        String userId = auth.getCurrentUser().getUid();

        databaseReference.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                planList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Plan plan = dataSnapshot.getValue(Plan.class);
                    if (plan != null && plan.getDate().equals(date)) {
                        planList.add(plan);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Planer.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewPlan() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_plan);

        EditText etRecipeName = dialog.findViewById(R.id.etRecipeName);
        EditText etDescription = dialog.findViewById(R.id.etDescription);
        Button btnSavePlan = dialog.findViewById(R.id.btnSavePlan);

        btnSavePlan.setOnClickListener(v -> {
            String recipeName = etRecipeName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String selectedDate = getSelectedDate();

            if (recipeName.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = auth.getCurrentUser().getUid();
            String id = databaseReference.push().getKey();
            Plan plan = new Plan(id, userId, selectedDate, recipeName, description);

            databaseReference.child(id).setValue(plan).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Thêm kế hoạch thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadPlansByDate(selectedDate);
            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Lỗi khi thêm kế hoạch!", Toast.LENGTH_SHORT).show()
            );
        });

        dialog.show();
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            calendar.set(year1, month1, dayOfMonth);
            updateDateText();
            loadPlansByDate(getSelectedDate());
        }, year, month, day);
        datePickerDialog.show();
    }
}
