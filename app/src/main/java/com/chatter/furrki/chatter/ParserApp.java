package com.chatter.furrki.chatter;

import com.parse.Parse;
import android.app.Application;

public class ParserApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                // if defined
                .clientKey("android")
                .server(getString(R.string.parse_server_url))
                .build()
        );
    }
}