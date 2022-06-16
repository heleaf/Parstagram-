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

import com.example.parstagram_java.Adapters.PostAdapter;
import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostDetail extends Fragment {

    private static final String TAG = "PostDetail";
    RecyclerView rvPost;
    PostAdapter adapter;
    List<Post> posts;
    SwipeRefreshLayout swipeContainer;
    Post post;
    Fragment prevFragment;

    public PostDetail() {
        // Required empty public constructor
    }

    public PostDetail(Post post, Fragment prevFragment){
        this.post = post;
        this.prevFragment = prevFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // timeline_item
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPost = view.findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        posts.add(post);
        adapter = new PostAdapter(getContext(), posts);
//        adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
//               @Override
//               public void onItemClick(View itemView, int position) {
//
//               }
//           });

        adapter.setOnProfilePhotoClickListener(new PostAdapter.OnProfilePhotoClickListener() {
            @Override
            public void onProfilePhotoClick(View itemView, int position) {
                Post post = posts.get(position);
                ParseUser user = post.getUser();
//                Fragment profileFragment = new ProfileFragment(user, true, PostDetail.this);
                Fragment profileFragment = new ProfileFragment(user,
                        true, PostDetail.this);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, profileFragment);
                fragmentTransaction.commit();
            }
        });

        rvPost.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPost.setLayoutManager(linearLayoutManager);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.timelineSwipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem back = menu.findItem(R.id.backToFeed);
        back.setVisible(true);
        back.setEnabled(true);
        Log.d(TAG, "setting back button?");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.backToFeed){
            // go back to the home fragment
            Log.d(TAG, "trying to go back");

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.flContainer, prevFragment).commit();

        }
        super.onOptionsItemSelected(item);
        return true;
    }
}