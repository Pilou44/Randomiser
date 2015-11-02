package com.freak.asteroid.randomiser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {

    @Override
	public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean(context.getString(R.string.key_random_on_boot), false))
        {
            Intent serviceIntent = new Intent(context, RandomService.class);
            context.startService(serviceIntent);
        }
        if (prefs.getBoolean(context.getString(R.string.key_alpha_on_boot), false))
        {
            Intent serviceIntent = new Intent(context, AlphaService.class);
            context.startService(serviceIntent);
        }
	}

}
