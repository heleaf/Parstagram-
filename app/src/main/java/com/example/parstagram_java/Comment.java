package com.example.parstagram_java;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CREATED_AT = "createdAt";

    public String getAuthor() {
        return getString(KEY_AUTHOR);
    }

    public void setAuthor(String author){
        put(KEY_AUTHOR, author);
    }

    public String getMessage(){
        return getString(KEY_MESSAGE);
    }

    public void setMessage(String message){
        put(KEY_MESSAGE, message);
    }

    public String getRelativeTimeCreated() {
        Date createdAt = getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        return timeAgo;
    }

    public String getProfileImgUrl() { return Post.getProfileUrl(getAuthor()); }

}
