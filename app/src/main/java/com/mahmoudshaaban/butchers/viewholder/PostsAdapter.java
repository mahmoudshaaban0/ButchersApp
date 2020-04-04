package com.mahmoudshaaban.butchers.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.adapters.PersonAdapter;
import com.mahmoudshaaban.butchers.pojo.Posts;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    Context mcontext;
    List<Posts> mdata;

    public PostsAdapter(Context mcontext, List<Posts> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.posts_item, parent, false);
        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        Picasso.get()
                .load(mdata.get(position).getUserImage())
                .fit()
                .into(holder.owner_image);
        Picasso.get()
                .load(mdata.get(position).getImage())
                .fit()
                .into(holder.post_image);

        holder.owner_name.setText(mdata.get(position).getUsername());
        holder.post_time.setText(mdata.get(position).getPid());
        holder.post_description.setText(mdata.get(position).getDescription());


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class PostsViewHolder extends RecyclerView.ViewHolder {


        public CircleImageView owner_image;
        public TextView owner_name, post_time, post_description;
        public ImageView post_image;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            owner_image = itemView.findViewById(R.id.circle_owner_pic);
            owner_name = itemView.findViewById(R.id.owner_name);
            post_time = itemView.findViewById(R.id.post_time);
            post_description = itemView.findViewById(R.id.post_describtion);
            post_image = itemView.findViewById(R.id.post_image);


        }
    }


}

