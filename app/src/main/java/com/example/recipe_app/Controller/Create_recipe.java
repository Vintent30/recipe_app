package com.example.recipe_app.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.Fragment.UserFragment;
import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Create_recipe extends AppCompatActivity {
    private ImageView cookPicture, videoThumbnail, back;
    private Button saveRecipeButton;
    private Uri imageUri, videoUri;
    private EditText recipeName, recipeCalories, recipeDescription;
    private Spinner categorySpinner;
    private DatabaseReference databaseReference, categoryReference;
    private StorageReference storageReference;
    private FirebaseUser currentUser;

    private String selectedCategoryId;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipre);

        // Initialize Firebase references
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        categoryReference = FirebaseDatabase.getInstance().getReference("Categories");

        // Initialize views
        cookPicture = findViewById(R.id.cookPicture);
        back= findViewById(R.id.back_create);
        videoThumbnail = findViewById(R.id.Video);
        saveRecipeButton = findViewById(R.id.btn_save);
        recipeName = findViewById(R.id.et_cookName);
        recipeCalories = findViewById(R.id.Category);
        recipeDescription = findViewById(R.id.desc);
        categorySpinner = findViewById(R.id.categorySpinner);

        // Set up image and video selection
        cookPicture.setOnClickListener(v -> selectImage());
        videoThumbnail.setOnClickListener(v -> selectVideo());

        // Set up save recipe button
        saveRecipeButton.setOnClickListener(v -> uploadRecipe());
        back.setOnClickListener(new View.OnClickListener() {
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
        setupCategorySpinner();
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = String.valueOf(position + 1); // Giả định ID là position + 1
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void selectVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            cookPicture.setImageURI(imageUri);
        } else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();
            showVideoThumbnail(videoUri);
        }
    }

    private void showVideoThumbnail(Uri videoUri) {
        try {
            // Sử dụng MediaMetadataRetriever để tạo thumbnail từ URI
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, videoUri);

            // Lấy bitmap của frame đầu tiên (timeUs = 0)
            Bitmap thumbnail = retriever.getFrameAtTime(0);

            // Hiển thị thumbnail
            if (thumbnail != null) {
                videoThumbnail.setImageBitmap(thumbnail);
            } else {
                Toast.makeText(this, "Không thể tạo ảnh đại diện cho video", Toast.LENGTH_SHORT).show();
            }

            retriever.release();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tạo ảnh đại diện", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadRecipe() {
        if (currentUser == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để tạo công thức", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null || videoUri == null || recipeName.getText().toString().trim().isEmpty()
                || recipeDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn ảnh, video", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị thông báo chờ
        Toast.makeText(this, "Đang tạo công thức, vui lòng chờ...", Toast.LENGTH_SHORT).show();

        String userId = currentUser.getUid();
        StorageReference fileRefImage = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
        StorageReference fileRefVideo = storageReference.child("videos/" + System.currentTimeMillis() + ".mp4");

        fileRefImage.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRefImage.getDownloadUrl().addOnSuccessListener(imageUrl -> {
            fileRefVideo.putFile(videoUri).addOnSuccessListener(taskSnapshot1 -> fileRefVideo.getDownloadUrl().addOnSuccessListener(videoUrl -> {
                String recipeId = databaseReference.push().getKey();

                int calories;
                try {
                    calories = Integer.parseInt(recipeCalories.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Calories phải là một số", Toast.LENGTH_SHORT).show();
                    return;
                }

                Recipe recipe = new Recipe(recipeId, recipeName.getText().toString(), calories,
                        recipeDescription.getText().toString(), categorySpinner.getSelectedItem().toString(),
                        imageUrl.toString(), videoUrl.toString(), "active", userId, selectedCategoryId, 0);

                if (recipeId != null) {
                    databaseReference.child(recipeId).setValue(recipe).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            categoryReference.child(selectedCategoryId).child("recipes").child(recipeId).setValue(true)
                                    .addOnCompleteListener(categoryTask -> {
                                        if (categoryTask.isSuccessful()) {
                                            Intent intent = new Intent(Create_recipe.this, UserFragment.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                            Toast.makeText(Create_recipe.this, "Tạo công thức thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Create_recipe.this, "Thất bại khi liên kết công thức với danh mục", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Create_recipe.this, "Thất bại khi tạo công thức", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })).addOnFailureListener(e -> Toast.makeText(Create_recipe.this, "Thất bại khi tải video lên", Toast.LENGTH_SHORT).show());
        })).addOnFailureListener(e -> Toast.makeText(Create_recipe.this, "Thất bại khi tải ảnh lên", Toast.LENGTH_SHORT).show());
    }
}
