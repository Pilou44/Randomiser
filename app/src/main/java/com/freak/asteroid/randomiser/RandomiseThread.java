package com.freak.asteroid.randomiser;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;

public class RandomiseThread extends Thread {

    private static final String TAG = "RandomiseThread";
    private static final boolean DEBUG = true;
    private final File mStartFolder;
    private final boolean mRandom;
    private final String mSuffix;
    private RandomThreadListener listener;

    public RandomiseThread(File startFolder, boolean random, String suffix) {
        mStartFolder = startFolder;
        mRandom = random;
        mSuffix = suffix;
    }

    @Override
    public void run() {
        if (mStartFolder != null && mStartFolder.isDirectory()) {
            if (listener != null) {
                listener.notifyStartOfParsing();
            }
            parse(mStartFolder);
            if (listener != null) {
                listener.notifyEndOfParsing();
            }
        }
        else if (listener != null) {
            listener.notifyRootError();
        }
    }

    public void setListener(RandomThreadListener listener) {
        this.listener = listener;
    }

    private Vector<File> parse(File folder) {
        if (DEBUG)
            Log.d(TAG, "Parse " + folder.getName());

        Vector<File> files = new Vector<>();
        File[] filesTab = folder.listFiles();
        sortFiles(filesTab);

        for (File aFilesTab : filesTab) {
            if (aFilesTab.getName().endsWith("mp3")) {
                files.add(aFilesTab);
            } else if (aFilesTab.isDirectory()) {

                if (DEBUG)
                    Log.d(TAG, "Found directory " + aFilesTab.getName());

                Vector<File> subFiles = parse(aFilesTab);

                if (DEBUG) {
                    Log.d(TAG, aFilesTab.getName() + " contains " + subFiles.size() + " files");
                    Log.d(TAG, "Vector size was " + files.size());
                }

                files.addAll(subFiles);

                if (DEBUG)
                    Log.d(TAG, "Vector size is now " + files.size());
            }
        }
        
        File playlist = new File(mStartFolder, folder.getName() + mSuffix + ".m3u");
        randomiseAndWrite((Vector<File>) files.clone(), playlist);


        if (DEBUG)
            Log.d(TAG, "Done parsing " + folder.getName() + " with size " + files.size());

        return files;
    }

    private static void sortFiles(File[] tableau) {
        int longueur = tableau.length;
        File tampon;
        boolean permut;

        do {
            // hypothèse : le tableau est trié
            permut = false;
            for (int i = 0; i < longueur - 1; i++) {
                // Teste si 2 éléments successifs sont dans le bon ordre ou non
                if (tableau[i].getName().compareToIgnoreCase(tableau[i + 1].getName()) > 0) {
                    // s'ils ne le sont pas, on échange leurs positions
                    tampon = tableau[i];
                    tableau[i] = tableau[i + 1];
                    tableau[i + 1] = tampon;
                    permut = true;
                }
            }
        } while (permut);
    }
    private void randomiseAndWrite(Vector<File> files, File playlist) {
        String[] filesNames = new String[files.size()];
        Random random = new Random();
        int index;

        for (int i = 0; i < filesNames.length; i++) {
            if (mRandom) {
                if (files.size() > 1) {
                    index = random.nextInt() % files.size();
                    if (index < 0)
                        index *= -1;
                } else {
                    index = 0;
                }
            }
            else {
                index = 0;
            }

            if (DEBUG)
                Log.d(TAG, "i=" + i + ", size=" + files.size() + ", index=" + index);

            filesNames[i] = files.get(index).getAbsolutePath();
            files.remove(index);
        }

        if (!playlist.exists()) {
            try {
                playlist.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PrintWriter writer;
        try {
            writer = new PrintWriter(playlist, "UTF-8");
            for (String filesName : filesNames) {
                writer.println(filesName);
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
