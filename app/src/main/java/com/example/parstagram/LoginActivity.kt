package com.example.parstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseObject

class LoginActivity : AppCompatActivity() {
    var mUserName : EditText? = null;
    var mPassword : EditText? = null;
    var mLoginButton : Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize username, password, button
        mUserName = findViewById(R.id.etUsername);
        mPassword = findViewById(R.id.etPassword);
        mLoginButton = findViewById(R.id.logInButton);

//        val firstObject = ParseObject("FirstClass");
//        firstObject.put("message","Hey! First message from android. Parse is now connected")
//        firstObject.saveInBackground {
//            if (it != null) {
//                it.localizedMessage?.let { message -> Log.e("MainActivity", message) }
//            } else {
//                Log.d("MainActivity","Object saved.")
//            }
//        }
    }

    override fun onStart() {
        super.onStart();

        // set login button onclick listener
        val res = mLoginButton?.setOnClickListener(View.OnClickListener { Log.d("LoginActivity", "help") }); // ....
        if (res == null)
            Toast.makeText(this, "Failed to set login onClick listener",
                Toast.LENGTH_LONG).show()

    }
}