package com.example.parstagram_java.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.parstagram_java.Adapters.CommentAdapter;
import com.example.parstagram_java.Comment;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment {

    private static final int RESULTS_PER_LOAD = 30;
    private static final String TAG = "CommentsFragment";
    Post post;
    RecyclerView rvComments;
    CommentAdapter adapter;
    List<Comment> comments;
    MenuItem backToFeedButton;
    Fragment prevFragment;

    ImageView sendMessageButton;
    EditText etCommentMessage;

    public CommentsFragment() {}

    public CommentsFragment(Post post, Fragment prevFragment){
        this.post = post;
        this.prevFragment = prevFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        backToFeedButton = menu.findItem(R.id.backToFeed);
        backToFeedButton.setVisible(true);
        backToFeedButton.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.backToFeed){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.flContainer, prevFragment).commit();
        }
        super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView commentingProfilePhoto = view.findViewById(R.id.commentingProfilePhoto);
        ParseUser currentUser = ParseUser.getCurrentUser();
        String profileImgUrl = Post.getProfileUrl(currentUser.getUsername());
        if (profileImgUrl != null){
            Glide.with(view.getContext()).load(profileImgUrl).circleCrop().into(commentingProfilePhoto);
        }

        rvComments = view.findViewById(R.id.rvComments);
        comments = new ArrayList<>();
        adapter = new CommentAdapter(getContext(), comments);
        sendMessageButton = view.findViewById(R.id.sendCommentButton);
        etCommentMessage = view.findViewById(R.id.etCommentMessage);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = etCommentMessage.getText().toString();
                Log.d(TAG, messageText);
                if (messageText.isEmpty()){
                    Toast.makeText(getContext(),
                            "Error: comment cannot be empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                postComment(messageText);
            }
        });

        rvComments.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);

        queryComments(0, RESULTS_PER_LOAD, true);


    }

    private void postComment(String messageText) {
        Comment comment = new Comment();
        comment.setMessage(messageText);
        comment.setAuthor(ParseUser.getCurrentUser().getUsername());

        comment.saveInBackground(new SaveCallback() {
             @Override
             public void done(ParseException e) {
                 if (e != null){
                     Toast.makeText(getContext(), "Error saving comment: " + e.getMessage(),
                             Toast.LENGTH_LONG).show();
                     return;
                 }
                 ParseRelation<Comment> postComments = post.getComments();
                 postComments.add(comment);
                 post.saveInBackground(new SaveCallback() {
                     @Override
                     public void done(ParseException e) {
                         if (e != null){
                             Toast.makeText(getContext(), "Error updating post with comment: " + e.getMessage(),
                                     Toast.LENGTH_LONG).show();
                             return;
                         }
                         adapter.notifyDataSetChanged();
                         etCommentMessage.setText("");
                     }
                 });
             }
         });

    }

    private void queryComments(int numResultsToSkip, int resultsPerLoad, boolean notifyEntireDataSet) {
        ParseRelation<Comment> postComments = post.getComments();

        ParseQuery<Comment> query = postComments.getQuery();

        query.setSkip(numResultsToSkip);
        query.setLimit(resultsPerLoad);

        query.addAscendingOrder(Comment.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error querying for comments", e);
                    return;
                }
                comments.addAll(objects);
                if (notifyEntireDataSet){
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyItemRangeInserted(numResultsToSkip, resultsPerLoad);
                }
            }
        });
    }
}