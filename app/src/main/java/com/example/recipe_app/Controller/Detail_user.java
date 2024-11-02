package com.example.recipe_app.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Detail_user extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageView, profilePicture;
    private EditText etNickName, etEmail, etPhone, etPassword;
    private Button btnSave;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.detail_user);

        // Initialize Firebase references
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);
        storageReference = FirebaseStorage.getInstance().getReference("ProfileImages");

        // Link views
        profilePicture = findViewById(R.id.profilePicture);
        etNickName = findViewById(R.id.et_NickName);
        etEmail = findViewById(R.id.et_Email);
        etPhone = findViewById(R.id.et_Phone);
        etPassword = findViewById(R.id.et_password);
        btnSave = findViewById(R.id.btn_detailSave);
        imageView = findViewById(R.id.back_DU);

        // Make email non-editable
        etEmail.setFocusable(false);

        // Set up listeners
        btnSave.setOnClickListener(view -> updateUserData());
        imageView.setOnClickListener(view -> finish());
        profilePicture.setOnClickListener(view -> openFileChooser());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailUser), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateUserData() {
        String nickName = etNickName.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        // Check if any fields are empty
        if (nickName.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to update data
        Map<String, Object> userData = new HashMap<>();
        userData.put("Name", nickName);
        userData.put("Phone", phone);
        userData.put("Password", password);

        // Update data in Firebase
        databaseReference.updateChildren(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Detail_user.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                // Return to UserFragment
                finish();
            } else {
                Toast.makeText(Detail_user.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            String imageName = userId + ".jpg";
            StorageReference fileReference = storageReference.child(imageName);

            UploadTask uploadTask = fileReference.putFile(imageUri);

            uploadTask.addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                updateImageUrlInDatabase(imageUrl);
            }).addOnFailureListener(e -> {
                Toast.makeText(Detail_user.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            })).addOnFailureListener(e -> {
                Toast.makeText(Detail_user.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(Detail_user.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateImageUrlInDatabase(String imageUrl) {
        databaseReference.child("Avatar").setValue(imageUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Detail_user.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Detail_user.this, "Failed to update image URL!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
