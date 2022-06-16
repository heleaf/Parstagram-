package com.example.parstagram_java.Viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram_java.Adapters.PostAdapter;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.ParseFile;

public class PostAdapterViewholder extends RecyclerView.ViewHolder {
    TextView username;
    ImageView postPhoto;
    ImageView profilePhoto;
    TextView description;
    TextView relativeTimeStamp;
    Context context;
    OnItemClickListener itemClickListener;
    OnProfilePhotoClickListener profilePhotoClickListener;

    interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    interface OnProfilePhotoClickListener {
        void onProfilePhotoClick(View itemView, int position);
    }

    public void setOnProfilePhotoClickListener(OnProfilePhotoClickListener listener){
        this.profilePhotoClickListener = listener;
    }

    public PostAdapterViewholder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.timelineUsername);
        postPhoto = itemView.findViewById(R.id.timelinePostPhoto);
        profilePhoto = itemView.findViewById(R.id.timelineProfilePhoto);
        description = itemView.findViewById(R.id.timelineDescription);
        relativeTimeStamp = itemView.findViewById(R.id.timelineRelativeTimeStamp);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener == null) return;
                itemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profilePhotoClickListener == null) return;
                profilePhotoClickListener.onProfilePhotoClick(itemView, getAdapterPosition());
            }
        });
    }

    public void bind(Post post){
        username.setText(post.getUser().getUsername());

        ParseFile postImg = post.getImage();

        if (postImg != null){
            Glide.with(context).load(postImg.getUrl()).into(postPhoto);
        }

        description.setText(post.getDescription());

        relativeTimeStamp.setText(post.getRelativeTimeCreated());

        String profileImgUrl = post.getProfileImgUrl();
        if (profileImgUrl != null){
            Glide.with(context).load(profileImgUrl).circleCrop().into(profilePhoto);
        }

    }
}
