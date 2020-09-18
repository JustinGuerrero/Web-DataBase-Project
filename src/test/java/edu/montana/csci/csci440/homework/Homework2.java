package edu.montana.csci.csci440.homework;

import edu.montana.csci.csci440.DBTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class Homework2 extends DBTest {

    @Test
    void selectArtistsWhoseNameHasAnAInIt(){
        List<Map<String, Object>> results = executeSQL("SELECT * from artists where Name like '%a%'"
        );
        assertEquals(211, results.size());
    }

    @Test
    void selectAllArtistsWithMoreThanOneAlbum(){
        List<Map<String, Object>> results = executeSQL(
                "SELECT artists.Name,\n" +
                        "       COUNT(DISTINCT albums.AlbumId) as Albums\n" +
                        "FROM albums\n" +
                        "         JOIN artists on albums.ArtistId = artists.ArtistId\n" +
                        "GROUP BY albums.ArtistId\n" +
                        "HAVING Albums > 1;");

        assertEquals(56, results.size());
        assertEquals("AC/DC", results.get(0).get("Name"));
    }

    @Test
    void selectTheTrackAndAlbumAndArtistForAllTracksLongerThanSixMinutes() {
        List<Map<String, Object>> results = executeSQL(
                "SELECT tracks.Name as TrackName, albums.Title as AlbumTitle, artists.Name as ArtistsName\n" +
                        "FROM tracks\n" +
                        "             JOIN albums on tracks.AlbumId = albums.AlbumId\n" +
                        "             JOIN artists on albums.ArtistId = artists.ArtistId\n" +
                        "WHERE tracks.Milliseconds >= 360000;");

        assertEquals(623, results.size());

        // For now just get the count right, we'll do more elaborate stuff when we get
        // to ORDER BY
        //
        //
//        assertEquals("Princess of the Dawn", results.get(0).get("TrackName"));
//        assertEquals("Restless and Wild", results.get(0).get("AlbumTitle"));
//        assertEquals("Accept", results.get(0).get("ArtistsName"));
//
//        assertEquals("Snoopy's search-Red baron", results.get(10).get("TrackName"));
//        assertEquals("The Best Of Billy Cobham", results.get(10).get("AlbumTitle"));
//        assertEquals("Billy Cobham", results.get(10).get("ArtistsName"));

    }


}
