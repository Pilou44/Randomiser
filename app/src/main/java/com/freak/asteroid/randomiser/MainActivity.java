package com.freak.asteroid.randomiser;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {

    private File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = new File(Environment.getExternalStorageDirectory() + "/Musique");

        ImageButton randomButton = (ImageButton) findViewById(R.id.random_button);
        randomButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*File root = new File(Environment.getExternalStorageDirectory() + "/Musique");
        RandomiseThread randomiser = new RandomiseThread(root);
        randomiser.start();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.random_button) {
            RandomiseThread randomiser = new RandomiseThread(root);
            randomiser.start();
        }
    }
}
