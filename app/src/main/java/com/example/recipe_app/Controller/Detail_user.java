package com.example.recipe_app.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.recipe_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Detail_user extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView username;
    private EditText et_NickName, et_Email, et_Phone, et_password;
    private ImageView profilePicture,back_DU;
    private Button btn_detailSave;

    private Uri imageUri;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private StorageReference storageRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_user);

        // Initialize Firebase and UI elements
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(currentUser.getUid());
        storageRef = FirebaseStorage.getInstance().getReference("ProfilePictures");

        username = findViewById(R.id.username);
        et_NickName = findViewById(R.id.et_NickName);
        et_Email = findViewById(R.id.et_Email);
        et_Phone = findViewById(R.id.et_Phone);
        et_password = findViewById(R.id.et_password);
        profilePicture = findViewById(R.id.profilePicture);
        btn_detailSave = findViewById(R.id.btn_detailSave);
        back_DU = findViewById(R.id.back_DU);

        progressDialog = new ProgressDialog(this);

        // Load user data from Firebase
        loadUserData();
        back_DU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    // Nếu không còn fragment nào trong back stack, có thể gọi finish() hoặc xử lý khác
                    finish();
                }
            }
        });
        // Set up click listener for profile picture to upload new image
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        // Save changes on button click
        btn_detailSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }

    private void loadUserData() {
        userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    username.setText(dataSnapshot.child("name").getValue(String.class));
                    et_Email.setText(dataSnapshot.child("email").getValue(String.class));
                    et_NickName.setText(dataSnapshot.child("name").getValue(String.class));
                    et_Phone.setText(dataSnapshot.child("phone").getValue(String.class));

                    // Load profile picture if it exists
                    String avatarUrl = dataSnapshot.child("avatar").getValue(String.class);
                    if (avatarUrl != null) {
                        // Load image using a library like Glide or Picasso
                        Glide.with(Detail_user.this).load(avatarUrl).into(profilePicture);
                    }
                } else {
                    Toast.makeText(Detail_user.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUserData() {
        progressDialog.setMessage("Saving changes...");
        progressDialog.show();

        // Update avatar if a new image is selected
        if (imageUri != null) {
            StorageReference fileRef = storageRef.child(auth.getCurrentUser().getUid() + ".jpg");
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String avatarUrl = task.getResult().toString();
                                    saveDataToDatabase(avatarUrl);
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Detail_user.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            saveDataToDatabase(null);
        }
    }

    private void saveDataToDatabase(String avatarUrl) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", et_NickName.getText().toString().trim());
        updates.put("phone", et_Phone.getText().toString().trim());

        // Lấy mật khẩu mới từ EditText
        String newPassword = et_password.getText().toString().trim();

        // Cập nhật mật khẩu trong Firebase Authentication
        FirebaseUser user = auth.getCurrentUser();
        if (!newPassword.isEmpty()) {
            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Nếu cập nhật mật khẩu thành công, cập nhật avatar nếu có
                        if (avatarUrl != null) {
                            updates.put("avatar", avatarUrl);
                        }
                        // Cập nhật thông tin người dùng trong Realtime Database
                        userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(Detail_user.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Detail_user.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Detail_user.this, "Failed to update password.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Nếu không có mật khẩu mới, chỉ cập nhật các trường khác
            if (avatarUrl != null) {
                updates.put("avatar", avatarUrl);
            }
            userRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(Detail_user.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Detail_user.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
