package com.example.recipe_app.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference databaseReference;

    public FirebaseHelper() {
        // Khởi tạo tham chiếu tới Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Categories");
    }

    // Thêm một danh mục vào Firebase (có thể dùng để thêm danh mục mới và lấy danh mục)
    public void addCategoryToFirebase(Category category) {
        String key = databaseReference.push().getKey(); // Tạo khóa duy nhất
        if (key != null) {
            databaseReference.child(key).setValue(category);
        }
    }

    // Lấy tất cả danh mục từ Firebase
    public DatabaseReference getCategoriesFromFirebase() {
        return databaseReference;
    }
}
