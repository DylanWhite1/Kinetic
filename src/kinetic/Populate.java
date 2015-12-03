package kinetic;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * 
 * @author Dylan White
 * 
 * Class: Populate
 * 
 * Objective: To populate the main table with instances of Track. The file metadata
 * is extracted and used to fill the table with relevant text - track number, artist, 
 * song title, album, year released, and genre. If any of these fields are null - 
 * which they often are - an underscore replaces the null value. 
 * 
 * Uses: mp3agic library via Maven repository.
 * 
 * Accessed in Files, Library, and MainScreenCtrl by composition
 * 
 */

public class Populate {
    
    private final Track track;
    
    public Populate(){
        this.track = new Track();
    }
    
    /**
     * 
     * Gets input mp3 file from .Kinetic/tracks, .Kinetic/playlists, or .Kinetic/faves,
     * and sets Track instance variables to specific value derived from metadata. 
     * These values are used to fill the main table - including a hidden id, based
     * on the file hash code. 
     * 
     * Accessed in MainScreenCtrl, Library and Files classes.
     * 
     * @param filepath
     * 
     */
    
    public void populateTable(String filepath){       
        
        ArrayList<Track> newTrack = new ArrayList<>();
        
        try {
            File inputFilesDir = new File (filepath); 
            
            for (File inputFile : inputFilesDir.listFiles()) { 
                
                // new mp3 file set to input file from .Kinetic/...
                Mp3File mp3file = new Mp3File(inputFile.getPath());
                
                if (mp3file.hasId3v1Tag()){
                    
                    // set new tag and metadata to fields in Track class
                    ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                    setTrackData(id3v1Tag);
                    
                }
                else if (mp3file.hasId3v2Tag()){
                    
                    // set new tag and metadata to fields in Track class
                    ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                    setTrackData(id3v2Tag);
                    
                }
                
                // set newTrack array list to new Track instance
                newTrack.add(new Track(track.getTrackNum(), track.getArtist(),
                    track.getTitle(), track.getAlbum(), track.getYear(),
                    track.getGenre(), inputFile.hashCode()));
                
            }
            
            // set values to table
            setTableColumnValues(newTrack);
            
        }
        
        catch (NullPointerException | IOException | UnsupportedTagException | 
                InvalidDataException anException){
            System.out.println("populateTable error: " + anException);
        }
        
    }
    
    /**
     * 
     * Sets table columns to values set in Track class
     * 
     * @param newTrack
     * 
     */
    
    private void setTableColumnValues(ArrayList<Track> newTrack){
        
        ObservableList<Object> data = FXCollections.observableArrayList(newTrack);
        
        TableColumnInstance.getInstance().currentTableColumn().getTrackTc().
                setCellValueFactory(new PropertyValueFactory<>("trackNum"));
        
        TableColumnInstance.getInstance().currentTableColumn().getArtistTc().
                setCellValueFactory(new PropertyValueFactory<>("artist"));
        
        TableColumnInstance.getInstance().currentTableColumn().getTitleTc().
                setCellValueFactory(new PropertyValueFactory<>("title"));
        
        TableColumnInstance.getInstance().currentTableColumn().getAlbumTc().
                setCellValueFactory(new PropertyValueFactory<>("album"));
        
        TableColumnInstance.getInstance().currentTableColumn().getYearTc().
                setCellValueFactory(new PropertyValueFactory<>("year"));
        
        TableColumnInstance.getInstance().currentTableColumn().getGenreTc().
                setCellValueFactory(new PropertyValueFactory<>("genre"));
        
        TableColumnInstance.getInstance().currentTableColumn().getIdTc().
                setCellValueFactory(new PropertyValueFactory<>("id"));
        
        // set table as editable
        TableViewInstance.getInstance().currentTableView().getTableview().setEditable(true);
        
        TableViewInstance.getInstance().currentTableView().getTableview().setItems(data);  

    }
    
    /**
     * 
     * Helper method - sets track data in Track. Also validates if the data is null
     * and replaces with an underscore.
     * 
     * @param idTag 
     * 
     */
    
