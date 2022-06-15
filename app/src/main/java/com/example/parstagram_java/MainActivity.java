package com.example.parstagram_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public String photoFileName = "photo.jpg";

    @Nullable EditText mDescription;
    @Nullable ImageView mIvPicture;
    @Nullable Button mTakePictureButton;
    @Nullable Button mSubmitPostButton;
    File photoFile;

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

    @Override
    protected void onStart() {
        super.onStart();

        if (mTakePictureButton == null) return;
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open the camera app
                launchCamera();
            }
        });

        if (mSubmitPostButton == null) return;
        mSubmitPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDescription == null){
                    Log.d(TAG, "description button is null");
                    return;
                }
                String descriptionText = mDescription.getText().toString();
                if (descriptionText.isEmpty()){
                    Toast.makeText(MainActivity.this, "Description cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (photoFile == null || mIvPicture.getDrawable() == null){
                    Toast.makeText(MainActivity.this, "There is no image", Toast.LENGTH_LONG).show();
                    return;
                }
                ParseUser currUser = ParseUser.getCurrentUser();
                savePost(descriptionText, currUser, photoFile);
            }
        });


    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.hfyfileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // when IG picture call returns
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below (if i run out of memory)
                // Load the taken image into a preview
                mIvPicture.setImageBitmap(takenImage);
                mIvPicture.setVisibility(View.VISIBLE);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void savePost(String descriptionText, ParseUser currUser, File photoFile) {
        Post post = new Post();
        post.setDescription(descriptionText);
        post.setUser(currUser);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error saving post", e);
                    Toast.makeText(MainActivity.this, "Error saving post: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                if (mDescription == null) return;
                mDescription.setText(""); // clear the description
                Toast.makeText(MainActivity.this, "yay submitted post", Toast.LENGTH_LONG).show();
                mIvPicture.setImageResource(0);
            }
        });
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