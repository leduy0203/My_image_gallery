package com.example.my_image_gallery;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class PhotoDetailActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private int currentImageIndex = 0; // Chỉ số ảnh hiện tại
    private List<String> imagePaths;  // Danh sách các đường dẫn ảnh
    private PhotoPagerAdapter adapter; // Adapter cho ViewPager2

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        // Lấy danh sách ảnh từ intent
        imagePaths = getIntent().getStringArrayListExtra("imagePaths");
        currentImageIndex = getIntent().getIntExtra("currentIndex", 0);

        viewPager = findViewById(R.id.viewPager2);

        // Tạo adapter và gán cho ViewPager2
        adapter = new PhotoPagerAdapter(imagePaths);
        viewPager.setAdapter(adapter);

        // Thiết lập vị trí ảnh hiện tại
        viewPager.setCurrentItem(currentImageIndex);

        // Thêm PageTransformer
        viewPager.setPageTransformer(new DepthPageTransformer());

        // Khởi tạo GestureDetector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // Chỉ xử lý nếu có 3 ngón tay
                if (e1 != null && e2 != null && e2.getPointerCount() == 3) {
                    if (distanceX > 0) {
                        // Vuốt sang trái: Chuyển sang ảnh tiếp theo
                        int nextIndex = viewPager.getCurrentItem() + 1;
                        if (nextIndex < imagePaths.size()) {
                            viewPager.setCurrentItem(nextIndex, true);
                        }
                    } else if (distanceX < 0) {
                        // Vuốt sang phải: Quay lại ảnh trước
                        int prevIndex = viewPager.getCurrentItem() - 1;
                        if (prevIndex >= 0) {
                            viewPager.setCurrentItem(prevIndex, true);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        // Gán OnTouchListener cho ViewPager2
        viewPager.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private static class DepthPageTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();
            if (position < -1) {
                page.setAlpha(0);
            } else if (position <= 1) {
                page.setAlpha(1);
                page.setTranslationX(-position * pageWidth);
                float scaleFactor = Math.max(0.85f, 1 - Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            } else {
                page.setAlpha(0);
            }
        }
    }
}
