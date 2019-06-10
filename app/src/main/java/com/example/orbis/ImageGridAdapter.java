package com.example.orbis;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.GridItemViewHolder> {
    private List<ImageItem> imageItemList;

    ImageGridAdapter(List imageList) {
        this.imageItemList = imageList;
    }

    class GridItemViewHolder extends RecyclerView.ViewHolder {
        SquareImageView siv;
        ImageButton btn;

        GridItemViewHolder(View view) {
            super(view);
            siv = view.findViewById(R.id.siv);
            btn = view.findViewById(R.id.imageDeleteButton);
        }
    }

    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_grid, parent, false);

        return new GridItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GridItemViewHolder holder, final int position) {
        final String path = imageItemList.get(position).URI;

        Picasso.get()
                .load(path)
                .resize(250, 250)
                .centerCrop()
                .into(holder.siv);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageItemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, imageItemList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageItemList.size();
    }

    List<ImageItem> getImageItemList() {
        return imageItemList;
    }
}