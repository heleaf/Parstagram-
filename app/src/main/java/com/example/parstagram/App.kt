package com.example.parstagram
import android.app.Application
import android.util.Log
import com.parse.Parse;
import com.parse.ParseObject

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

        val firstObject = ParseObject("FirstClass");
        firstObject.put("message","Hey! First message from android. Parse is now connected")
        firstObject.saveInBackground {
            if (it != null) {
                it.localizedMessage?.let { message -> Log.e("MainActivity", message) }
            } else {
                Log.d("MainActivity","Object saved.")
            }
        }
    }
}