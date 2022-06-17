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
import android.widget.ImageView;

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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment {

    private static final int RESULTS_PER_LOAD = 10;
    private static final String TAG = "CommentsFragment";
    Post post;
    RecyclerView rvComments;
    CommentAdapter adapter;
    List<Comment> comments;
    MenuItem backToFeedButton;
    Fragment prevFragment;

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

        rvComments.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);

        queryComments(0, RESULTS_PER_LOAD, true);


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
                    // error
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