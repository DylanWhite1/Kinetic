package kinetic;

/*
    Kinetic is a free music player that plays mp3 files. It provides the core 
    functionality expected of any desktop-based music player: ie play, pause, and 
    stop the mp3 file, create playlists, and set favourite songs. This is the
    first beta release of the project - extra functionality is planned, such as 
    support for other file types (like .flac, for eg), and the ability to edit 
    file metadata. Also, this beta release will only function on Linux systems, 
    but it is expected that the full 1.0 release will also function on Windows.
    Kinetic was made with core Java and JavaFX, and is released under GPL. 
    Please note, I am a self-taught hobbyist and not a professional; as such, 
    any comments on how I can improve KineticMain will be greatly appreciated. 
    Stay tuned for the 1.0 release...no pun intended.
*/

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import javafx.scene.image.Image;

/**
 * 
 * @author Dylan White
 
 Class: KineticMain extends Application
 
 Objective: Initiates JavaFX, using mainScreen.fxml as view, and 
 mainScreen.css for styles.
 * 
 */

public class KineticMain extends Application {
    
    /*  
       Start method - includes createFiles method which creates the necessary
       directories for the application: .Kinetic/tracks, .Kinetic/playlists, and
       .Kinetic/faves, if they don't already exist.     
    */
    
    @Override
    public void start(Stage stage) throws Exception {
        //  Create user directories if not exist.
        createFiles();         
        System.out.println(System.getProperty("java.version"));
        
        Parent root = FXMLLoader.load(getClass().getResource("fxml/mainScreen.fxml"));
        Scene scene = new Scene(root, 1128, 700);        
        
        stage.setTitle("Kinetic");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.png")));
        stage.setScene(scene);
        stage.show();     
    }
    
    //  Create user directories if not exist.
    
    private static void createFiles(){        
        try {
            if (!new File(System.getProperty("user.home") + ".Kinetic").exists()) {

                boolean fileCreated = new File(System.getProperty
                    ("user.home") + ".Kinetic/tracks").mkdirs();
                fileCreated = new File(System.getProperty
                    ("user.home") + ".Kinetic/faves").mkdir();
                fileCreated = new File(System.getProperty
                    ("user.home") + ".Kinetic/playlists").mkdir();

                if (!fileCreated) {
                    
                    System.out.println("Files not created.");  
                    
                }
            }

            else {            
                System.out.println("Files present.");            
            }
        }
        
        catch (NullPointerException anException) {
            System.out.println("createFiles error: " + anException);
        }        
    }
    
    public static void main(String[] args) {        
        launch(args);    
    }

}
