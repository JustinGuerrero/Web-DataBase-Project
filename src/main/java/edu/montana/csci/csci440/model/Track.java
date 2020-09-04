package edu.montana.csci.csci440.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Track extends Model {
    public Album getAlbum() {
        return null;
    }
    public MediaType getMediaType() {
        return null;
    }
    public Genre getGenre() {
        return null;
    }
    public List<Playlist> getPlaylists(){
        return Collections.emptyList();
    }
    public List<InvoiceItem> getInvoiceItems(){
        return Collections.emptyList();
    }
}
