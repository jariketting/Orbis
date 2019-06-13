package com.example.orbis;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ExampleViewHolder> implements Filterable {
    private ArrayList<DiaryItems> mExampleList;
    public ArrayList<DiaryItems> mExampleListFull;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public DiaryAdapter(ArrayList<DiaryItems> exampleList) {
        this.mExampleList = exampleList;
        mExampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_diary_items, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int position) {
        DiaryItems currentItem = mExampleList.get(position);

        /*exampleViewHolder.mImageView.setImageResource(currentItem.getImageResource());*/

        if(!currentItem.getImageResource().equals("")) {
            Picasso.get()
                    .load(currentItem.getImageResource())
                    .into(exampleViewHolder.mImageView);
        }

        exampleViewHolder.mTextView1.setText(currentItem.getText1());
        exampleViewHolder.mTextView2.setText(currentItem.getText2());
        exampleViewHolder.mTextView3.setText(currentItem.getText3());
    }

    // Dit zorgt ervoor dat je er maar 10 ziet
    private final int limit = 10;

    @Override
    public int getItemCount() {
        if (mExampleList.size() > limit) {
            return limit;
        } else {
            return mExampleList.size();
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DiaryItems> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(mExampleListFull);

            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DiaryItems item : mExampleListFull){
                    if (item.getText1().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);}
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mExampleList.clear();
            mExampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
