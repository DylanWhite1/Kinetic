package kinetic;

/**
 * 
 * @author Dylan White
 * 
 * Class: Tableview
 * 
 * Objective: JavaBeans model class to set and return instance of JavaFX TableView
 * 
 */

import javafx.scene.control.TableView;

public class Tableview implements java.io.Serializable {

    private TableView tableview;
    
    public Tableview(){
        this.tableview = null;
    }
    
    public TableView getTableview() {
        
        return tableview;
        
    }

    public void setTableview(TableView tableview) {
        
        this.tableview = tableview;
        
    }  
    
}
