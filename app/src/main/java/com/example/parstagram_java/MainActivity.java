package com.example.parstagram_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.parstagram_java.Fragments.ComposeFragment;
import com.example.parstagram_java.Fragments.ProfileFragment;
import com.example.parstagram_java.Fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Nullable BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    Fragment homeFragment;
    Fragment composeFragment;
    Fragment currentUserProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        homeFragment = new TimelineFragment();
        composeFragment = new ComposeFragment();
        currentUserProfileFragment = new ProfileFragment(ParseUser.getCurrentUser(), false, null);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                // TODO: don't make a new fragment every time... store them
                switch (item.getItemId()){
                    case R.id.actionHome:
                        Log.d(TAG, "home button");
                        // nope
                        fragment = homeFragment; // TimelineFragment();
                        break;
                    case R.id.actionCreate:
                        Log.d(TAG, "create button");
                        fragment = composeFragment; // ComposeFragment();
                        break;
                    case R.id.actionProfile:
                        Log.d(TAG, "action button");
                        fragment = currentUserProfileFragment;// ProfileFragment(ParseUser.getCurrentUser(), false);
                        break;
                    default:
                        fragment = homeFragment; // ComposeFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // default tab selection
        bottomNavigationView.setSelectedItemId(R.id.actionHome);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.backToFeed).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.logOutFromMenu){
            // log out the user
            ParseUser.logOutInBackground();
            Log.d(TAG, String.valueOf(ParseUser.getCurrentUser()));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.backToFeed){
            Log.d(TAG, "wtf");
        }
        return super.onOptionsItemSelected(item);
    }
}