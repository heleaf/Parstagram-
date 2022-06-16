package com.example.parstagram_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText userName;
    EditText password;
    EditText email;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userName = findViewById(R.id.etUsernameSignUp);
        password = findViewById(R.id.etPasswordSignUp);
        email = findViewById(R.id.etEmailAddressSignUp);
        signUpButton = findViewById(R.id.signUpSubmitButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // try to sign up and log the user in
                String usernameText = userName.getText().toString();
                String passwordText = password.getText().toString();
                String emailText = email.getText().toString();

                if (usernameText.isEmpty()){
                    Toast.makeText(SignUpActivity.this,
                            "Username cannot be empty",
                            Toast.LENGTH_LONG).show();
                    return;
                } if (passwordText.isEmpty()){
                    Toast.makeText(SignUpActivity.this,
                            "Password cannot be empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (emailText.isEmpty()){
                    Toast.makeText(SignUpActivity.this,
                            "Email address cannot be empty",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                signUp(usernameText, passwordText, emailText);
            }
        });
    }

    private void signUp(String usernameText, String passwordText, String emailText) {
        ParseUser user = new ParseUser();
        user.setUsername(usernameText);
        user.setPassword(passwordText);
        user.setEmail(emailText);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Toast.makeText(SignUpActivity.this,
                            "Error signing up: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignUpActivity.this,
                            "Successfully signed up",
                            Toast.LENGTH_LONG).show();
                    finish(); // return to the login intent
                }
            }
        });
    }

}