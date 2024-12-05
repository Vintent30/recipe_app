package com.example.recipe_app.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe_app.Model.Plan;
import com.example.recipe_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private final Context context;
    private final List<Plan> planList;

    public PlanAdapter(Context context, List<Plan> planList) {
        this.context = context;
        this.planList = planList;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plan_recipe, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);

        holder.tvRecipeName.setText(plan.getRecipeName());
        holder.tvDescription.setText(plan.getDescription());
        holder.tvDate.setText(plan.getDate());

        // Xóa kế hoạch
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa kế hoạch")
                    .setMessage("Bạn có chắc chắn muốn xóa?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("Plans")
                                .child(plan.getId());
                        ref.removeValue().addOnSuccessListener(aVoid ->
                                Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show()
                        ).addOnFailureListener(e ->
                                Toast.makeText(context, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show()
                        );
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        });

        // Chỉnh sửa kế hoạch
        holder.itemView.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_add_plan);

            EditText etRecipeName = dialog.findViewById(R.id.etRecipeName);
            EditText etDescription = dialog.findViewById(R.id.etDescription);
            Button btnSavePlan = dialog.findViewById(R.id.btnSavePlan);

            etRecipeName.setText(plan.getRecipeName());
            etDescription.setText(plan.getDescription());

            btnSavePlan.setOnClickListener(v1 -> {
                String newName = etRecipeName.getText().toString().trim();
                String newDescription = etDescription.getText().toString().trim();

                if (newName.isEmpty()) {
                    Toast.makeText(context, "Tên không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                plan.setRecipeName(newName);
                plan.setDescription(newDescription);

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("Plans")
                        .child(plan.getId());
                ref.setValue(plan).addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }).addOnFailureListener(e ->
                        Toast.makeText(context, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show()
                );
            });

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName, tvDescription, tvDate;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
