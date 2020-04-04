package com.mahmoudshaaban.butchers.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoudshaaban.butchers.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView circleImageView22;
    public TextView username , statues;
    public Button button;



    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);

        circleImageView22 = itemView.findViewById(R.id.circleImageView2);
        username = itemView.findViewById(R.id.name);
        statues = itemView.findViewById(R.id.stutus);
        button = itemView.findViewById(R.id.follow_btn);
    }
}
