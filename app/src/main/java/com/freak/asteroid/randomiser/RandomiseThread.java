package com.freak.asteroid.randomiser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;

public class RandomiseThread extends Thread {

    private final File mStartFolder;

    public RandomiseThread(File startFolder) {
        mStartFolder = startFolder;
    }

    @Override
    public void run() {
        if (mStartFolder != null && mStartFolder.isDirectory()) {
            parse(mStartFolder);
        }
    }

    private Vector<File> parse(File folder) {
        Vector<File> files = new Vector<>();
        File[] filesTab = folder.listFiles();

        for (File aFilesTab : filesTab) {
            if (aFilesTab.getName().endsWith("mp3")) {
                files.add(aFilesTab);
            } else if (aFilesTab.isDirectory()) {
                files.addAll(parse(aFilesTab));
            }
        }
        
        File playlist = new File(mStartFolder, folder.getName() + ".m3u");
        randomiseAndWrite(files, playlist);

        return files;
    }

    private void randomiseAndWrite(Vector<File> files, File playlist) {
        String[] filesNames = new String[files.size()];
        Random random = new Random();
        int index;

        for (int i = 0 ; i < files.size() ; i++) {
            if (files.size() > 1) {
                index = random.nextInt() % files.size();
            }
            else {
                index = 0;
            }
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
