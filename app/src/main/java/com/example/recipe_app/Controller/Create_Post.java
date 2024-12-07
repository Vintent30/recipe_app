package com.example.recipe_app.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.Model.Post;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Create_Post extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;  // Mã yêu cầu để chọn ảnh

    private ImageView postPicture, editIcon;
    private EditText etContent;
    private Button btnSave;

    private Uri selectedImageUri;

    // Firebase instances
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);

        // Ánh xạ các view từ XML
        postPicture = findViewById(R.id.postPicture);
        editIcon = findViewById(R.id.edit_icon);  // Thêm cho ImageView để chỉnh sửa ảnh
        etContent = findViewById(R.id.content); // Chỉnh sửa tên EditText cho phù hợp
        btnSave = findViewById(R.id.btn_save);

        // Khởi tạo Firebase
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        // Bắt sự kiện khi người dùng nhấn vào edit_icon để chọn ảnh
        editIcon.setOnClickListener(v -> selectImage());

        // Bắt sự kiện lưu bài đăng khi nhấn vào btn_save
        btnSave.setOnClickListener(v -> savePost());
    }

    // Mở thư viện ảnh để người dùng chọn ảnh
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Nhận kết quả từ thư viện ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Cập nhật hình ảnh cho ImageView cookPicture
            postPicture.setImageURI(selectedImageUri);
        }
    }

    // Lưu bài đăng vào Firebase
    private void savePost() {
        if (selectedImageUri != null && !etContent.getText().toString().isEmpty()) {
            // Lấy nội dung bài đăng và userId
            String content = etContent.getText().toString();
            String userId = firebaseAuth.getCurrentUser().getUid();
            String status = "active";  // Mặc định là "active"
            int like = 0;  // Mặc định là 0 lượt thích
            int comment =0;
            // Tạo một PostId duy nhất
            String postId = databaseReference.push().getKey();

            // Tải lên ảnh lên Firebase Storage
            StorageReference fileReference = storageReference.child("posts/" + postId + ".jpg");
            UploadTask uploadTask = fileReference.putFile(selectedImageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Lấy URL của ảnh sau khi tải lên
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Lưu thông tin bài đăng vào Firebase Realtime Database
                    Post post = new Post(postId, uri.toString(), content, status, userId, like,comment);
                    databaseReference.child(postId).setValue(post)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Create_Post.this, "Bài đăng đã được tạo", Toast.LENGTH_SHORT).show();
                                    finish();  // Đóng activity sau khi lưu
                                } else {
                                    Toast.makeText(Create_Post.this, "Lỗi khi lưu bài đăng", Toast.LENGTH_SHORT).show();
                                }
                            });
                }).addOnFailureListener(e -> {
                    Toast.makeText(Create_Post.this, "Không thể tải ảnh lên", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(Create_Post.this, "Không thể tải ảnh lên", Toast.LENGTH_SHORT).show();
            });

        } else {
            Toast.makeText(this, "Vui lòng chọn ảnh và nhập nội dung!", Toast.LENGTH_SHORT).show();
        }
    }
}
