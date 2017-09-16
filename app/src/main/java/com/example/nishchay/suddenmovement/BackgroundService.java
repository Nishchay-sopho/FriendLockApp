package com.example.nishchay.suddenmovement;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Nishchay on 16-09-2017.
 */

public class BackgroundService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        return super.onStartCommand(intent, flags, startId);
    }
}