    private void setTrackData(ID3v1 idTag){   
        
        // sets array element to true if metadata = null
        boolean[] nullArray = {
            
            idTag.getTrack() == null,
            
            idTag.getArtist() == null,
            
            idTag.getTitle() == null,
            
            idTag.getAlbum() == null,
            
            idTag.getYear() == null,
            
            idTag.getGenreDescription() == null
                
        };
        
        // sets array elements to specific metadata
        
        String[] trackArray = {
            
            idTag.getTrack(),
            
            idTag.getArtist(),
            
            idTag.getTitle(),
            
            idTag.getAlbum(),
            
            idTag.getYear(),
            
            idTag.getGenreDescription()
                
        };
        
        // string array trackData is set to value specified in track array if the 
        // value is not null, else it sets it to an underscore.
        String[] trackData = new String[trackArray.length];
        
        for (int i = 0; i < trackData.length; i++){
            
            if (nullArray[i] == false){
                
                trackData[i] = trackArray[i];
                
            }
            
            else {
                
                trackData[i] = "_";
                
            }
            
            if (trackData[i].isEmpty()){
                
                trackData[i] = "_";
                
            }
            
        }
        
        Integer trackNumber;
        
        // if track number metadata is not null
        if (!trackData[0].equals("_")){
            
            // sometimes metadata appears as a fraction - set trackNum to first value.            
            if (trackData[0].contains("/")){
                
                String trackString = trackData[0].substring(0,trackData[0].indexOf("/"));
                trackNumber = Integer.valueOf(trackString);
                track.setTrackNum(trackNumber);
                
            }
            
            // if track data starts with 0, set trackNum to values following the 0
            else if (trackData[0].startsWith("0")){
                
                String trackString = trackData[0].substring(1);
                trackNumber = Integer.valueOf(trackString);
                track.setTrackNum(trackNumber);
                
            }            
            else {
                
                // set trackNum to value of trackData, if the above conditions do not apply
                trackNumber = Integer.valueOf(trackData[0]); 
                track.setTrackNum(trackNumber); 
                
            }
            
        }
        
        // if null, set as underscore.
        else {
            
           track.setTrackNum(trackData[0]); 
           
        }
        
        // set Track instance variables
        track.setArtist(trackData[1]);
        
        track.setTitle(trackData[2]);
        
        track.setAlbum(trackData[3]);
        
        track.setYear(trackData[4]);
        
        track.setGenre(trackData[5]);
        
    }
    
    /**
     * 
     * Helper method - sets track data in Track. Also validates if the data is null
     * and replaces with an underscore. Overloaded version of the above. Note: takes
     * ID3v2 object as param.
     * 
     * @param idTag 
     */
    
    private void setTrackData(ID3v2 idTag){
        boolean[] nullArray = {
            idTag.getTrack() == null,
            idTag.getArtist() == null,
            idTag.getTitle() == null,
            idTag.getAlbum() == null,
            idTag.getYear() == null,
            idTag.getGenreDescription() == null
        };
        String[] trackArray = {
            idTag.getTrack(),
            idTag.getArtist(),
            idTag.getTitle(),
            idTag.getAlbum(),
            idTag.getYear(),
            idTag.getGenreDescription()
        };
        String[] trackData = new String[6];
        for (int i = 0; i < 6; i++){
            if (nullArray[i] == false){
                trackData[i] = trackArray[i];
            }
            else {
                trackData[i] = "_";
            }
            if (trackData[i].isEmpty()){
                trackData[i] = "_";
            }
        }
        Integer trackNumber;
        if (!trackData[0].equals("_")){
            if (trackData[0].contains("/")){
                String trackString = trackData[0].substring(0,trackData[0].indexOf("/"));
                trackNumber = Integer.valueOf(trackString);
                track.setTrackNum(trackNumber);                
            }
            else if (trackData[0].startsWith("0")){
                
                String trackString = trackData[0].substring(1);
                trackNumber = Integer.valueOf(trackString);
                track.setTrackNum(trackNumber);
            }            
            else {
                trackNumber = Integer.valueOf(trackData[0]); 
                track.setTrackNum(trackNumber); 
            }
        }
        else {
           track.setTrackNum(trackData[0]);         
        }   
        track.setArtist(trackData[1]);
        track.setTitle(trackData[2]);
        track.setAlbum(trackData[3]);
        track.setYear(trackData[4]);
        track.setGenre(trackData[5]);
    }
    
}
