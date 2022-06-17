package com.example.parstagram_java.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.parstagram_java.EndlessRecyclerViewScrollListener;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.Adapters.PostAdapter;
import com.example.parstagram_java.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {
    public static final String TAG = "TimelineFragment";
    public static final int RESULTS_PER_LOAD = 10;

    RecyclerView rvPosts;
    PostAdapter adapter;
    List<Post> posts;
    SwipeRefreshLayout swipeContainer;

    // Store a member variable for the listener
    EndlessRecyclerViewScrollListener scrollListener;

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

        @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem back = menu.findItem(R.id.backToFeed);
        back.setVisible(false);
        back.setEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        adapter = new PostAdapter(getContext(), posts);

        adapter.setOnItemClickListener(PostAdapter.getNewOnItemClickListener(posts,
                getActivity(), TimelineFragment.this));

        adapter.setOnProfilePhotoClickListener(PostAdapter.getNewOnProfilePhotoClickListener(
                posts, getActivity(), TimelineFragment.this
        ));

        adapter.setOnCommentClickListener(PostAdapter.getNewOnCommentClickListener(
                posts, getActivity(), TimelineFragment.this
        ));

        rvPosts.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                queryPosts(posts.size(), RESULTS_PER_LOAD, false);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.timelineSwipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                posts.clear();
                queryPosts(0, RESULTS_PER_LOAD, true);
                scrollListener.resetState();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryPosts(0, RESULTS_PER_LOAD, true);
    }

    protected void queryPosts(int numResultsToSkip, int numberOfResults, boolean notifyEntireDataSet
                              ){
        // get an object for querying posts
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // include data referred by user key
        query.include(Post.KEY_USER);

        query.setSkip(numResultsToSkip);
        query.setLimit(numberOfResults);

        query.addDescendingOrder(Post.KEY_CREATED_AT); // newest first

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null){
                    // error
                    Toast.makeText(getContext(),
                            "Error querying posts: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                posts.addAll(objects);
                if (notifyEntireDataSet){
                    adapter.notifyDataSetChanged();
                } else {
                    // notify just the range...
                    adapter.notifyItemRangeInserted(numResultsToSkip, numberOfResults);
                }
                swipeContainer.setRefreshing(false);

            }
        });

    }
}