package com.freak.asteroid.randomiser;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

public class AlphaService extends Service {
    public AlphaService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        RandomiseThread randomiser;
        File root = new File(Environment.getExternalStorageDirectory() + "/Musique");
        File dest = new File(Environment.getExternalStorageDirectory() + "/Playlists");
        if (!dest.exists())
            dest.mkdir();
        randomiser = new RandomiseThread(root, false, "", dest);
        randomiser.start();
    }
}
