package kinetic;

/**
 *
 * @author Dylan White
 * 
 * Class: TableColumnInstance
 * 
 * Objective: Singleton class to return instance of TableColumns so that they can be 
 * accessed globally.
 * 
 */

public class TableColumnInstance {
    private final static TableColumnInstance INSTANCE = new TableColumnInstance();
    
    private TableColumnInstance(){}
    
    public static TableColumnInstance getInstance() {
        return INSTANCE;
    }

    private final Tablecolumn tablecolumn = new Tablecolumn();

    public Tablecolumn currentTableColumn() {
        return tablecolumn;
    }
}
