package kinetic;

/**
 *
 * @author Dylan White
 * 
 * Class: ButtonInstance
 * 
 * Objective: Singleton class to return instance of Buttons so that they can be 
 * accessed globally.
 * 
 * Copyright 2015 Dylan White (GPL)
 * 
 */
public class ButtonInstance {
    
    private final static ButtonInstance INSTANCE = new ButtonInstance();
    
    private ButtonInstance(){}
    
    public static ButtonInstance getInstance() {
        
        return INSTANCE;
        
    }
    
    // Buttons class contains play, pause and stop javafx ToggleButton fields 
    
    private final Buttons button = new Buttons();

    public Buttons currentButton() {
        
        return button;
        
    }
}
