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

    // Define listener member variable
    private OnItemClickListener postListener;

    public ProfilePostAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_profile_item, parent, false);
        return new ViewHolder(view, postListener);
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
        this.postListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final int MEDIA_SIZE = (Resources.getSystem().getDisplayMetrics().widthPixels / 3);

        ImageView postPhoto;

        public ViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);
            postPhoto = itemView.findViewById(R.id.profileGridSquarePhoto);

            ViewGroup.LayoutParams params = postPhoto.getLayoutParams();
            params.height = MEDIA_SIZE;
            params.width = MEDIA_SIZE;
            postPhoto.setLayoutParams(params);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener == null) return;
                    clickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }

        public void bind(Post post) {
            ParseFile postImg = post.getImage();
            if (postImg != null){
                Glide.with(context).load(postImg.getUrl()).into(postPhoto);
            }
        }
    }


}


