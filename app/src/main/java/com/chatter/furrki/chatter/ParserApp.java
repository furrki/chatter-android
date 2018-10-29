package com.chatter.furrki.chatter;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParserApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Room.class);
        ParseObject.registerSubclass(User.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                // if defined
                .clientKey("android")
                .server(getString(R.string.parse_server_url))
                .build()
        );
    }
}