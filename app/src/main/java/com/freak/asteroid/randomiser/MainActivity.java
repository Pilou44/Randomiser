package com.freak.asteroid.randomiser;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener, RandomThreadListener {

    private static final int MESSAGE_ERROR = 0;
    private static final int MESSAGE_RUNNING = 1;
    private static final int MESSAGE_FINISHED = 2;
    private static final int MESSAGE_TESTING = 3;
    private static final int MESSAGE_DELETING = 4;

    private TextView randomText;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton randomButton = (ImageButton) findViewById(R.id.random_button);
        randomButton.setOnClickListener(this);

        ImageButton linearButton = (ImageButton) findViewById(R.id.linear_button);
        linearButton.setOnClickListener(this);

        randomText = (TextView) findViewById(R.id.random_text);

        handler = new Handler(new IncomingHandlerCallback());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        RandomiseThread randomiser;
        File dest = new File(Environment.getExternalStorageDirectory() + "/Playlists");
        File root = new File(Environment.getExternalStorageDirectory() + "/Musique");

        if (!dest.exists())
            dest.mkdir();
        if (view.getId() == R.id.random_button) {
            randomiser = new RandomiseThread(this, root, true, "_Random", dest);
            randomiser.setListener(this);
            randomiser.start();
        }
        else if (view.getId() == R.id.linear_button) {
            randomiser = new RandomiseThread(this, root, false, "", dest);
            randomiser.setListener(this);
            randomiser.start();
        }
    }

    @Override
    public void notifyEndOfParsing() {
        handler.sendEmptyMessage(MESSAGE_FINISHED);
    }

    @Override
    public void notifyRootError() {
        handler.sendEmptyMessage(MESSAGE_ERROR);
    }

    @Override
    public void notifyStartOfParsing() {
        handler.sendEmptyMessage(MESSAGE_RUNNING);
    }

    @Override
    public void notifyTestExistingFiles() {
        handler.sendEmptyMessage(MESSAGE_TESTING);
    }

    @Override
    public void notifyDeletingExistingFiles() {
        handler.sendEmptyMessage(MESSAGE_DELETING);
    }

    class IncomingHandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == MESSAGE_ERROR) {
                updateText(getString(R.string.thread_error));
            }
            else if (msg.what == MESSAGE_FINISHED) {
                updateText(getString(R.string.thread_finished));
            }
            else if (msg.what == MESSAGE_RUNNING) {
                updateText(getString(R.string.thread_running));
            }
            else if (msg.what == MESSAGE_TESTING) {
                updateText(getString(R.string.thread_testing));
            }
            else if (msg.what == MESSAGE_DELETING) {
                updateText(getString(R.string.thread_deleting));
            }

            return true;
        }
    }

    private void updateText(String text) {
        randomText.setText(text);
    }
}
