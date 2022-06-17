package com.example.parstagram_java.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.parstagram_java.Post;
import com.example.parstagram_java.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeFragment extends Fragment {

    public static final String TAG = "ComposeFragment";

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public String photoFileName = "photo.jpg";

    @Nullable EditText mDescription;
    @Nullable ImageView mIvPicture;
    @Nullable Button mTakePictureButton;
    @Nullable Button mSubmitPostButton;
    @Nullable File photoFile;
    @Nullable ProgressBar progressBar;

    public ComposeFragment() {
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
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        mDescription = view.findViewById(R.id.etPostDescription);
        mIvPicture = view.findViewById(R.id.postPicture);
        mTakePictureButton = view.findViewById(R.id.takePictureButton);
        mSubmitPostButton = view.findViewById(R.id.submitPostButton);
        progressBar = view.findViewById(R.id.pbLoading);

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
                    Toast.makeText(getContext(), "Description cannot be blank", Toast.LENGTH_LONG).show();
                    return;
                }
                if (photoFile == null || mIvPicture.getDrawable() == null){
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_LONG).show();
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
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.hfyfileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // when IG picture call returns
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below (if i run out of memory)
                // Load the taken image into a preview
                mIvPicture.setImageBitmap(takenImage);
                mIvPicture.setVisibility(View.VISIBLE);
                mDescription.setVisibility(View.VISIBLE);
                mSubmitPostButton.setVisibility(View.VISIBLE);
                mTakePictureButton.setText("Retake Picture");
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void savePost(String descriptionText, ParseUser currUser, File photoFile) {
        Post post = new Post();
        post.setDescription(descriptionText);
        post.setUser(currUser);
        post.setImage(new ParseFile(photoFile));
        post.setLikes(0);

        if (progressBar != null) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error saving post", e);
                    Toast.makeText(getContext(), "Error saving post: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    return;
                } else if (mDescription != null &&
                    mIvPicture != null && mTakePictureButton != null
                    && mSubmitPostButton != null) {
                    mDescription.setText(""); // clear the description
                    Toast.makeText(getContext(), "yay submitted post", Toast.LENGTH_LONG).show();
                    mIvPicture.setImageResource(0); // remove the image
                    mTakePictureButton.setText("Take Picture");
                    mSubmitPostButton.setVisibility(View.GONE);
                    mDescription.setVisibility(View.GONE);
                }

                if (progressBar != null){
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
                // switch to timeline fragment???
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.flContainer, new TimelineFragment()).commit();
            }
        });
    }

}