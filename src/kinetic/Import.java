package kinetic;

import java.io.File;
import java.io.IOException;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Dylan White
 * 
 * Class: Import
 * 
 * Objective: Provides functionality for 'import' menu items. Validates that file is
 * mp3 and imports to .Kinetic/tracks directory using Apache Commons FileUtils library.
 * Also, uses mp3agic library to verify file is mp3.
 * 
 * Copyright 2015 Dylan White (GPL)
 * 
 */

public class Import {
       
    private final Files files;   
    
    public Import(){        
        this.files = new Files();
    }
    
    /**
     * 
     * Checks if file is mp3 and doesn't already exist in .Kinetic/tracks dir.
     * Imports mp3 file to .Kinetic/tracks dir.
     * 
     * Accessed by MainScreenCtrl controller class, to provide functionality to 
     * 'import tune' tree item listener.
     * 
     */
    
    public void importFile(){        
        // importSourceFile (Files) set to JavaFX FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import track");
        files.setImportSourceFile(fileChooser.showOpenDialog(new Stage()));
                
        // file validated, copied to .Kinetic/tracks, and tableview re-populated
        try {
            if (checkIfMp3(files.getImportSourceFile().getPath()) && !checkIfExists(files.getImportSourceFile())){
                
                FileUtils.copyFileToDirectory(files.getImportSourceFile(), files.getTracksDir());                
                
            }
            
            else {                
                System.out.println("File is not an mp3. Or it already exists.");                
            }
        }
        
        catch (IOException | NullPointerException anException){
            System.out.println("importTrack error: " + anException);
        }        
    }
    
    /**
     * 
     * Checks if files are mp3 and doesn't already exist in .Kinetic/tracks dir.
     * Imports mp3 files to .Kinetic/tracks dir.
     * 
     * Accessed by MainScreenCtrl controller class, to provide functionality to 
     * 'import album' tree item listeners.
     * 
     */
    
    public void importDir(){  
        // importSourceFile (Files) set to JavaFX DirectoryChooser object
        DirectoryChooser dirChooser = new DirectoryChooser();        
        dirChooser.setTitle("Import Album");
        files.setImportSourceFile(dirChooser.showDialog(new Stage()));
        
        // files validated, copied to .Kinetic/tracks, and tableview re-populated
        try {
            for (File inputFile : files.getImportSourceFile().listFiles()){
                
                if (checkIfMp3(inputFile.getPath()) && !checkIfExists(inputFile)){
                    
                    FileUtils.copyFileToDirectory(inputFile, files.getTracksDir());              
                    
                }
                
                else {
                    System.out.println("File is not an mp3. Or it already exists.");
                }
            }            
        }
        catch (NullPointerException | IOException anException){
            System.out.println("importTrack error: " + anException);
        }        
    }
    
    /**
     * 
     * Helper method - Checks if files are mp3 format using mp3agic library.
     * 
     * Accessed by importDir() and importFile() above.
     * 
     * @return boolean
     * 
     * @param String sourceFilePath
     * 
     */
    
    private static boolean checkIfMp3(String sourceFilePath){
        boolean isMp3 = false;
        
        try {
            Mp3File mp3file = new Mp3File(sourceFilePath);
            
            // has imported file got either Id3v1 tag or Id3v2 tag?
            if (mp3file.hasId3v1Tag() || mp3file.hasId3v2Tag()) {
                
                isMp3 = true;
                
            }            
        }
        
        catch (IOException | UnsupportedTagException | InvalidDataException anException){
            System.out.println("checkifmp3 error: " + anException);
        }
        
        return isMp3;
    }
    
    /**
     * 
     * Helper method - Checks if file exists at .Kinetic/tracks
     * 
     * Accessed by importDir() and importFile() above.
     * 
     * @return boolean
     * 
     * @param File sourceFile
     * 
     */
    
    private boolean checkIfExists(File sourceFile){
        boolean exists = false;
        
        try {
            for(File inputFile : files.getTracksDir().listFiles()){
                
               if (inputFile.hashCode() == sourceFile.hashCode()){
                   
                   exists = true;
                   
               }
               
            }            
        }
        
        catch (NullPointerException anException){
            System.out.println("Error: " + anException);
        }
        
        return exists;
    }      
}
