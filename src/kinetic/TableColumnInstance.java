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
 * Copyright 2015 Dylan White (GPL)
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
