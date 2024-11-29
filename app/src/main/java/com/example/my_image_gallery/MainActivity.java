package com.example.my_image_gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_image_gallery.ImageAdapter;
import com.example.my_image_gallery.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    TextView photoCount = null;
    private List<String> imagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Hiển thị dạng lưới

        // Kiểm tra và yêu cầu quyền
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            } else {
                loadImages();
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            } else {
                loadImages();
            }
        }
    }

    @Override
    public void onItemClick(String imagePath) {
        Intent intent = new Intent(MainActivity.this, PhotoDetailActivity.class);
        intent.putExtra("imagePath", imagePath);  // Truyền đường dẫn ảnh vào Intent
        startActivity(intent);
    }

    private void loadImages() {
        photoCount = findViewById(R.id.photoCount);
        imagePaths = getAllImages(); // Lấy danh sách ảnh
        photoCount.setText("Photo(" + imagePaths.size() + ")"); // Hiển thị số lượng ảnh
        imageAdapter = new ImageAdapter(this, imagePaths, this); // Tạo Adapter
        recyclerView.setAdapter(imageAdapter); // Gắn Adapter vào RecyclerView
    }

    private List<String> getAllImages() {
        List<String> imagePaths = new ArrayList<>();
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        );

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(columnIndex);
                imagePaths.add(imagePath);
            }
            cursor.close();
        }
        return imagePaths;
    }
}
