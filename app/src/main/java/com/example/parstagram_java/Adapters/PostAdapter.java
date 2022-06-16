package com.example.parstagram_java.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram_java.MainActivity;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.ParseFile;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<Post> posts;
    // Define listener member variable
    private OnItemClickListener listener;

    public PostAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return a new viewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_item, parent, false);
        return new ViewHolder(view, listener);
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

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView postPhoto;
        ImageView profilePhoto;
        TextView description;
        TextView relativeTimeStamp;

        public ViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);
            username = itemView.findViewById(R.id.timelineUsername);
            postPhoto = itemView.findViewById(R.id.timelinePostPhoto);
            profilePhoto = itemView.findViewById(R.id.timelineProfilePhoto);
            description = itemView.findViewById(R.id.timelineDescription);
            relativeTimeStamp = itemView.findViewById(R.id.timelineRelativeTimeStamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
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

            String profileImgUrl = post.getProfileImgUrl();
            if (profileImgUrl != null){
                Glide.with(context).load(profileImgUrl).circleCrop().into(profilePhoto);
            }

        }

    }
}
