package com.example.recipe_app.Helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryManager {
    private static final String CLOUD_NAME = "djt5scxpj";
    private static final String API_KEY = "261253623231484";
    private static final String API_SECRET = "zSXfVs6Wzj8Iby7SyLseh9HWSFo";
    private Cloudinary cloudinary;

    // Constructor
    public CloudinaryManager() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        cloudinary = new Cloudinary(config);
    }

    // Method to convert URI to actual file path
    private String getRealPathFromURI(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

    // Method to upload media to Cloudinary
    public void uploadMedia(Context context, Uri mediaUri, UploadCallback callback) {
        String filePath = getRealPathFromURI(context, mediaUri);

        if (filePath == null) {
            Toast.makeText(context, "Cannot get file path", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(() -> {
            try {
                // Upload media to Cloudinary
                Map uploadResult = cloudinary.uploader().upload(filePath, ObjectUtils.asMap(
                        "resource_type", "auto" // Let Cloudinary auto-detect the resource type
                ));
                String mediaUrl = uploadResult.get("url").toString();
                Log.d("Cloudinary", "Media URL: " + mediaUrl);

                if (callback != null) {
                    callback.onSuccess(mediaUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        }).start();
    }

    public interface UploadCallback {
        void onSuccess(String mediaUrl);
        void onFailure(Exception e);
    }
}