package com.example.parstagram_java.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parstagram_java.R;

public class PostDetail extends Fragment {
    TextView username;
    ImageView postPhoto;
    ImageView profilePhoto;
    TextView description;
    TextView relativeTimeStamp;

    public PostDetail() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.timeline_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.timelineUsername);
        postPhoto = view.findViewById(R.id.timelinePostPhoto);
        profilePhoto = view.findViewById(R.id.timelineProfilePhoto);
        description = view.findViewById(R.id.timelineDescription);
        relativeTimeStamp = view.findViewById(R.id.timelineRelativeTimeStamp);
    }
}