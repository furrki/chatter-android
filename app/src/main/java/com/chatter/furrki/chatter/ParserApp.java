package com.chatter.furrki.chatter;

import com.chatter.furrki.chatter.Models.Message;
import com.chatter.furrki.chatter.Models.Room;
import com.chatter.furrki.chatter.Models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParserApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Room.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                // if defined
                .clientKey("android")
                .server(getString(R.string.parse_server_url))
                .build()
        );
    }
}