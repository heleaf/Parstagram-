package com.example.parstagram_java.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.ParseFile;

import java.util.List;

public class GridViewPostAdapter extends RecyclerView.Adapter<GridViewPostAdapter.ViewHolder>{

    Context context;
    List<Post> posts;

    public GridViewPostAdapter(Context c, List<Post> p){
        context = c;
        posts = p;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_item, parent, false);
        return new GridViewPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView postPhoto;
        ImageView profilePhoto;
        TextView description;
        TextView relativeTimeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.timelineUsername);
            postPhoto = itemView.findViewById(R.id.timelinePostPhoto);
            profilePhoto = itemView.findViewById(R.id.timelineProfilePhoto);
            description = itemView.findViewById(R.id.timelineDescription);
            relativeTimeStamp = itemView.findViewById(R.id.timelineRelativeTimeStamp);
        }

        public void bind(Post post) {
            // just bind the image
            // bind it as a square

            ParseFile postImg = post.getImage();

            if (postImg != null){
                Glide.with(context).load(postImg.getUrl()).into(postPhoto);
                postPhoto.setVisibility(View.VISIBLE);
            }

            username.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            relativeTimeStamp.setVisibility(View.GONE);
            profilePhoto.setVisibility(View.GONE);

        }
    }

}
