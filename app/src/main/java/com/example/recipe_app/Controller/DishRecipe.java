package com.example.recipe_app.Controller;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.recipe_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DishRecipe extends AppCompatActivity {
    private ImageView imgRecipe, imgLike;
    private TextView tvRecipeName, tvAuthor, tvIngredients, tvDescription, tvLike, tvCalo;
    private VideoView vdRecipe;
    private DatabaseReference databaseReference;
    private String recipeId;
    private Button btnFollow;
    private boolean isLiked = false;// Biến kiểm tra trạng thái like
    private boolean isFollow = false;
    private int likeCount;
    private  int followerCount;
    private  int followingCount;
    private DatabaseReference databaseReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.description);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dish_descrip), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

// Quay lại màn hình trước và load lại Fragment cũ nếu có
        ImageView imageView = findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    // Quay lại Fragment trước đó và load lại trạng thái
                    getSupportFragmentManager().popBackStack();
                } else {
                    // Nếu không có fragment nào trong backstack, kết thúc Activity hiện tại
                    finish();
                }
            }
        });
        // Ánh xạ các view
        imgRecipe = findViewById(R.id.dish_image);
        tvRecipeName = findViewById(R.id.recipe_title);
        tvAuthor = findViewById(R.id.username);
        tvLike = findViewById(R.id.tv_like);
        tvCalo = findViewById(R.id.tv_calo);
        btnFollow = findViewById(R.id.follow_button);
        tvIngredients = findViewById(R.id.ingredients);
        vdRecipe = findViewById(R.id.video_guide);
        imgLike = findViewById(R.id.like_button);// Thêm ImageView cho nút like

        // Lấy recipeId từ Intent
        recipeId = getIntent().getStringExtra("recipeId");


        // Tham chiếu đến Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes").child(recipeId);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Accounts").child(userID);
        // Gọi phương thức để lấy dữ liệu
        loadRecipeData();

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra trạng thái like
                if (!isLiked) {
                    // Thay đổi hình ảnh thành hình like màu đỏ khi like
                    imgLike.setBackgroundResource(R.drawable.heart_red);  // Thay đổi hình ảnh thành like đỏ
                    likeCount++;
                    tvLike.setText(String.valueOf(likeCount));  // Cập nhật số lượng like lên giao diện

                    // Cập nhật lại số like trong Firebase
                    databaseReference.child("like").setValue(likeCount);
                    updateUserLikes(userID, recipeId, true);
                    saveToUserLikes(userID, recipeId);
                    // Đánh dấu là đã like
                    isLiked = true;
                } else {
                    // Nếu đã like rồi, gỡ like và trả lại hình ảnh mặc định
                    imgLike.setBackgroundResource(R.drawable.heart_nomal);  // Thay đổi hình ảnh thành like bình thường
                    likeCount--;
                    tvLike.setText(String.valueOf(likeCount));

                    // Cập nhật lại số like trong Firebase
                    databaseReference.child("like").setValue(likeCount);

                    updateUserLikes(userID, recipeId, false);
                    // Đánh dấu là chưa like
                    isLiked = false;
                }
            }
        });

    }

    // Lưu trạng thái like của người dùng đối với một công thức
    private void updateUserLikes(String userID, String recipeId, boolean isLiked) {
        // Tham chiếu đến node "UserLikes"
        DatabaseReference userLikeRef = FirebaseDatabase.getInstance().getReference("UserLikes").child(userID).child(recipeId);

        if (isLiked) {
            // Nếu like, cập nhật trạng thái like (true) vào Firebase
            userLikeRef.setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DishRecipe.this, "Trạng thái like đã được cập nhật!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DishRecipe.this, "Lỗi khi cập nhật trạng thái like!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Nếu bỏ like, xóa khỏi Firebase
            userLikeRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(DishRecipe.this, "Đã gỡ công thức khỏi yêu thích!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DishRecipe.this, "Lỗi khi gỡ yêu thích!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateUserFollows(String userID, String authorID, boolean isFollow) {
        // Tham chiếu đến node "UserFollows"
        DatabaseReference userFollowsRef = FirebaseDatabase.getInstance().getReference("UserFollows").child(userID);
        DatabaseReference authorFollowsRef = FirebaseDatabase.getInstance().getReference("UserFollows").child(authorID);

        if (isFollow) {
            // Thêm authorID vào trường "following" của userID
            userFollowsRef.child("following").child(authorID).setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Thêm userID vào trường "follower" của authorID
                    authorFollowsRef.child("follower").child(userID).setValue(true).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(DishRecipe.this, "Bạn đã theo dõi người đăng!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DishRecipe.this, "Lỗi khi thêm người theo dõi!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(DishRecipe.this, "Lỗi khi theo dõi người đăng!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Gỡ authorID khỏi trường "following" của userID
            userFollowsRef.child("following").child(authorID).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Gỡ userID khỏi trường "follower" của authorID
                    authorFollowsRef.child("follower").child(userID).removeValue().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(DishRecipe.this, "Bạn đã hủy theo dõi người đăng!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DishRecipe.this, "Lỗi khi hủy người theo dõi!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(DishRecipe.this, "Lỗi khi hủy theo dõi người đăng!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveToUserLikes(String userId, String recipeId) {
        // Tham chiếu đến bảng Recipes
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference("Recipes").child(recipeId);

        // Lấy thông tin chi tiết từ Recipes
        recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy thông tin công thức từ Recipes
                    String name = snapshot.child("name").getValue(String.class);
                    String image = snapshot.child("image").getValue(String.class);
                    int calories = snapshot.child("calories").getValue(Integer.class);
                    String category = snapshot.child("category").getValue(String.class);
                    // Tham chiếu đến bảng UserLikes
                    DatabaseReference userLikesRef = FirebaseDatabase.getInstance().getReference("UserLikes").child(userId).child(recipeId);

                    // Tạo HashMap để lưu thông tin
                    Map<String, Object> likeData = new HashMap<>();
                    likeData.put("name", name);
                    likeData.put("image", image);
                    likeData.put("calories", calories);
                    likeData.put("likeStatus", true);
                    likeData.put("category", category);

                    // Lưu vào Firebase
                    userLikesRef.setValue(likeData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Công thức đã được lưu vào yêu thích!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Lỗi khi lưu yêu thích!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Không tìm thấy công thức!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadRecipeData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String authorId = snapshot.child("userId").getValue(String.class);  // Lấy userId của người đăng công thức
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    String ingredients = snapshot.child("description").getValue(String.class);
                    likeCount = snapshot.child("like").getValue(Integer.class);  // Lấy số like từ Firebase
                    int calo = snapshot.child("calories").getValue(Integer.class);
                    String video = snapshot.child("video").getValue(String.class);

                    // Cập nhật thông tin món ăn
                    tvRecipeName.setText(name);
                    tvIngredients.setText(ingredients);
                    tvLike.setText(String.valueOf(likeCount));  // Hiển thị số like
                    tvCalo.setText(String.valueOf(calo));

                    Glide.with(DishRecipe.this).load(imageUrl).into(imgRecipe);

                    // Cập nhật video cho VideoView
                    if (video != null && !video.isEmpty()) {
                        Uri videoUri = Uri.parse(video); // Đặt video URI
                        vdRecipe.setVideoURI(videoUri); // Đặt video vào VideoView

                        // Thêm MediaController để người dùng có thể điều khiển video
                        MediaController mediaController = new MediaController(DishRecipe.this);
                        vdRecipe.setMediaController(mediaController);
                        mediaController.setAnchorView(vdRecipe);

                    } else {
                        Toast.makeText(DishRecipe.this, "Video không có sẵn!", Toast.LENGTH_SHORT).show();
                    }

                    // Kiểm tra trạng thái like của người dùng
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference userLikeRef = FirebaseDatabase.getInstance().getReference("UserLikes").child(userID).child(recipeId).child("likeStatus");
                    userLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            if (userSnapshot.exists()) {
                                isLiked = userSnapshot.getValue(Boolean.class); // Lấy trạng thái like từ Firebase
                                if (isLiked) {
                                    imgLike.setBackgroundResource(R.drawable.heart_red);  // Nếu đã like thì đổi thành đỏ
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DishRecipe.this, "Lỗi khi kiểm tra trạng thái like!", Toast.LENGTH_SHORT).show();
                        }
                    });
                // Kiểm tra trạng thái follow của người dùng từ trường "following"
                    DatabaseReference userFollowRef = FirebaseDatabase.getInstance().getReference("UserFollows").child(userID).child("following").child(authorId);
                    userFollowRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            if (userSnapshot.exists()) {
                                isFollow = userSnapshot.getValue(Boolean.class); // Lấy trạng thái follow từ Firebase
                                if (isFollow) {
                                    btnFollow.setText("Unfollow");  // Nếu đã follow thì đổi thành "Unfollow"
                                } else {
                                    btnFollow.setText("Follow");  // Nếu chưa follow thì giữ "Follow"
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi khi kiểm tra trạng thái follow
                            Toast.makeText(DishRecipe.this, "Lỗi khi kiểm tra trạng thái follow!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Cập nhật tên tác giả từ bảng Accounts dựa trên userId
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(authorId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            if (userSnapshot.exists()) {
                                String authorName = userSnapshot.child("name").getValue(String.class);
                                tvAuthor.setText(authorName); // Cập nhật tên tác giả lên giao diện
                            } else {
                                tvAuthor.setText("Unknown Author");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DishRecipe.this, "Lỗi khi tải tên tác giả!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Cập nhật số lượng followers của người đăng công thức khi follow/unfollow
                    btnFollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Accounts").child(userID);
                            DatabaseReference authorRef = FirebaseDatabase.getInstance().getReference("Accounts").child(authorId);

                            authorRef.child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    followerCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0; // Lấy số followers hiện tại

                                    if (!isFollow) { // Nếu chưa follow
                                        btnFollow.setText("Unfollow"); // Đổi nút thành Unfollow
                                        followerCount++; // Tăng followers của người đăng công thức
                                        followingCount++; // Tăng following của người dùng hiện tại
                                    } else { // Nếu đã follow
                                        btnFollow.setText("Follow"); // Đổi nút thành Follow
                                        // Chỉ giảm nếu followerCount và followingCount lớn hơn 0
                                        if (followerCount > 0) followerCount--;
                                        if (followingCount > 0) followingCount--;
                                    }

                                    // Cập nhật giá trị vào Firebase
                                    authorRef.child("followers").setValue(followerCount);
                                    userRef.child("following").setValue(followingCount);

                                    // Cập nhật trạng thái follow hoặc unfollow
                                    updateUserFollows(userID, authorId, !isFollow);

                                    // Cập nhật trạng thái
                                    isFollow = !isFollow;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(DishRecipe.this, "Lỗi khi cập nhật followers!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DishRecipe.this, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
