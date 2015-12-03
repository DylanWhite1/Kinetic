package kinetic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Dylan White
 * 
 * Class: Library
 * 
 * Objective: Responsible for loading treeview (library) items to the main screen 
 * and listens to and handles relevant events. 
 * 
 */

public class Library {
    
    private final PlayBack playback;
    
    private final Files files;
    
    private final Populate populate;    
    
    public Library(){
        this.files = new Files();
        this.playback = new PlayBack();
        this.populate = new Populate();         
    }
    
    /**
     * 
     * loadLibrary - fills library treeview and handles functionality for 
     * tree items. These are: Library, Faves and Playlist items. Accessed in
     * initialize method of MainScreenCtrl controller class.
     * 
     * @param treeview
     * @param slider
     * @param currentTime 
     * @param songLabel 
     * 
     */
    
    public void loadLibrary(TreeView treeview, Slider slider, Label currentTime, Label songLabel) {
        //  set treeview to return value of setTreeView method
        treeview.setRoot(setTreeView());
        
        // set file default
        setBoolFile(true);
             
        //  listen for selected items
        treeview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() { 
               
            // set TableView selectionModel to singleton TableViewInstance tableview
            TableView.TableViewSelectionModel selectionModel = 
                TableViewInstance.getInstance().currentTableView().getTableview().getSelectionModel();
                
            @Override
            public void changed(ObservableValue observable, Object oldValue,
                    Object newValue) {
                
                // set selected tree item to new value object returned by listener
                TreeItem<String> selectedItem = (TreeItem<String>) newValue; 
                
                boolean maintable = true;
                
                try {
                    
                    // set function for tree items by selectedItem.getValue()
                    switch (selectedItem.getValue()) {
                        
                        // faves
                        case "Play faves":                            
                            
                            maintable = false;
                            populate.populateTable(files.getFavesDir().getPath());
                            playback.listenTableClick(slider, currentTime, songLabel,
                                    files.getFavesDir().getPath(), selectionModel);
                            playback.setDelete(files.getFavesDir());
                            break;
                        
                        // set table to fill with files from .Kinetic/tracks
                        case "Library":
                            
                            maintable = true;
                            populate.populateTable(files.getTracksDir().getPath());
                            playback.listenTableClick(slider, currentTime, songLabel,
                                    files.getTracksDir().getPath(), selectionModel);
                            playback.setDelete(files.getTracksDir());
                            break;
                        
                        // new playlist window - sets new playlist name in PlayList class
                        case "New playlist":
                                                       
                            Parent root;
                            root = FXMLLoader.load(getClass().getResource("fxml/newPlaylist.fxml"));
                            Stage stage = new Stage();
                            stage.setTitle("New Playlist");
                            stage.setScene(new Scene(root, 450, 135));
                            
                            // update treeview after 'new playlist' window closed
                            stage.setOnCloseRequest((final WindowEvent event) -> {
                                treeview.setRoot(setTreeView());
                                treeview.setEditable(true);
                            });     stage.show();
                            break;
                            
                        default:
                            break;
                            
                    }
                    
                    
                    
                    // set table to populate with mp3 files from .Kinetic/playlist/...
                    for (File plFile : files.getPlaylistDir().listFiles()){
                        rightClickTreeView(treeview, selectedItem, slider, currentTime, songLabel, selectionModel);
                        if (selectedItem.getValue().equals(plFile.getName())){ 
                            
                            maintable = false;
                            populate.populateTable(plFile.getPath());                    
                            playback.listenTableClick(slider, currentTime, songLabel, plFile.getPath(),
                                    selectionModel); 
                            playback.setDelete(plFile);  

                        }

                    }
                    
                    setBoolFile(maintable);
                
                }
                
                catch (NullPointerException | IOException anException){
                    System.out.println("loadlibrary error: " + anException);
                } 
                
            }             
            
        });
        
    }
    
    /**
     * 
     * Helper method - sets tree item values and returns the root item.
     * Accessed in the loadLibrary method above to set library treeview in main
     * screen.
     * 
     * @return boolean root
     * 
     */
    
    private TreeItem<String> setTreeView(){
        
        TreeItem<String> root, favourites, playlists, playlistNames;
        
        root = new TreeItem<>("Library"); 
        root.setExpanded(true); 
        
        favourites = makeBranch("Faves", root);
        makeBranch("Play faves", favourites);
        
        playlists = makeBranch("Playlists", root);
        makeBranch("New playlist", playlists);
        playlistNames = makeBranch("My PlayLists", playlists);
        
        for (File playlistDir : files.getPlaylistDir().listFiles()){
            
            if (playlistDir.isDirectory()){
                
                makeBranch(playlistDir.getName(), playlistNames);
                
            }
            
        }
        
        return root;
        
    }
    
    /**
     * 
     * Helper method - creates new children branches of treeview by setting them
     * to a parent branch.
     * Used in setTreeView method above.
     * 
     * @param title
     * @param parent
     * @return TreeItem<String> item
     * 
     */
    
    private TreeItem<String> makeBranch(String title, TreeItem<String> parent){        
        
        TreeItem<String> item = new TreeItem<>(title);        
        parent.getChildren().add(item);
        
        return item;
        
    }    
    
    /**
     * 
     * Writes file to hold boolean value - is this the main table?
     * 
     * @param boolean maintable
     * 
     */
    
    private void setBoolFile(boolean maintable){
        
        FileWriter writeMainTableBool = null;        
        
        try {
            
            writeMainTableBool = new FileWriter("mainTable.bool");
            writeMainTableBool.write(String.valueOf(maintable));
        
        }
        
        catch (IOException | NullPointerException anException){
            System.out.println("setboolfile error: " + anException);
        }
        
        finally {
            
            try {
                
                writeMainTableBool.close();
                
            }
            
            catch (IOException | NullPointerException anException){
                System.out.println("setboolfile finally error: " + anException);
            }
            
        }
        
    }
    
    /**
     * 
     * Right click mouse listener for treeview - delete playlist functionality.
     * 
     * @param treeview
     * @param selectedItem
     * @param slider
     * @param currentTime
     * @param selectionModel 
     */
    
    private void rightClickTreeView(TreeView treeview, TreeItem<String> selectedItem, 
            Slider slider, Label currentTime, Label songLabel, TableView.TableViewSelectionModel selectionModel){
        
        treeview.setOnMousePressed((MouseEvent event) -> {
            
            // file = playlist dir in .Kinetic.playlists
            File plFile = new File(files.getPlaylistDir().getPath() + "/" + selectedItem.getValue()); 
            
            if (event.isSecondaryButtonDown() && plFile.exists()){
                
                // set new context menu - item = delete + playlist name
                ContextMenu tvContextMenu = new ContextMenu();
                MenuItem playlistMenuItem = new MenuItem("Delete " + selectedItem.getValue());
                tvContextMenu.getItems().add(playlistMenuItem);
                treeview.setContextMenu(tvContextMenu); 
                
                playlistMenuItem.setOnAction((ActionEvent e) -> {
                        
                    // if treeview item selected = playlist dir name
                    if (selectedItem.getValue().equals(plFile.getName())){ 

                        try {
                            FileUtils.deleteDirectory(plFile);

                            // update treeview
                            treeview.setRoot(setTreeView());
                            treeview.setEditable(true);

                            // update tableview
                            populate.populateTable(files.getTracksDir().getPath());
                            playback.listenTableClick(slider, currentTime, songLabel,
                                    files.getTracksDir().getPath(), selectionModel);
                            playback.setDelete(files.getTracksDir());

                        } 

                        catch (IOException anException) {
                            Logger.getLogger(Library.class.getName()).log
                                    (Level.SEVERE, null, anException);
                        }

                    }             
                    
                });
                
            }
            
            else {
                
                // set context menu to null if playlist deleted
                treeview.setContextMenu(null);
                
            }
            
        });
        
    }
    
}
