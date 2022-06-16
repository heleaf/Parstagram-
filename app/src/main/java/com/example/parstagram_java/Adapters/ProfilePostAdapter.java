package com.example.parstagram_java.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.ParseFile;

import java.util.List;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ViewHolder> {
    Context context;
    List<Post> posts;

    public ProfilePostAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_profile_item, parent, false);
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
        final int MEDIA_HEIGHT = (Resources.getSystem().getDisplayMetrics().widthPixels / 3);

        ImageView postPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postPhoto = itemView.findViewById(R.id.profileGridSquarePhoto);

//            int width =
            ViewGroup.LayoutParams params = postPhoto.getLayoutParams();
            params.height = MEDIA_HEIGHT;
            params.width = MEDIA_HEIGHT;
            postPhoto.setLayoutParams(params);
        }

        public void bind(Post post) {
            ParseFile postImg = post.getImage();
            if (postImg != null){
                Glide.with(context).load(postImg.getUrl()).into(postPhoto);
            }
        }
    }


}


