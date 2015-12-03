package kinetic;

/**
 * 
 * @author Dylan White
 * 
 * Class: Track
 * 
 * Objective: JavaBeans model class to set and return instances of Track
 * 
 */

public class Track implements java.io.Serializable {
    
    private String artist, title, album, year, genre; 
    
    private int id;
    
    private Object trackNum;    
    
    public Track(Object aTrackNum, String anArtist, String aTitle, String anAlbum, 
            String aYear, String aGenre, int anId){
        this.album = anAlbum;
        this.artist = anArtist;
        this.genre = aGenre;
        this.id = anId;
        this.title = aTitle;
        this.trackNum = aTrackNum;
        this.year = aYear;
    }
    
    public Track(){
        this.album = null;
        this.artist = null;
        this.genre = null;
        this.id = 0;
        this.title = null;
        this.trackNum = null;
        this.year = null;
    }

    public String getArtist() {
        
        return artist;
        
    }

    public void setArtist(String artist) {
        
        this.artist = artist;
        
    }

    public String getTitle() {
        
        return title;
        
    }

    public void setTitle(String title) {
        
        this.title = title;
        
    }

    public String getAlbum() {
        
        return album;
        
    }

    public void setAlbum(String album) {
        
        this.album = album;
        
    }

    public String getYear() {
        
        return year;
        
    }

    public void setYear(String year) {
        
        this.year = year;
        
    }

    public String getGenre() {
        
        return genre;
        
    }

    public void setGenre(String genre) {
        
        this.genre = genre;
        
    }

    public int getId() {
        
        return id;
        
    }

    public void setId(int id) {
        
        this.id = id;
        
    }

    public Object getTrackNum() {
        
        return trackNum;
        
    }

    public void setTrackNum(Object trackNum) {
        
        this.trackNum = trackNum;
        
    }
}
