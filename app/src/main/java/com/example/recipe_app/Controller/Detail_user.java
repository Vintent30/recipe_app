package com.example.recipe_app.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.recipe_app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Detail_user extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private FirebaseUser currentUser;

    private EditText etNickName, etPhone, etPassword;
    private TextView etEmail;
    private ImageView profilePicture;
    private Button btnSave;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_user);

        // Initialize Firebase Auth, Firestore, and Storage references
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("avatars");
        currentUser = mAuth.getCurrentUser();

        // Initialize views
        etNickName = findViewById(R.id.et_NickName);
        etEmail = findViewById(R.id.et_Email);
        etPhone = findViewById(R.id.et_Phone);
        etPassword = findViewById(R.id.et_password);
        profilePicture = findViewById(R.id.profilePicture);
        btnSave = findViewById(R.id.btn_detailSave);

        // Disable email EditText to make it uneditable
        etEmail.setEnabled(false);

        loadUserData();

        // Set click listener on profilePicture to open image picker
        profilePicture.setOnClickListener(view -> openImagePicker());

        // Set click listener on btnSave to save updated data
        btnSave.setOnClickListener(view -> saveUserData());
    }

    // Method to load user data from Firestore
    private void loadUserData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference docRef = db.collection("Accounts").document(userId);

            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String phone = documentSnapshot.getString("phone");
                    String avatar = documentSnapshot.getString("avatar");

                    etNickName.setText(name);
                    etEmail.setText(email);
                    etPhone.setText(phone);

                    // Load avatar from Firebase Storage if available, otherwise load default image
                    if (avatar != null && !avatar.isEmpty()) {
                        FirebaseStorage.getInstance().getReferenceFromUrl(avatar)
                                .getDownloadUrl()
                                .addOnSuccessListener(uri -> Glide.with(this).load(uri).into(profilePicture))
                                .addOnFailureListener(e -> profilePicture.setImageResource(R.drawable.icon_intro1));
                    } else {
                        profilePicture.setImageResource(R.drawable.icon_intro1);
                    }
                }
            });
        }
    }

    // Method to open image picker for profile picture selection
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            profilePicture.setImageURI(profileImageUri); // Set selected image as profile picture preview
        }
    }

    // Method to save user data (nickname, phone, password, and avatar) to Firebase
    private void saveUserData() {
        String name = etNickName.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference docRef = db.collection("Accounts").document(userId);

            // Update Firestore fields
            docRef.update("name", name, "phone", phone).addOnSuccessListener(aVoid -> {
                if (!password.isEmpty()) {
                    // Update Firebase Authentication password
                    currentUser.updatePassword(password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                if (profileImageUri != null) {
                    uploadProfileImage(userId); // Upload profile picture to Firebase Storage
                } else {
                    Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Method to upload the profile picture to Firebase Storage
    private void uploadProfileImage(String userId) {
        if (profileImageUri != null) {
            StorageReference fileRef = storageRef.child(userId + ".jpg");
            fileRef.putFile(profileImageUri).addOnSuccessListener(taskSnapshot ->
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update avatar URL in Firestore
                        db.collection("Accounts").document(userId).update("avatar", uri.toString())
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show());
                    })
            ).addOnFailureListener(e -> Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show());
        }
    }
}