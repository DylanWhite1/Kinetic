package kinetic;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Dylan White
 * 
 * Class: PlayBack
 * 
 * Objective: Listens for mouse click events on the tableview, handles play, 
 * pause and stop button events, sets context menu for tableview, and provides
 * slider / progressbar functionality. Accessed by composition in other classes.
 * 
 */

public class PlayBack {
    
    @FXML private Duration totalTime;
    
    @FXML private static MediaPlayer mediaplayer; 
    
    private static int tableId;   
    
    private static ArrayList<Object> selectedRows = new ArrayList<>();
    
    private static String currentlyPlaying;
       
    private final Files files;
    
    private final Populate populate;
    
    public PlayBack(){        
        this.files = new Files();
        this.populate = new Populate();    
    }
    
    /**
     * 
     * Functionality for mouse double click event in tableview
     * Used in listenTableClick method below
     * 
     * @param slider
     * @param currentTime
     * @param filepath
     * @param selectionModel 
     */
    
    private void doubleClick(Slider slider, Label currentTime, Label songLabel, String filepath, 
            TableView.TableViewSelectionModel selectionModel, MouseEvent event){
        
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            
            // set table id to current selected id tablecolumn
            setTableId(selectionModel); 
            
            // play current track and continue playing the next in table            
            play(currentTime, songLabel, slider, filepath, selectionModel);            
            
        }
        
    }
    
    /**
     * 
     * Functionality for mouse single click event in tableview
     * Used in listenTableClick method below
     * 
     * @param slider
     * @param currentTime
     * @param filepath
     * @param selectionModel 
     * @param event 
     * 
     */
    
    private void singleClick(Slider slider, Label currentTime, Label songLabel, String filepath, 
            TableView.TableViewSelectionModel selectionModel, MouseEvent event){
        
        if (event.isPrimaryButtonDown()) { 
            // set selection mode of tableview to multiple
            TableViewInstance.getInstance().currentTableView().getTableview()
                    .getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            
            // set multiple track ids to a string arraylist - selectedRows
            setTableId(TableViewInstance.getInstance().currentTableView().getTableview()); 
            
            // handler - if left mouse button clicked listen for play button
            ButtonInstance.getInstance().currentButton().getPlayBt().setOnAction(
                    (ActionEvent e) -> {
                
                // play current track and continue playing the next in table        
                play(currentTime, songLabel, slider, filepath, selectionModel);    
                
            });
            
        }
           
    }
    
    /**
     * 
     * Functionality for mouse right click event in tableview
     * Used in listenTableClick method below
     * 
     * @param slider
     * @param currentTime
     * @param filepath
     * @param selectionModel 
     * @param event 
     */
    
    private void rightClick(MouseEvent event){
        
        if (event.isSecondaryButtonDown() && isMainTable()){          
            
            // add first menu item ("Add to faves") to new context menu
            ContextMenu menu = new ContextMenu(); 
            ArrayList<MenuItem> menuItemAl = new ArrayList<>();
            menuItemAl.add(new MenuItem("Add to faves"));
            
            SeparatorMenuItem separator = new SeparatorMenuItem();
            menuItemAl.add(separator);  
            
            // rest of menu items to context menu = dir names in .Kinetic/playlists
            for (File playlistDir : files.getPlaylistDir().listFiles()){
                
                menuItemAl.add(new MenuItem("Add to " + playlistDir.getName()));
                
            } 
            
            menu.getItems().addAll(menuItemAl);
            
            // handle if "Add to faves": add to .Kinetic/faves dir
            menuItemAl.get(0).setOnAction((ActionEvent e) -> {
                
                // set multiple track ids to a string arraylist - selectedRows
                setTableId(TableViewInstance.getInstance().currentTableView().getTableview());
                addToFaves();
                
            }); 
            
            /*  handle if "Add to <any playlist>" clicked - adds to 
                .Kinetic/playlists/<name of playlist> - note: starts at 3rd index
                of ArrayList because a javafx seperator is at 2nd index */
            for (int i = 2; i < menuItemAl.size(); i++){
                
                // local variable for use in handle() = specific menu item
                String playlistString = menuItemAl.get(i).getText();
                
                menuItemAl.get(i).setOnAction((ActionEvent e) -> {
                    
                    // set multiple track ids to a string arraylist - selectedRows
                    setTableId(TableViewInstance.getInstance().currentTableView().getTableview());
                    addToPlayList(playlistString.substring(7));
                    
                }); 
                
            } 
            
            // set context menu to current instance of tableview
            TableViewInstance.getInstance().currentTableView().getTableview().setContextMenu(menu);  
            
        }
        
        else {
            
            // set context menu to null if not in main table view
            TableViewInstance.getInstance().currentTableView().getTableview().setContextMenu(null);  
            
        }
    }
    
    /**
     * 
     * Listens for multiple mouse click events and handles behaviour of 
     * the tableview selection accordingly.
     * 
     * Accessed by initialize method in MainScreenCtrl controller class.
     * 
     * @param slider
     * @param currentTime
     * @param songLabel
     * @param filepath
     * @param selectionModel 
     */
    
    public void listenTableClick(Slider slider, Label currentTime, Label songLabel, String filepath, 
            TableView.TableViewSelectionModel selectionModel){
        
        TableViewInstance.getInstance().currentTableView().getTableview()
            .setOnMousePressed((MouseEvent event) -> {
                
                doubleClick(slider, currentTime, songLabel, filepath, selectionModel, event);
                singleClick(slider, currentTime, songLabel, filepath, selectionModel, event);         
                rightClick(event);      
                
        });
        
    }
    
    /**
     * 
     * Set multiple tableIds equal to selected rows (tablecolumn id) 
     * and set results to selectedRows arraylist. The selectedRows arraylist is used in 
     * addToFaves and addToPlayList methods. The below method is used in rightClick and 
     * singleClick methods to set multiple id's if required.     * 
     * 
     * @param table
     * @param selectionModel 
     */
    
    private static void setTableId(TableView<?> table) {
        
        ArrayList<Object> selectedId = new ArrayList<>();
        
        // set observable list to selected cells
        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();
        
        // iterate through selected cells and add individual table ids to selectedId        
        for (TablePosition position : positionList) {
            
            int row = position.getRow();           
            Object id = (Object) table.getColumns().get(6).getCellData(row);
            tableId = (Integer) id;
            selectedId.add(tableId);
            
        }
        selectedRows = selectedId;
    }
    
    /**
     * 
     * Overloaded - same functionality as above but is used when 
     * only a single tableId is required. Used in doubleClick and playTrack methods.
     * 
     * @param selectionModel 
     */
    
    private static void setTableId(TableView.TableViewSelectionModel selectionModel){ 
        
        // set tableId to selectedIndex's id tablecolumn
        tableId = Integer.parseInt(TableColumnInstance.getInstance().
                currentTableColumn().getIdTc().getCellData(selectionModel.getSelectedIndex()).toString()); 
        
    }   
    
    /**
     * 
     * Reads mainTable.bool file set in Library class to get value
     * relating to whether the current library state is main table, playlist table, 
     * or faves table. Used in rightClick method to ensure that functionality is
     * only accessed in main table.
     * 
     * @return boolean 
     * 
     */
    
    private boolean isMainTable(){
        BufferedReader br = null;
        
        boolean maintable = true;
                
        try {
            br = new BufferedReader(new FileReader("mainTable.bool"));
                        
            String currentLine = br.readLine();

            if (currentLine.equals("false")){
                
                maintable = false;
                
            }         
            
        } 
        
        catch (IOException anException) {            
            Logger.getLogger(PlayBack.class.getName()).log(Level.SEVERE, null, anException);            
        } 
        
        finally {
            
            try {
                
                br.close();
                
            }
            
            catch (NullPointerException | IOException anException){
                Logger.getLogger(PlayBack.class.getName()).log(Level.SEVERE, null, anException);
            }
            
        }
        
        System.out.println(maintable);
        return maintable;
        
    }
    
    /**
     * 
     * Copies files from .Kinetic/tracks to .Kinetic/faves dir 
     * using Apache Commons FileUtils library. Accessed in rightClick method - invoked
     * when context menu item clicked = "Add to faves".
     * 
     */
    
    private void addToFaves(){     
        
        try {
            for (File trackFile : files.getTracksDir().listFiles()){
                
                // selectedRows contains list of id's (which are equal to specific
                // file hash codes).
                if (selectedRows.contains(trackFile.hashCode())){
                    
                    FileUtils.copyFileToDirectory(trackFile, files.getFavesDir());
                
                }
            }
        }
        catch (IOException anException){
            System.out.println("addtofaves error: " + anException);
        }
        
    }
    
    /**
     * 
     * Copies files from .Kinetic/tracks to specific 
     * .Kinetic/playlists/... dir using Apache Commons FileUtils library. 
     * Used in rightClick method - invoked when context menu item clicked 
     * = "Add to x", where x = selected playlist. Accepts a string argument which 
     * is equal to the selected context menu item text.
     * 
     * @param playlistName 
     */
    
    private void addToPlayList(String playlistName){         
        
        File playlistDir = new File(files.getPlaylistDir().getPath() 
                + "/" + playlistName);         
        try {
            for (File trackFile : files.getTracksDir().listFiles()){
                
                // selectedRows contains list of id's (which are equal to specific
                // file hash codes).
                if (selectedRows.contains(trackFile.hashCode())){                    
                    
                    FileUtils.copyFileToDirectory(trackFile, playlistDir);
                
                }
                
            }
        }
        catch (IOException anException){
            System.out.println("addtofaves error: " + anException);
        }
        
    } 
    
    /**
     * 
     * Plays the next track in the tableview by selecting next 
     * index and repeating play method. Used in any method that invokes play
     * method.
     * 
     * @param currentTime
     * @param slider
     * @param filepath
     * @param selectionModel 
     */
    
    private void continuePlay(Label currentTime, Label songLabel, Slider slider, String filepath, 
            TableView.TableViewSelectionModel selectionModel){
        
        // when media player is finished play next track        
        mediaplayer.setOnEndOfMedia(() -> {
            // select next in tableview
            selectionModel.clearAndSelect(selectionModel.getSelectedIndex() + 1);
            
            setTableId(selectionModel);
            
            play(currentTime, songLabel, slider, filepath, selectionModel);
        });
        
    }      
        
    /**
     * 
     * Iterates through mp3 files in a specific filepath and sets 
     * new MediaPlayer instance to the filepath and the mediaplayer field in this class.
     * Used in play method, below.
     * 
     * @param filepath 
     * 
     */    
    
    private void playTrack(String filepath){        
        
        try {
            File inputFilesDir = new File (filepath); 
            
            for (File inputFile : inputFilesDir.listFiles()) {
                
                if (inputFile.hashCode() == tableId){
                    
                    Media media = new Media(inputFile.toURI().toString()); 
                    
                    mediaplayer = new MediaPlayer(media);
                    
                    currentlyPlaying = inputFile.getPath();
                    
                }
            }
        }
        
        catch (NullPointerException anException){
            System.out.println("playtrack error: " + anException);
        } 
        
    } 
    
    /**
     * 
     * If player is not null, the mediaplayer play method is invoked.
     * The sliderbar (bound slider and progressbar) is also started.
     * Play is continued if not interrupted, so that subsequent tracks in the 
     * table are played.
     * Accessed in singleClick, doubleClick and continuePlay methods.
     * 
     * @param currentTime
     * @param slider
     * @param filepath
     * @param selectionModel 
     */
    
    private void play(Label currentTime, Label songLabel, Slider slider, String filepath, 
            TableView.TableViewSelectionModel selectionModel){
        
        try {
            if (mediaplayer != null){
                
                // prevents multiple tracks from playing at once
                mediaplayer.stop();
                
            }
            
            // set media player to mp3 file that needs to be played
            playTrack(filepath);
            
            mediaplayer.play(); 
            
                     
            // sliderbar listener
            listenSliderBar(slider, currentTime, songLabel); 
            
            // play next track in table
            continuePlay(currentTime, songLabel, slider, filepath, selectionModel);
        }
        
        catch (NullPointerException anException){
            System.out.println("play: " + anException);
        } 
        
    }
    
    /**
     * 
     * Listener for stop button
     * Accessed in initialize method of MainScreenCtrl controller class
     * 
     */
    
    public void listenStop(){
        
        ButtonInstance.getInstance().currentButton().getStopBt().setOnAction((ActionEvent e) -> {
            
                mediaplayer.stop();                
                ButtonInstance.getInstance().currentButton().getStopBt().setSelected(false);
                
        });
        
    }

    /**
     * 
     * Listener for pause button
     * Accessed in initialize method of MainScreenCtrl controller class
     * 
     */
    
    public void listenPause(){
        
        ButtonInstance.getInstance().currentButton().getPauseBt().setOnAction((ActionEvent e) -> {
            
            // if playing, pause
            if (mediaplayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
                
                mediaplayer.pause();
                ButtonInstance.getInstance().currentButton().getPauseBt().setSelected(false); 
                
            }
            
            // if paused, play
            else if (mediaplayer.getStatus().equals(MediaPlayer.Status.PAUSED)){
                
                mediaplayer.play();
                ButtonInstance.getInstance().currentButton().getPauseBt().setSelected(false); 
                
            }
            
        });
        
    }
    
    /**
     * 
     * Handler for delete key event - if delete key pressed, select file that 
     * specifies the tableview selection, and delete.
     * 
     * @param deleteDir 
     */
    
    public void setDelete(File deleteDir){
        
        TableViewInstance.getInstance().currentTableView().getTableview().setOnKeyPressed((KeyEvent event) -> {
            
            if (event.getCode() == KeyCode.DELETE){
                
                for (File deleteFile : deleteDir.listFiles()){
                    
                    // if any of the ids in selectedRows arraylist = files hashcode
                    if (selectedRows.contains(deleteFile.hashCode())){
                        
                        deleteFile.delete();     
                        
                    }
                }
                
                // re-populate table
                populate.populateTable(deleteDir.getPath());
                
            }
            
        });
        
    }
    
    // return valueproperty for current slider.
    
    private DoubleProperty sliderValueProperty(Slider slider) {
        
        return slider.valueProperty();
        
    }
    
    // return true if slider is changing
    
    private boolean isSliderValueChanging(Slider slider) {
        
        return slider.isValueChanging();
        
    }
    
    /**
     * 
     * Listener for sliderbar - sliderbar is a slider and progressbar bound by 
     * javafx bindings.
     * 
     * Accessed by play method, so that it starts when play is activated.
     * 
     * @param slider
     * @param currentTime 
     */
    
    private void listenSliderBar(Slider slider, Label currentTime, Label songLabel){
        
        // mediaplayer listener - sets duration and displays in currentTime label
        mediaplayer.currentTimeProperty().addListener((
                ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
            
            // set slider property to duration
            sliderValueProperty(slider).setValue(newValue.divide(totalTime.toMillis()).toMillis() * 100.0);
            
            setTimeDisplay(newValue, currentTime);
            
        });
        
        // set total time for current track to totalTime
        mediaplayer.setOnReady(() -> { 
            
            totalTime = mediaplayer.getMedia().getDuration();  
            
            setSongLabel(songLabel);
           
        });        
        
        sliderValueProperty(slider).addListener((ov) -> {
            
            // if slider is active
            if (isSliderValueChanging(slider)) {
                
                if (mediaplayer != null){
                    
                    // seek mediaplayer to new duration (set to slider valueprop)
                    mediaplayer.seek(totalTime.multiply(sliderValueProperty(slider).getValue() / 100.0));                    
                    
                }
                
                else {
                    
                    sliderValueProperty(slider).setValue(0);
                    
                }
                
            }
            
        });
        
    }
    
    /**
     * 
     * Bind slider and progressbar.
     * 
     * Accessed in the initialize method of MainScreenCtrl controller class.
     * 
     * @param progressBar
     * @param slider 
     */
    
    public void bindSliderBar(ProgressBar progressBar, Slider slider){
        
        progressBar.prefWidthProperty().bind(slider.widthProperty());
        progressBar.progressProperty().bind(slider.valueProperty().divide(100));
        
    }
    
    /**
     * 
     * Formats a duration value into a decimal value - hours, minutes, seconds
     * 
     * @param aDuration
     * @return String timer
     * 
     */
    
    private static String formatDuration(Duration aDuration){
        
        String timer;
        
        // use decimal format
        DecimalFormat formatter = new DecimalFormat("00"); 
        
        // set duration new value to relevant time units         
        int hours = (int) aDuration.toHours();
        int minutes = (int) aDuration.toMinutes();
        int seconds = (int) aDuration.toSeconds();
        
        // reset at 60 seconds
        if (seconds / 60 >= 1) {
            
            seconds %= 60; 
            
        }
        
        // reset at 60 minutes
        if (minutes / 60 >= 1) {
            
            minutes %= 60;
            
        }
        
        return formatter.format(hours) + ":" + formatter.format(minutes) 
                + ":" + formatter.format(seconds);
        
    }
    
    /**
     * 
     * Sets currentTime label to formatted duration.
     * 
     * Accessed by listenSliderBarMethod.
     * 
     * @param newValue
     * @param currentTime 
     * 
     */    
    
    private static void setTimeDisplay(Duration newValue, Label currentTime){   
        
        //set currentTime formatted label on display
        currentTime.setText(formatDuration(newValue));
        
    }  
    
    /**
     * 
     * Sets songLabel label to metadata values artist and title, and formatted timer
     * value.
     * 
     * @param songLabel 
     */
    
    
    private void setSongLabel(Label songLabel){
        
        String artist, title;     
        
        try {
            
            Mp3File mp3file = new Mp3File(currentlyPlaying);
            
            if (mp3file.hasId3v1Tag()){
                
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                artist = id3v1Tag.getArtist();
                title = id3v1Tag.getTitle();
                 
                if (title.equals(null)){
                    
                    title = " Title unknown.";
                    
                }         
                
            }
            
            else {
                
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                artist = id3v2Tag.getArtist();
                title = id3v2Tag.getTitle();         
                
            }
            
            if (title == null || title.isEmpty()){
                    
                title = "Title unknown";
                    
            } 
            
            if (artist == null || title.isEmpty()){
                
                artist = "Artist unknown";
                
            }
            
            songLabel.setText(artist + "  --  " + title +  "\t" + formatDuration(totalTime));
            
        } 
        
        catch (IOException | UnsupportedTagException | InvalidDataException ex) {
            Logger.getLogger(PlayBack.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    } 
    
}
