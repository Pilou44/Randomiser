package com.freak.asteroid.randomiser;

public interface RandomThreadListener {

    void notifyEndOfParsing();

    void notifyRootError();

    void notifyStartOfParsing();

    void notifyTestExistingFiles();

    void notifyDeletingExistingFiles();
}
