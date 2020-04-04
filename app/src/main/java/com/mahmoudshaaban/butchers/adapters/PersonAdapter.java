package com.mahmoudshaaban.butchers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.CategoriesModel;
import com.mahmoudshaaban.butchers.ui.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.myViewHolder> {

    Context mcontext;
    List<CategoriesModel> mdata;
    HomeFragment homeFragment;

    public PersonAdapter(Context mcontext, List<CategoriesModel> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.recycler_itm, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.textView.setText(mdata.get(position).getCategorie_Name());
        Picasso.get()
                .load(mdata.get(position).getCategorie_Image())
                .fit()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categorie_image);
            textView = itemView.findViewById(R.id.categorie_name);
        }
    }
}
