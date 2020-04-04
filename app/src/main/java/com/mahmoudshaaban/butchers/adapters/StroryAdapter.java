package com.mahmoudshaaban.butchers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.CategoriesModel;
import com.mahmoudshaaban.butchers.pojo.Story;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StroryAdapter extends RecyclerView.Adapter<StroryAdapter.StoryViewHolder> {

    Context mcontext;
    List<Story> mdata;

    public StroryAdapter(Context mcontext, List<Story> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public StroryAdapter.StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.story_item, parent, false);
        return new StroryAdapter.StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StroryAdapter.StoryViewHolder holder, int position) {
        Picasso.get()
                .load(mdata.get(position).getImage())
                .fit()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.story_image);
        }
    }
}
