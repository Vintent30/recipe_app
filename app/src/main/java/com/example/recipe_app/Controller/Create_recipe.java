package com.example.recipe_app.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Create_recipe extends AppCompatActivity {
    private ImageView cookPicture, videoThumbnail;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_recipre);

        // Initialize Firebase references
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        categoryReference = FirebaseDatabase.getInstance().getReference("Categories");

        // Initialize views
        cookPicture = findViewById(R.id.cookPicture);
        videoThumbnail = findViewById(R.id.Video);
        saveRecipeButton = findViewById(R.id.btn_save);
        recipeName = findViewById(R.id.et_cookName);
        recipeCalories = findViewById(R.id.Category);
        recipeDescription = findViewById(R.id.desc);
        categorySpinner = findViewById(R.id.categorySpinner);

        // Set up image selection
        cookPicture.setOnClickListener(v -> selectImage());
        videoThumbnail.setOnClickListener(v -> selectVideo());

        // Set up save recipe button
        saveRecipeButton.setOnClickListener(v -> uploadRecipe());

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
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // Giả định rằng danh mục được chọn có ID tương ứng (lấy từ cơ sở dữ liệu)
                // Ở đây giả định ID của danh mục là position + 1 dưới dạng chuỗi
                selectedCategoryId = String.valueOf(position + 1); // Lấy categoryId từ cơ sở dữ liệu
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
            videoThumbnail.setImageURI(videoUri);
        }
    }

    private void uploadRecipe() {
        if (currentUser == null || imageUri == null || videoUri == null) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn ảnh, video", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        StorageReference fileRefImage = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
        StorageReference fileRefVideo = storageReference.child("videos/" + System.currentTimeMillis() + ".mp4");

        fileRefImage.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRefImage.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();

            fileRefVideo.putFile(videoUri).addOnSuccessListener(taskSnapshot1 -> fileRefVideo.getDownloadUrl().addOnSuccessListener(uri1 -> {
                String videoUrl = uri1.toString();

                String recipeId = databaseReference.push().getKey();
                Recipe recipe = new Recipe(recipeId, recipeName.getText().toString(), recipeCalories.getText().toString(),
                        recipeDescription.getText().toString(), categorySpinner.getSelectedItem().toString(),
                        imageUrl, videoUrl, "active", userId, selectedCategoryId);

                if (recipeId != null) {
                    databaseReference.child(recipeId).setValue(recipe).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Create_recipe.this, "Recipe created successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Create_recipe.this, "Failed to create recipe", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })).addOnFailureListener(e -> Toast.makeText(Create_recipe.this, "Failed to upload video", Toast.LENGTH_SHORT).show());
        })).addOnFailureListener(e -> Toast.makeText(Create_recipe.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
    }

}
