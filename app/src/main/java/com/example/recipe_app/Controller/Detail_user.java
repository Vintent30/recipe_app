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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Detail_user extends AppCompatActivity {
    private EditText editTextPhone, editTextName;
    private ImageView profileImageView;
    private Button updateButton;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_user);

        // Initialize views
        editTextPhone = findViewById(R.id.et_Phone); // Ensure you use the correct IDs from your layout
        editTextName = findViewById(R.id.et_NickName); // Assuming the name input field is for the nickname
        profileImageView = findViewById(R.id.profilePicture);
        updateButton = findViewById(R.id.btn_detailSave);

        // Get Firebase references
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Accounts").child(userId);

        // Set up profile image click listener to open image picker
        profileImageView.setOnClickListener(v -> openImagePicker());

        // Update button listener
        updateButton.setOnClickListener(v -> updateUserData());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void updateUserData() {
        String phone = editTextPhone.getText().toString();
        String name = editTextName.getText().toString();

        if (imageUri != null) {
            uploadImageAndSaveData(name, phone);
        } else {
            saveDataWithoutImage(name, phone);
        }
    }

    private void uploadImageAndSaveData(String name, String phone) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profile_images").child(userId + ".jpg");
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String avatarUrl = uri.toString();
                    saveDataToFirebase(name, phone, avatarUrl);
                })
        ).addOnFailureListener(e -> Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show());
    }

    private void saveDataWithoutImage(String name, String phone) {
        saveDataToFirebase(name, phone, null);
    }

    private void saveDataToFirebase(String name, String phone, String avatarUrl) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);
        if (avatarUrl != null) {
            updates.put("avatar", avatarUrl);
        }

        databaseReference.updateChildren(updates)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show());
    }
}
