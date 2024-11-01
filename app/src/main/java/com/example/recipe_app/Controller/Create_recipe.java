package com.example.recipe_app.Controller;

import android.content.Intent;
import android.graphics.Bitmap;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipe_app.Model.Recipe;
import com.example.recipe_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Create_recipe extends AppCompatActivity {

    private ImageView cookPicture;
    private Uri imageUri;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private EditText etCookName, etCalories, etDescription;
    private Spinner categorySpinner;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipre);

        // Firebase initialization
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes");

        cookPicture = findViewById(R.id.cookPicture);
        etCookName = findViewById(R.id.et_cookName);
        etCalories = findViewById(R.id.Category);  // Assuming 'Category' is for calories
        etDescription = findViewById(R.id.desc);
        categorySpinner = findViewById(R.id.categorySpinner);
        btnSave = findViewById(R.id.btn_save);

        // Handle image selection
        cookPicture.setOnClickListener(view -> selectImage());

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Handle save button click
        btnSave.setOnClickListener(view -> uploadRecipe());

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                cookPicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadRecipe() {
        if (imageUri != null) {
            // Generate unique identifier for the image
            String imageId = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + imageId);

            // Upload image to Firebase Storage
            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get image download URL
                        ref.getDownloadUrl().addOnSuccessListener(uri -> saveRecipeToDatabase(uri.toString()));
                    })
                    .addOnFailureListener(e -> Toast.makeText(Create_recipe.this, "Image Upload Failed", Toast.LENGTH_SHORT).show());
        } else {
            saveRecipeToDatabase(null);  // No image selected
        }
    }

    private void saveRecipeToDatabase(String imageUrl) {
        String userId = auth.getCurrentUser().getUid();
        String recipeName = etCookName.getText().toString().trim();
        String calories = etCalories.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (recipeName.isEmpty() || calories.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique recipeId
        String recipeId = databaseReference.push().getKey();

        // Create a Recipe object
        Recipe recipe = new Recipe(recipeId, userId, recipeName, calories, description, category, imageUrl);

        // Save the Recipe object to Firebase Database
        databaseReference.child(recipeId).setValue(recipe)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Create_recipe.this, "Recipe created successfully!", Toast.LENGTH_SHORT).show();
                        finish();  // Close activity
                    } else {
                        Toast.makeText(Create_recipe.this, "Failed to save recipe", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

