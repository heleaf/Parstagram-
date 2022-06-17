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
    public static final String KEY_POST = "post";
    public static final String KEY_CREATED_AT = "createdAt";

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser author){
        put(KEY_AUTHOR, author);
    }

    public String getMessage(){
        return getString(KEY_MESSAGE);
    }

    public void setMessage(String message){
        put(KEY_MESSAGE, message);
    }

    public Post getPost(){
        return (Post)getParseObject(KEY_POST);
    }

    public void setPost(Post post){
        put(KEY_POST, post);
    }

    public String getRelativeTimeCreated() {
        Date createdAt = getCreatedAt();
        String timeAgo = Post.calculateTimeAgo(createdAt);
        return timeAgo;
    }

    public String getProfileImgUrl() { return Post.getProfileUrl(getAuthor().getUsername()); }

}
