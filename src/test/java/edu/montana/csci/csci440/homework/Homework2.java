package edu.montana.csci.csci440.homework;

import edu.montana.csci.csci440.DBTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class Homework2 extends DBTest {

    @Test
    void selectArtistsWhoseNameHasAnAInIt(){
        List<Map<String, Object>> results = executeSQL("SELECT * FROM artists");
        assertEquals(211, results.size());
    }

    @Test
    void selectAllArtistsWithMoreThanOneAlbum(){
        List<Map<String, Object>> results = executeSQL(
                "SELECT * FROM artists");

        assertEquals(56, results.size());
        assertEquals("AC/DC", results.get(0).get("Name"));
    }

    @Test
    void selectTheTrackAndAlbumAndArtistForAllTracksLongerThanSixMinutes() {
        List<Map<String, Object>> results = executeSQL(
                "SELECT tracks.Name as TrackName, albums.Title as AlbumTitle, artists.Name as ArtistsName FROM tracks " +
                        "-- NEED TO DO SOME JOINS HERE KIDS");

        System.out.println(results);
        assertEquals(260, results.size());
        assertEquals("Sleeping Village", results.get(0).get("TrackName"));
        assertEquals("Black Sabbath", results.get(0).get("AlbumTitle"));
        assertEquals("Black Sabbath", results.get(0).get("ArtistsName"));

        assertEquals("Walkin'", results.get(10).get("TrackName"));
        assertEquals("The Essential Miles Davis [Disc 1]", results.get(10).get("AlbumTitle"));
        assertEquals("Miles Davis", results.get(10).get("ArtistsName"));

    }

}
