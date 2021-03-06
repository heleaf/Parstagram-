package com.example.parstagram_java.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram_java.Adapters.PostAdapter;
import com.example.parstagram_java.Adapters.ProfilePostAdapter;
import com.example.parstagram_java.EndlessRecyclerViewScrollListener;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public static final int RESULTS_PER_LOAD = 9;
    private static final String TAG = "NicerProfileFragment";

    RecyclerView rvProfilePosts;
    ProfilePostAdapter adapter;
    List<Post> posts;
    ParseUser profileUser;
    boolean showBackButton;
    MenuItem backToFeedButton;
    Fragment prevFragment;

    EndlessRecyclerViewScrollListener scrollListener;

    public ProfileFragment(){
        this.profileUser = ParseUser.getCurrentUser();
        this.showBackButton = false;
        this.prevFragment = null;
    }

    public ProfileFragment(ParseUser profileUser, boolean showBackButton, Fragment prevFragment){
        this.profileUser = profileUser == null ? ParseUser.getCurrentUser() : profileUser;
        this.showBackButton = showBackButton;
        this.prevFragment = prevFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(this.showBackButton);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        backToFeedButton = menu.findItem(R.id.backToFeed);
        backToFeedButton.setVisible(this.showBackButton);
        backToFeedButton.setEnabled(this.showBackButton);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvProfilePosts = view.findViewById(R.id.profileGridRv);
        posts = new ArrayList<>();
        adapter = new ProfilePostAdapter(getContext(), posts);

        // TODO: set on item click listener for the adapter?
        adapter.setOnItemClickListener(ProfilePostAdapter.getNewOnItemClickListener(posts, getActivity(),
                ProfileFragment.this));

        rvProfilePosts.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvProfilePosts.setLayoutManager(gridLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                queryProfilePosts(posts.size(), RESULTS_PER_LOAD, false);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvProfilePosts.addOnScrollListener(scrollListener);

        // TODO: Swipe container

        ImageView profileImg = view.findViewById(R.id.profileProfilePhoto);
        String profileImgUrl = Post.getProfileUrl(profileUser.getUsername());
        if (profileImgUrl != null){
            Glide.with(getContext()).load(profileImgUrl).circleCrop().into(profileImg);
        }
        TextView profileUsername = view.findViewById(R.id.profileUsername);
        profileUsername.setText(profileUser.getUsername());

        queryProfilePosts(0, RESULTS_PER_LOAD, true);
    }

    private void queryProfilePosts(int numResultsToSkip, int numberOfResults, boolean notifyEntireDataSet) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, profileUser);
        query.setSkip(numResultsToSkip);
        query.setLimit(numberOfResults); // lastest number of posts

        query.addDescendingOrder(Post.KEY_CREATED_AT); // newest first

        Log.d(TAG, "trying to query");
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
                Log.d(TAG, "finished querying...?");
                posts.addAll(objects);
                Log.d(TAG, "added");
                if (notifyEntireDataSet){
                    adapter.notifyDataSetChanged();
                } else {
                    // notify just the range...
                    adapter.notifyItemRangeInserted(numResultsToSkip, numberOfResults);
                }

                Log.d(TAG, "wtf");
//                swipeContainer.setRefreshing(false);
            }
        });
    }

}
