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
import com.example.parstagram_java.Viewholders.ProfilePostAdapterViewholder;
import com.parse.ParseFile;

import java.util.List;

public class GridViewPostAdapter extends RecyclerView.Adapter<ProfilePostAdapterViewholder>{

    Context context;
    List<Post> posts;
    // Define listener member variable
//    private PostAdapter.OnItemClickListener listener;
//    private PostAdapter.OnProfilePhotoClickListener profilePhotoClickListener;

    public GridViewPostAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ProfilePostAdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_item, parent, false);
        return new ProfilePostAdapterViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostAdapterViewholder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
