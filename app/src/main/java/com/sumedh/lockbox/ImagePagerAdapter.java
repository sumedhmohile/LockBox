package com.sumedh.lockbox;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageHolder> {

    private final String TAG = "ImagePagerAdapter";
    private Box box;
    private Context context;
    private List<Uri> filePaths;

    ImagePagerAdapter(Context context, Box box, List<Uri> file) {
        this.context = context;
        this.box = box;
        this.filePaths = file;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_view_slider, parent, false);
        return new ImageHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, int position) {
        holder.imageView.setImageURI(filePaths.get(position));
    }

    @Override
    public int getItemCount() {
        return box.getFiles().size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.box_image);
        }
    }
}
