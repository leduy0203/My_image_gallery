package com.example.my_image_gallery;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<String> imagePaths;  // Đây là danh sách đường dẫn ảnh
    private Context context;
    private OnItemClickListener onItemClickListener;  // Giao diện listener

    // Giao diện xử lý sự kiện click vào ảnh
    public interface OnItemClickListener {
        void onItemClick(String imagePath);
    }

    // Constructor thêm OnItemClickListener
    public ImageAdapter(Context context, List<String> imagePaths, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = imagePaths.get(position);
        Glide.with(context).load(imagePath).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PhotoDetailActivity.class);
            intent.putStringArrayListExtra("imagePaths", new ArrayList<>(imagePaths));  // Truyền danh sách ảnh
            intent.putExtra("currentIndex", position);  // Truyền chỉ số ảnh hiện tại
            context.startActivity(intent);
        });
    }






    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
