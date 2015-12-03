package kinetic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class AboutCtrl implements Initializable {
    
    @FXML private Label aboutText;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        aboutText.setText("Kinetic is a basic mp3 player for Linux, which plays mp3 files, "
                + "and allows the user to set playlists and favourite tracks. This"
                + " is a beta release - the alpha version will handle more file formats - "
                + "such as .flac files, is likely to contain more functions, such as a "
                + "search box and equaliser, and Windows / mac platform capability.\n\n"
                + "Author: Dylan White\n\n"
                + "email: dwhite2209@yahoo.co.uk\n\n");        
        
    }
    
    
}
