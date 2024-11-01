package com.example.recipe_app.Model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.recipe_app.Model.Category;

public class FirebaseHelper {

    private DatabaseReference databaseReference;

    public FirebaseHelper() {
        // Khởi tạo tham chiếu đến Firebase Realtime Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("categories");
    }

    // Phương thức đẩy dữ liệu lên Firebase
    public void addCategoryToFirebase(Category category) {
        // Tạo một khóa duy nhất cho từng Category
        String categoryId = databaseReference.push().getKey();
        if (categoryId != null) {
            databaseReference.child(categoryId).setValue(category)
                    .addOnSuccessListener(aVoid -> {
                        // Thành công
                        System.out.println("Category added successfully to Firebase.");
                    })
                    .addOnFailureListener(e -> {
                        // Thất bại
                        System.out.println("Failed to add category: " + e.getMessage());
                    });
        }
    }
}
