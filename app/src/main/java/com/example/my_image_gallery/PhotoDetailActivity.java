package com.example.my_image_gallery;

import android.os.Bundle;
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

        // Gán OnTouchListener cho ViewPager2
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            private float initialX = 0f;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() == 3) { // Chỉ xử lý khi đủ 3 ngón tay
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = event.getX(); // Ghi lại vị trí bắt đầu
                            viewPager.requestDisallowInterceptTouchEvent(true); // Ngăn ViewPager2 chặn sự kiện
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            float currentX = event.getX();
                            float deltaX = currentX - initialX;

                            if (Math.abs(deltaX) > 50) { // Kiểm tra nếu vuốt đủ xa
                                if (deltaX < 0) {
                                    // Vuốt sang trái: Chuyển sang ảnh tiếp theo
                                    int nextIndex = viewPager.getCurrentItem() + 1;
                                    if (nextIndex < imagePaths.size()) {
                                        viewPager.setCurrentItem(nextIndex, true);
                                    }
                                } else {
                                    // Vuốt sang phải: Quay lại ảnh trước
                                    int prevIndex = viewPager.getCurrentItem() - 1;
                                    if (prevIndex >= 0) {
                                        viewPager.setCurrentItem(prevIndex, true);
                                    }
                                }
                                initialX = currentX; // Cập nhật lại vị trí
                            }
                            return true;

                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            viewPager.requestDisallowInterceptTouchEvent(false); // Cho phép ViewPager2 xử lý lại sự kiện
                            return true;
                    }
                }
                return false;
            }
        });
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
