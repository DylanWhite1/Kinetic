package kinetic;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * 
 * @author Dylan White
 * 
 * Class: PlayListCtrl controller
 * 
 * Objective: Controller class for newPlaylist. Sets playListName field as result from
 * user input and creates new directory in .Kinetic/playlists, by that name. 
 * 
 * Copyright 2015 Dylan White (GPL)
 * 
 */

public class PlayListCtrl {
    
    @FXML TextField playListName;
   
    @FXML Label playListNameStatus;
   
    @FXML Button setBt;
    
    private final Files files;
    
    public PlayListCtrl(){
         
        this.files = new Files();
        
    }
    
    /**
     * 
     * Handler for setBt - when pressed by user, playlist string is set and new 
     * dir created.
     * 
     * @param e 
     */
    
    @FXML
    private void setPlayListName(ActionEvent e){
        
        // create new playlist dir
        File newPLDir = new File(files.getPlaylistDir().getPath() + "/" + playListName.getText());
        newPLDir.mkdir();        
        
        if (newPLDir.exists()){
            
            // validates that new playlist has been created in 'new playlist' window
            playListNameStatus.setText("New playlist set.");
            
        }
        
    }
    
}
