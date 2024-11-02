package com.example.recipe_app.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.Helper.CloudinaryManager;
import com.example.recipe_app.Model.NewRecipe;
import com.example.recipe_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Create_recipe extends AppCompatActivity {
    private ImageView cookPicture, videoImageView;
    private Button btnSave;
    private EditText etCookName, etCalories, etDesc;
    private Spinner categorySpinner;
    private Uri imageUri, videoUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private DatabaseReference databaseRef;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipre);

        // Initialize Firebase Realtime Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("categories");

        // Bind views
        cookPicture = findViewById(R.id.cookPicture);
        videoImageView = findViewById(R.id.Video); // ImageView for video
        btnSave = findViewById(R.id.btn_save);
        etCookName = findViewById(R.id.et_cookName);
        categorySpinner = findViewById(R.id.categorySpinner);
        etCalories = findViewById(R.id.Category);
        etDesc = findViewById(R.id.desc);

        // Set up click listeners
        cookPicture.setOnClickListener(v -> openImageChooser());
        videoImageView.setOnClickListener(v -> openVideoChooser());
        btnSave.setOnClickListener(v -> saveRecipe());
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openVideoChooser() {
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
            // You can set a placeholder image for the video or handle it as needed
            videoImageView.setImageURI(videoUri);
        }
    }

    private void saveRecipe() {
        String cookName = etCookName.getText().toString();
        int calories = Integer.parseInt(etCalories.getText().toString());
        String description = etDesc.getText().toString();
        selectedCategory = categorySpinner.getSelectedItem().toString(); // Get selected category

        // Create a NewRecipe instance
        NewRecipe recipe = new NewRecipe(null, cookName, selectedCategory, calories, description, null, null);

        // Upload image if exists
        if (imageUri != null) {
            CloudinaryManager cloudinaryManager = new CloudinaryManager();
            cloudinaryManager.uploadMedia(this, imageUri, new CloudinaryManager.UploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    recipe.setImageUrl(imageUrl);
                    // Upload video if exists
                    if (videoUri != null) {
                        cloudinaryManager.uploadMedia(Create_recipe.this, videoUri, new CloudinaryManager.UploadCallback() {
                            @Override
                            public void onSuccess(String videoUrl) {
                                recipe.setVideoUrl(videoUrl);
                                saveRecipeToFirebase(recipe);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(Create_recipe.this, "Video upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        saveRecipeToFirebase(recipe);
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(Create_recipe.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (videoUri != null) {
            CloudinaryManager cloudinaryManager = new CloudinaryManager();
            cloudinaryManager.uploadMedia(this, videoUri, new CloudinaryManager.UploadCallback() {
                @Override
                public void onSuccess(String videoUrl) {
                    recipe.setVideoUrl(videoUrl);
                    saveRecipeToFirebase(recipe);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(Create_recipe.this, "Video upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please select an image or video", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRecipeToFirebase(NewRecipe recipe) {
        String recipeId = databaseRef.child(selectedCategory).push().getKey();
        recipe.setRecipeId(recipeId);

        databaseRef.child(selectedCategory).child(recipeId).setValue(recipe)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Create_recipe.this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Create_recipe.this, "Failed to save recipe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}