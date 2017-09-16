package com.example.nishchay.suddenmovement;

import android.app.Application;
import android.content.Intent;

/**
 * Created by Nishchay on 16-09-2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Making the application run in background.
        startService(new Intent(this, BackgroundService.class));
    }

}
