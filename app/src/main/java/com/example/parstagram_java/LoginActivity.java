package com.example.parstagram_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public final class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    @Nullable
    EditText mUserName;
    @Nullable
    EditText mPassword;
    @Nullable
    Button mLoginButton;
    @Nullable
    Button mSignUpButton;

    @Nullable
    public final EditText getMUserName() {
        return this.mUserName;
    }

    public final void setMUserName(@Nullable EditText var1) {
        this.mUserName = var1;
    }

    @Nullable
    public final EditText getMPassword() {
        return this.mPassword;
    }

    public final void setMPassword(@Nullable EditText var1) {
        this.mPassword = var1;
    }

    @Nullable
    public final Button getMLoginButton() {
        return this.mLoginButton;
    }

    public final void setMLoginButton(@Nullable Button var1) {
        this.mLoginButton = var1;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentSessionToken() != null){
            goMainActivity();
        }

        this.mUserName = (EditText)this.findViewById(R.id.etUsername);
        this.mPassword = (EditText)this.findViewById(R.id.etPassword);
        this.mLoginButton = (Button)this.findViewById(R.id.logInButton);
        this.mSignUpButton = (Button)this.findViewById(R.id.signUpButton);

        if (mLoginButton == null || mUserName == null || mPassword == null || mSignUpButton == null)
            // make a toast here
            return;

        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameText = mUserName.getText().toString();
                String passwordText = mPassword.getText().toString();
                loginUser(userNameText, passwordText);
            }
        });

        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }



    private void loginUser(String usernameText, String passwordText){
        Log.d(TAG, "attempting to log in");

        ParseUser.logInInBackground(usernameText, passwordText, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    // failed
                    Log.e(TAG, "failed to log in", e);
                    Toast.makeText(LoginActivity.this,
                            "Issue with login: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this,
                        "Success!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
