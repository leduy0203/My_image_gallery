package com.example.my_image_gallery;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;

import java.util.List;

public class PhotoDetailActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private int currentImageIndex = 0;  // Chỉ số ảnh hiện tại trong danh sách
    private List<String> imagePaths;  // Danh sách các đường dẫn ảnh
    private PhotoPagerAdapter adapter; // Adapter cho ViewPager2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        // Lấy danh sách ảnh từ intent hoặc từ nguồn dữ liệu
        imagePaths = getIntent().getStringArrayListExtra("imagePaths");
        currentImageIndex = getIntent().getIntExtra("currentIndex", 0);  // Chỉ số ảnh ban đầu

        viewPager = findViewById(R.id.viewPager2);

        // Tạo adapter và gán cho ViewPager2
        adapter = new PhotoPagerAdapter(imagePaths);
        viewPager.setAdapter(adapter);

        // Thiết lập vị trí ảnh hiện tại
        viewPager.setCurrentItem(currentImageIndex);

        // Thêm PageTransformer để hiệu ứng chuyển động khi vuốt
        viewPager.setPageTransformer(new DepthPageTransformer());
    }

    // PageTransformer để thêm hiệu ứng chuyển ảnh
    private static class DepthPageTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            if (position < -1) { // [-Infinity,-1)
                page.setAlpha(0);
            } else if (position <= 1) { // [-1,1]
                page.setAlpha(1);
                page.setTranslationX(-position * pageWidth);
                float scaleFactor = Math.max(0.85f, 1 - Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            } else { // (1,+Infinity]
                page.setAlpha(0);
            }
        }
    }
}
