package kinetic;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

/**
* 
*  @author Dylan White
* 
*  Class: MainScreenCtrl (Controller)
* 
*  Objective: Main controller class for the main screen. 
* 
*/

public class MainScreenCtrl implements Initializable {
    
    @FXML private TableView tableview;
    
    @FXML private ToggleButton playBt, stopBt, pauseBt;
    
    @FXML private TableColumn trackTc, artistTc, titleTc, albumTc, yearTc, genreTc, idTc;    
    
    @FXML private TreeView<String> treeview;    
    
    @FXML private ProgressBar progressBar;
    
    @FXML private Slider slider;
    
    @FXML private Label currentTime, songLabel;
    
    private final Files files;
    
    private final Populate populate;
    
    private final PlayBack playback;
    
    private final Library library; 
    
    private final Import importInstance;
    
    public MainScreenCtrl(){
        this.files = new Files();   
        this.populate = new Populate();
        this.playback = new PlayBack();
        this.library = new Library();
        this.importInstance = new Import();
    }
    
    //  Main screen initialize method 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //  set new TableView, TableColumn and ToggleButton instances  
        setInstances();        
        
        //  set tableview selection model
        TableView.TableViewSelectionModel selectionModel = tableview.getSelectionModel();
        
        //  playback listeners - table and button functionality   
        playback.listenTableClick(slider, currentTime, songLabel,
                files.getTracksDir().getPath(), selectionModel);
        playback.listenStop();
        playback.listenPause();
        
        //  binds slider and progress bar functionality
        playback.bindSliderBar(progressBar, slider); 
        
        //  event handler for 'delete' function - listens for user to press [del]
        playback.setDelete(files.getTracksDir()); 
        
        //  loads library treeview and listens for events       
        library.loadLibrary(treeview, slider, currentTime, songLabel); 
        
        //  populate tableview with new Track instances        
        populate.populateTable(files.getTracksDir().getPath());
    } 
    
    /**
     * 
     * Event listener for 'import tune' menuitem  
     * 
     */ 
    
    @FXML
    private void importTrack(ActionEvent e){ 
        
        importInstance.importFile();
        populate.populateTable(files.getTracksDir().getPath());
        
    }
    
    /**
     * 
     * Event listener for 'import album' menuitem  
     * 
     */ 
    
    @FXML
    private void importAlbum(ActionEvent e){
        
        importInstance.importDir(); 
        populate.populateTable(files.getTracksDir().getPath());
        
    }  
    
    /**
     * 
     *  Uses singleton and JavaBeans classes to set global fields
     * 
     */
      
    private void setInstances(){
        // Set tableview field to be used globally
        TableViewInstance.getInstance().currentTableView().setTableview(tableview);
        
        // Set tablecolumn fields to be used globally
        TableColumnInstance.getInstance().currentTableColumn().setTrackTc(trackTc);
        
        TableColumnInstance.getInstance().currentTableColumn().setArtistTc(artistTc);
        
        TableColumnInstance.getInstance().currentTableColumn().setTitleTc(titleTc);
        
        TableColumnInstance.getInstance().currentTableColumn().setAlbumTc(albumTc);
        
        TableColumnInstance.getInstance().currentTableColumn().setYearTc(yearTc);
        
        TableColumnInstance.getInstance().currentTableColumn().setGenreTc(genreTc);
        
        TableColumnInstance.getInstance().currentTableColumn().setIdTc(idTc);
        
        // Set button fields to be used globally
        ButtonInstance.getInstance().currentButton().setPlayBt(playBt);
        
        ButtonInstance.getInstance().currentButton().setPauseBt(pauseBt);
        
        ButtonInstance.getInstance().currentButton().setStopBt(stopBt);        
        
    }
    
    @FXML
    private void startAbout(ActionEvent e) throws IOException{
        Parent root;
        root = FXMLLoader.load(getClass().getResource("fxml/about.fxml"));
        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setScene(new Scene(root, 500, 300));
        stage.show();
    }

    @FXML
    private void exit(ActionEvent e){
        System.exit(0);        
    }
}