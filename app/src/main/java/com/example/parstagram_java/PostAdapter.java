package com.example.parstagram_java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public PostAdapter(Context c, List<Post> p){
        context = c;
        posts = p;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return a new viewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_item, parent, false);
        return new ViewHolder(view);
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
            // set the text and glide and stuff
            username.setText(post.getUser().getUsername());

            ParseFile postImg = post.getImage();

            if (postImg != null){
                Glide.with(context).load(postImg.getUrl()).into(postPhoto);
            }

            description.setText(post.getDescription());

             relativeTimeStamp.setText(post.getRelativeTimeCreated());

//            if (post.profileImgUrl != null){
//                Glide.with(context).load(post.profileImgUrl).circleCrop().into(profilePhoto);
//            }

        }
    }
}
