package com.freak.asteroid.randomiser;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity{

	@Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.settings);
    }

}
