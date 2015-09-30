package com.freak.asteroid.randomiser;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener, RandomThreadListener {

    private static final int MESSAGE_ERROR = 0;
    private static final int MESSAGE_RUNNING = 1;
    private static final int MESSAGE_FINISHED = 2;

    private File root;
    private TextView randomText;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = new File(Environment.getExternalStorageDirectory() + "/Musique");

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
        RandomiseThread randomiser;
        if (view.getId() == R.id.random_button) {
            randomiser = new RandomiseThread(root, true, " Random");
            randomiser.setListener(this);
            randomiser.start();
        }
        else if (view.getId() == R.id.linear_button) {
            randomiser = new RandomiseThread(root, false, "");
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

            return true;
        }
    }

    private void updateText(String text) {
        randomText.setText(text);
    }
}
