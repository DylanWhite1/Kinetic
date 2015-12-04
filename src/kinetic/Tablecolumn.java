package kinetic;

import javafx.scene.control.TableColumn;

/**
 * 
 * @author Dylan White
 * 
 * Class: Tablecolumn
 * 
 * Objective: JavaBeans model class to set and return instances of JavaFX TableColumn
 * 
 * Copyright 2015 Dylan White (GPL)
 * 
 */

public class Tablecolumn {
    
    private TableColumn trackTc;
    
    private TableColumn artistTc;
    
    private TableColumn titleTc;
    
    private TableColumn albumTc;
    
    private TableColumn yearTc;
    
    private TableColumn genreTc;
    
    private TableColumn idTc;

    public Tablecolumn(){
        this.albumTc = null;
        this.artistTc = null;
        this.genreTc = null;
        this.idTc = null;
        this.titleTc = null;
        this.trackTc = null;
        this.yearTc = null;
    }
    
    public TableColumn getTrackTc() {
        
        return trackTc;
        
    }

    public void setTrackTc(TableColumn trackTc) {
        
        this.trackTc = trackTc;
        
    }

    public TableColumn getArtistTc() {
        
        return artistTc;
        
    }

    public void setArtistTc(TableColumn artistTc) {
        
        this.artistTc = artistTc;
        
    }

    public TableColumn getTitleTc() {
        
        return titleTc;
        
    }

    public void setTitleTc(TableColumn titleTc) {
        
        this.titleTc = titleTc;
        
    }

    public TableColumn getAlbumTc() {
        
        return albumTc;
        
    }

    public void setAlbumTc(TableColumn albumTc) {
        
        this.albumTc = albumTc;
        
    }

    public TableColumn getYearTc() {
        
        return yearTc;
        
    }

    public void setYearTc(TableColumn yearTc) {
        
        this.yearTc = yearTc;
        
    }

    public TableColumn getGenreTc() {
        
        return genreTc;
        
    }

    public void setGenreTc(TableColumn genreTc) {
        
        this.genreTc = genreTc;
        
    }

    public TableColumn getIdTc() {
        
        return idTc;
        
    }

    public void setIdTc(TableColumn idTc) {
        
        this.idTc = idTc;
        
    }    
}
