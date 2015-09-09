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
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;

public class MainActivity extends Activity implements View.OnClickListener, RandomThreadListener {

    private File root;
    private TextView randomText;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = new File(Environment.getExternalStorageDirectory() + "/Musique");

        Button randomButton = (Button) findViewById(R.id.random_button);
        randomButton.setOnClickListener(this);

        randomText = (TextView) findViewById(R.id.random_text);

        handler = new Handler(new IncomingHandlerCallback());
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
            randomiser.setListener(this);
            randomiser.start();
        }
    }

    @Override
    public void notifyEndOfParsing() {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void notifyRootError() {
        handler.sendEmptyMessage(0);
    }

    @Override
    public void notifyStartOfParsing() {
        handler.sendEmptyMessage(2);
    }

    class IncomingHandlerCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 0) {
                updateText("Error with Root");
            }
            else if (msg.what == 1) {
                updateText("Done");
            }
            else if (msg.what == 2) {
                updateText("Start");
            }

            return true;
        }
    }

    /*static class ThreadHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        ThreadHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            MainActivity activity = mActivity.get();
            if (activity != null) {
               if (msg.what == 0) {
                   activity.updateText("Error with Root");
               }
               else if (msg.what == 1) {
                   activity.updateText("Done");
               }
               else if (msg.what == 2) {
                   activity.updateText("Start");
               }
            }
        }
    }*/

    private void updateText(String text) {
        randomText.setText(text);
    }
}
