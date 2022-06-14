package com.example.parstagram_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Nullable EditText mDescription;
    @Nullable ImageView mIvPicture;
    @Nullable Button mTakePictureButton;
    @Nullable Button mSubmitPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDescription = findViewById(R.id.etPostDescription);
        mIvPicture = findViewById(R.id.postPicture);
        mTakePictureButton = findViewById(R.id.takePictureButton);
        mSubmitPostButton = findViewById(R.id.submitPostButton);
        
        queryPosts();

    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // all post objects from our database
                if (e != null){
                    // something has gone wrong
                    Log.e(TAG, "Issue with getting posts", e);
                }
                for (Post post : posts){
                    Log.i(TAG, "Post: " + post.getDescription() + " username: " + post.getUser().getUsername());
                }
            }
        });
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.logOutFromMenu){
            // log out the user
            ParseUser.logOutInBackground();
            Log.d("MainActivity", String.valueOf(ParseUser.getCurrentUser()));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}