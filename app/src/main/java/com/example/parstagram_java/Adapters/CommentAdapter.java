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
import com.example.parstagram_java.Comment;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView commentUsername;
        ImageView commentAuthorProfilePhoto;
        TextView commentDescription;
        TextView relativeTimeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentAuthorProfilePhoto = itemView.findViewById(R.id.commentProfilePhoto);
            commentDescription = itemView.findViewById(R.id.commentDescription);
            relativeTimeStamp = itemView.findViewById(R.id.commentTimeStamp);
            commentUsername = itemView.findViewById(R.id.commentUsername);
        }

        public void bind(Comment comment) {
            String commentAuthorUsername = comment.getAuthor();
            commentUsername.setText(commentAuthorUsername);

            String commentDescriptionText = comment.getMessage();
            commentDescription.setText(commentDescriptionText);

            String authorProfilePhotoUrl = Post.getProfileUrl(comment.getAuthor());
            Glide.with(context).load(authorProfilePhotoUrl).circleCrop().into(commentAuthorProfilePhoto);

            String createdAt = comment.getRelativeTimeCreated();
            relativeTimeStamp.setText(createdAt);
        }
    }
}
