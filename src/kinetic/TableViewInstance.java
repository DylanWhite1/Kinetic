package kinetic;

/**
 *
 * @author Dylan White
 * 
 * Class: TableViewInstance
 * 
 * Objective: Singleton class to return instance of Tableview so that they can be 
 * accessed globally.
 * 
 */
public class TableViewInstance {
    private final static TableViewInstance INSTANCE = new TableViewInstance();

    public static TableViewInstance getInstance() {
        return INSTANCE;
    }

    private final Tableview tableview = new Tableview();

    public Tableview currentTableView() {
        return tableview;
    }
}
