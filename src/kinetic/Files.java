package kinetic;

import java.io.File;

/**
 * 
 * @author Dylan White
 * 
 * Class: Files
 * 
 * Objective: Provides getter / setter methods for specific files used across the
 * application. The Files class is accessed via composition.
 * 
 * Copyright 2015 Dylan White (GPL)
 * 
 */

public class Files {
    
    private File importSourceFile; // mp3 file imported to $HOME/.Kinetic/tracks by user
    
    private final File tracksDir = new File // holds imported mp3 files
        (System.getProperty("user.home") + ".Kinetic/tracks");
    
    private final File favesDir = new File // holds favourite track files
        (System.getProperty("user.home") + ".Kinetic/faves");
    
    private final File playlistDir = new File // holds playlist track files
        (System.getProperty("user.home") + ".Kinetic/playlists");
    
    // Getters and setters
    
    public Files(){
        this.importSourceFile = null;       
    }

    public File getImportSourceFile() {
        return importSourceFile;
    }

    public void setImportSourceFile(File importSourceFile) {
        this.importSourceFile = importSourceFile;
    }

    public File getTracksDir() {
        return tracksDir;
    }

    public File getFavesDir() {
        return favesDir;
    }

    public File getPlaylistDir() {
        return playlistDir;
    }     
    
}
