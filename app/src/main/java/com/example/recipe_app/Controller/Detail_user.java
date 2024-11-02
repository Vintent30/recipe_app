package com.example.recipe_app.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.Helper.CloudinaryManager;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Detail_user extends AppCompatActivity {
    private EditText editTextPhone, editTextName, editTextEmail, editTextPassword;
    private ImageView profileImageView;
    private Button updateButton;
    private Uri mediaUri;
    private String userId;
    private DatabaseReference databaseReference;
    private CloudinaryManager cloudinaryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_user);

        // Khởi tạo các view
        editTextPhone = findViewById(R.id.et_Phone);
        editTextName = findViewById(R.id.et_NickName);
        editTextEmail = findViewById(R.id.et_Email);
        editTextPassword = findViewById(R.id.et_password);
        profileImageView = findViewById(R.id.profilePicture);
        updateButton = findViewById(R.id.btn_detailSave);

        // Khởi tạo Firebase và Cloudinary
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);
        cloudinaryManager = new CloudinaryManager();

        // Load dữ liệu người dùng
        loadUserData(currentUser);

        // Sự kiện click vào ảnh profile để mở trình chọn media (ảnh/video)
        profileImageView.setOnClickListener(v -> openMediaPicker());

        // Xử lý khi nhấn nút cập nhật
        updateButton.setOnClickListener(v -> updateUserData());
    }

    private void loadUserData(FirebaseUser user) {
        editTextEmail.setText(user.getEmail());
        // Load thêm thông tin người dùng từ Firebase Realtime Database nếu cần
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Map<String, Object> data = (Map<String, Object>) task.getResult().getValue();
                if (data != null) {
                    editTextName.setText((String) data.get("name"));
                    editTextPhone.setText((String) data.get("phone"));
                    String avatarUrl = (String) data.get("avatar");
                    if (avatarUrl != null) {
                        // Load hình ảnh nếu cần
                    }
                }
            }
        });
    }

    private void openMediaPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("*/*"); // Cho phép chọn cả ảnh và video
        String[] mimeTypes = {"image/*", "video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mediaUri = data.getData();
            profileImageView.setImageURI(mediaUri); // Hiển thị media được chọn
        }
    }

    private void updateUserData() {
        String phone = editTextPhone.getText().toString();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Cập nhật email
        if (!email.isEmpty() && !email.equals(user.getEmail())) {
            user.updateEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Detail_user.this, "Email updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Detail_user.this, "Email update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Cập nhật mật khẩu
        if (!password.isEmpty()) {
            user.updatePassword(password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Detail_user.this, "Password updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Detail_user.this, "Password update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Tiếp tục upload media và cập nhật dữ liệu người dùng
        if (mediaUri != null) {
            uploadMediaToCloudinaryAndSaveData(name, phone);
        } else {
            saveDataWithoutMedia(name, phone);
        }
    }

    private void uploadMediaToCloudinaryAndSaveData(String name, String phone) {
        cloudinaryManager.uploadMedia(this, mediaUri, new CloudinaryManager.UploadCallback() {
            @Override
            public void onSuccess(String mediaUrl) {
                // Lưu dữ liệu người dùng với URL media từ Cloudinary
                saveDataToRealtimeDatabase(name, phone, mediaUrl);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(Detail_user.this, "Media upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveDataWithoutMedia(String name, String phone) {
        saveDataToRealtimeDatabase(name, phone, null);
    }

    private void saveDataToRealtimeDatabase(String name, String phone, String mediaUrl) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);
        if (mediaUrl != null) {
            updates.put("avatar", mediaUrl); // avatar sẽ lưu link của ảnh hoặc video
        }

        databaseReference.updateChildren(updates)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình UserFragment
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Profile update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
