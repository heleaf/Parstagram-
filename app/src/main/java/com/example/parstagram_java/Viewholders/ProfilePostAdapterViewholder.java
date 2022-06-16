package com.example.parstagram_java.Viewholders;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.parstagram_java.Post;
import com.parse.ParseFile;

public class ProfilePostAdapterViewholder extends PostAdapterViewholder {
    private static final String TAG = "ProfilePostAdapterViewholder";

    public ProfilePostAdapterViewholder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Post post) {
        super.bind(post);
//        ParseFile postImg = post.getImage();
//        if (postImg != null){
//            Glide.with(context).load(postImg.getUrl()).into(postPhoto);
//        }
        // hide everything else...
//        username.setVisibility(View.GONE);
        Log.d(TAG, "trying to bind " + post.getDescription());
        username.setVisibility(View.VISIBLE);
        description.setVisibility(View.GONE);
        relativeTimeStamp.setVisibility(View.GONE);
        profilePhoto.setVisibility(View.GONE);
    }
}
