package kinetic;

import javafx.scene.control.ToggleButton;

/**
 * 
 * @author Dylan White
 * 
 * Class: Buttons
 * 
 * Objective: JavaBeans model class to set and return instances of JavaFX ToggleButton
 * 
 */

public class Buttons implements java.io.Serializable {
    
    private ToggleButton playBt; // Play function set in PlayBack class
    
    private ToggleButton pauseBt; // Pause function set in PlayBack class
    
    private ToggleButton stopBt; // Stop function set in PlayBack class
    
    public Buttons() {
        
        this.playBt = null;
        this.pauseBt = null;
        this.stopBt = null;
        
    }
    
    // Getters and setters
    
    public ToggleButton getPlayBt() {
        
        return playBt;
        
    }

    public void setPlayBt(ToggleButton playBt) {
        
        this.playBt = playBt;
        
    }

    public ToggleButton getPauseBt() {
        
        return pauseBt;
        
    }

    public void setPauseBt(ToggleButton pauseBt) {
        
        this.pauseBt = pauseBt;
        
    }

    public ToggleButton getStopBt() {
        
        return stopBt;
        
    }

    public void setStopBt(ToggleButton stopBt) {
        
        this.stopBt = stopBt;
        
    }
}