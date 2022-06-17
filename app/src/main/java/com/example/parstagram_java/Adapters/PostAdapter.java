package com.example.parstagram_java.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram_java.Fragments.PostDetail;
import com.example.parstagram_java.Fragments.ProfileFragment;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private static final String TAG = "PostAdapter";
    Context context;
    List<Post> posts;
    // Define listener member variable
    private OnItemClickListener listener;
    private OnProfilePhotoClickListener profilePhotoClickListener;

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

    public static OnItemClickListener getNewOnItemClickListener(List<Post> posts, FragmentActivity activity,
                                                                Fragment prevFragment){
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (itemView == null) {
                    Log.d(TAG, "clicked null item");
                    return;
                }
                Post post = posts.get(position);
                Fragment fragment = new PostDetail(post, prevFragment);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, fragment);
                fragmentTransaction.commit();
            }
        };
    }

    public interface OnProfilePhotoClickListener {
        void onProfilePhotoClick(View itemView, int position);
    }

    public void setOnProfilePhotoClickListener(OnProfilePhotoClickListener profilePhotoClickListener) {
        this.profilePhotoClickListener = profilePhotoClickListener;
    }

    public static OnProfilePhotoClickListener
    getNewOnProfilePhotoClickListener(List<Post> posts, FragmentActivity activity, Fragment prevFragment){
        return new OnProfilePhotoClickListener() {
            @Override
            public void onProfilePhotoClick(View itemView, int position) {
                Post post = posts.get(position);
                ParseUser user = post.getUser();
                Fragment profileFragment = new ProfileFragment(user, true,
                        prevFragment);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, profileFragment);
                fragmentTransaction.commit();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView username2;
        ImageView postPhoto;
        ImageView profilePhoto;
        TextView description;
        TextView relativeTimeStamp;

        TextView numberOfLikes;
        ImageView likeButton;

        public ViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);
            username = itemView.findViewById(R.id.timelineUsername);
            username2 = itemView.findViewById(R.id.timelineUsername2);
            postPhoto = itemView.findViewById(R.id.timelinePostPhoto);
            profilePhoto = itemView.findViewById(R.id.timelineProfilePhoto);
            description = itemView.findViewById(R.id.timelineDescription);
            relativeTimeStamp = itemView.findViewById(R.id.timelineRelativeTimeStamp);

            numberOfLikes = itemView.findViewById(R.id.numberLikes);
            likeButton = itemView.findViewById(R.id.heartButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener == null) return;
                    clickListener.onItemClick(itemView, getAdapterPosition());
                }
            });

            View.OnClickListener toProfilePhoto = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (profilePhotoClickListener == null) return;
                    profilePhotoClickListener.onProfilePhotoClick(itemView, getAdapterPosition());
                }
            };

            profilePhoto.setOnClickListener(toProfilePhoto);
            username.setOnClickListener(toProfilePhoto);
            username2.setOnClickListener(toProfilePhoto);

        }

        public void bind(Post post) {
            // set the text and glide and stuff
            String usernameText = post.getUser().getUsername();
            username.setText(usernameText);
            username2.setText(usernameText);

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

            likeButton.setOnClickListener(getlikeButtonOnClickListener(likeButton, numberOfLikes, post));
            setNumberLikesAndLikeButton(context, likeButton, numberOfLikes, post);
        }

        // set the number of likes based on the database data
        void setNumberLikesAndLikeButton(Context context, ImageView likeButton, TextView numberOfLikes, Post post){
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseRelation<ParseUser> usersWhoLikedRelation = post.getUsersWhoLikedRelation();
            ParseQuery<ParseUser> queryOnLikedUsers = usersWhoLikedRelation.getQuery();
            queryOnLikedUsers.whereEqualTo("username", currentUser.getUsername());
            queryOnLikedUsers.findInBackground(new FindCallback<ParseUser>() {
               @Override
               public void done(List<ParseUser> objects, ParseException e) {
                   if (e != null) {
                       Toast.makeText(context,
                               "Failed to like post: " + e.getMessage(),
                               Toast.LENGTH_LONG).show();
                       return;
                   }
                   boolean postIsLiked = objects.size() > 0;
                   int likeButtonIconId = postIsLiked ? R.drawable.ufi_heart_active
                           : R.drawable.ufi_heart_icon;
                   likeButton.setImageResource(likeButtonIconId);
                   Number newLikes = post.getLikes() == null ? 0 : post.getLikes();
                   numberOfLikes.setText(String.format("%s likes", newLikes));
               }
           });
        }

        View.OnClickListener getlikeButtonOnClickListener(ImageView likeButton, TextView numberOfLikes, Post post) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    ParseRelation<ParseUser> usersWhoLikedRelation = post.getUsersWhoLikedRelation();
                    ParseQuery<ParseUser> queryOnLikedUsers = usersWhoLikedRelation.getQuery();
                    queryOnLikedUsers.whereEqualTo("username", currentUser.getUsername());
                    queryOnLikedUsers.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (e != null){
                                Toast.makeText(v.getContext(),
                                        "Failed to like post: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.d(TAG, String.valueOf(objects.size()));
                            boolean postIsAlreadyLiked = objects.size() > 0;
                            adjustPostLikesAndNumberLikes(postIsAlreadyLiked, post, likeButton,
                                    numberOfLikes);
                        }
                    });
                }
            };
        }

        void adjustPostLikesAndNumberLikes(boolean postIsAlreadyLiked,
                                           Post post, ImageView likeButton,
                                           TextView numberOfLikes){

            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseRelation<ParseUser> usersWhoLikedRelation = post.getUsersWhoLikedRelation();
            Number numberLikesOnPost = post.getLikes() == null ? 0 : post.getLikes();

            // update post data in the database
            if (postIsAlreadyLiked){
                // decrease the likes on the post by 1
                post.setLikes(numberLikesOnPost.intValue() - 1);
                // make it so that the user no longer likes the post
                usersWhoLikedRelation.remove(currentUser);
            } else {
                // increase the likes on the post by 1
                post.setLikes(numberLikesOnPost.intValue() + 1);
                // make it so that the user likes the post
                usersWhoLikedRelation.add(currentUser);
            }

            // save to the database
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null){
                        Log.d(TAG, "Error saving likes on post: " + e.getMessage());
                        return;
                    }
                    // update the UI
                    int likeButtonIconId = postIsAlreadyLiked ? R.drawable.ufi_heart_icon
                            : R.drawable.ufi_heart_active;
                    likeButton.setImageResource(likeButtonIconId);
                    Number newLikes = post.getLikes() == null ? 0 : post.getLikes();
                    numberOfLikes.setText(String.format("%s likes", newLikes));
                }
            });

        }

    }
}
