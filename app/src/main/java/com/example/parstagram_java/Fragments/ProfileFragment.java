package com.example.parstagram_java.Fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parstagram_java.Adapters.GridViewPostAdapter;
import com.example.parstagram_java.Adapters.PostAdapter;
import com.example.parstagram_java.EndlessRecyclerViewScrollListener;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends TimelineFragment {
    // GridViewPostAdapter gridAdapter;

    @Override
    protected void queryPosts(int numResultsToSkip, int numberOfResults, boolean notifyEntireDataSet) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setSkip(numResultsToSkip);
        query.setLimit(numberOfResults); // 20 latest

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



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        rvPosts = view.findViewById(R.id.rvPosts);
//        posts = new ArrayList<>();
//        gridAdapter = new GridViewPostAdapter(getContext(), posts);
//        rvPosts.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvPosts.setLayoutManager(gridLayoutManager);
//
//        // Retain an instance so that you can call `resetState()` for fresh searches
//        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                loadNextDataFromApi(page);
//            }
//        };
//        // Adds the scroll listener to RecyclerView
//        rvPosts.addOnScrollListener(scrollListener);
//
//        // Lookup the swipe container view
//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.timelineSwipeContainer);
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                posts.clear();
//                queryPosts(0, RESULTS_PER_LOAD, true);
//                scrollListener.resetState();
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//        queryPosts(0, RESULTS_PER_LOAD, true);
    }

    //    ImageView profilePhoto;
//    TextView username;
//    GridView photoGrid;
//
//    public ProfileFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        profilePhoto = view.findViewById(R.id.profileProfilePhoto);
//        username = view.findViewById(R.id.profileUsername);
//        photoGrid = view.findViewById(R.id.profileGridLayout);
//
//    }
}