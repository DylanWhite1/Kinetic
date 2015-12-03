package kinetic;

/**
 *
 * @author Dylan White
 * 
 * Class: LibraryInstance
 * 
 * Objective: Singleton class to return instance of Library so it they can be 
 * accessed globally.
 * 
 */

public class LibraryInstance {
    private final static LibraryInstance INSTANCE = new LibraryInstance();
    
    private LibraryInstance(){}
    
    public static LibraryInstance getInstance() {
        return INSTANCE;
    }

    private final Library lib = new Library();

    public Library currentLibrary() {
        return lib;
    }
}
