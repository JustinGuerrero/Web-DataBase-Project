package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Track extends Model {
    private Long trackId;
    private Long albumId;
    private Long mediaTypeId = 1L;
    private Long genreId =1L;
    private String name;
    private Long milliseconds = 0L;
    private Long bytes= 0L;
    private BigDecimal unitPrice = new BigDecimal(0);
    private String ArtistName;
    private String AlbumName;



    public static final String REDIS_CACHE_KEY = "cs440-tracks-count-cache";

    public Track() {
        mediaTypeId = 1l;
        genreId = 1l;
        milliseconds  = 0l;
        bytes  = 0l;
        unitPrice = new BigDecimal("0");
    }

    Track(ResultSet results) throws SQLException {
        name = results.getString("Name");
        milliseconds = results.getLong("Milliseconds");
        bytes = results.getLong("Bytes");
        unitPrice = results.getBigDecimal("UnitPrice");
        trackId = results.getLong("TrackId");
        albumId = results.getLong("AlbumId");
        mediaTypeId =1L;//results.getLong("MediaTypeId");
        genreId = 1L;//results.getLong("GenreId");
        ArtistName = results.getString("ArtistName");
        AlbumName = results.getString("AlbumName");

    }

    public static Track find(long i) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT tracks.*, Albums.Title as AlbumName, " +
                     "Artists.Name as ArtistName\n" +
                     "FROM tracks\n" +
                     "         INNER JOIN albums ON albums.AlbumID = tracks.AlbumID\n" +
                     "         INNER JOIN artists ON artists.ArtistId = albums.ArtistId\n" +
                     "WHERE TrackId =?")) {
            stmt.setLong(1, i);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                return new Track(results);
            } else {
                return null;
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static Long count() {
        Jedis redisClient = new Jedis(); // use this class to access redis and create a cache
        String stringVal = redisClient.get(REDIS_CACHE_KEY);
        if(stringVal != null){
            return Long.parseLong(stringVal);
        }
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as Count FROM tracks")) {
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                long count = results.getLong("Count");
                redisClient.set(REDIS_CACHE_KEY, Long.toString(count));
                return count;
            } else {
                throw new IllegalStateException("Should find a count!");
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public Album getAlbum() {
        return Album.find(albumId);
    }

    public MediaType getMediaType() {
        return null;
    }
    public Genre getGenre() {
        return null;
    }

    public List<Playlist> getPlaylists() {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM playlists PL INNER JOIN playlist_track pt on pt.PlaylistId = PL.PlaylistId INNER JOIN tracks t ON t.TrackId = pt.TrackId WHERE ? = pt.TrackId AND PL.PlaylistId = pt.PlaylistId"

             )) {
            stmt.setLong(1, this.getTrackId());
            ResultSet results = stmt.executeQuery();
            List<Playlist> resultList = new LinkedList<>();
            while (results.next()) {
                Playlist PlaylistAdd = new Playlist(results);
                resultList.add(PlaylistAdd);
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }

    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMilliseconds() { return milliseconds;}

    public void setMilliseconds(Long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public void setAlbum(Album album) {
        albumId = album.getAlbumId();
    }

    public Long getMediaTypeId() {
        return mediaTypeId;
    }

    public void setMediaTypeId(Long mediaTypeId) { this.mediaTypeId = mediaTypeId;}

    public Long getGenreId() {return genreId; }

    public void setGenreId(Long genreId) { this.genreId = genreId;}

    public String getArtistName() {
        return ArtistName;

    }

    public String getAlbumTitle() {
        return AlbumName;
    }
    //"SELECT tracks.*, Albums.Title as AlbumName, " +
    //                     "Artists.Name as ArtistName\n" +
    //                     "FROM tracks\n" +
    //                     "         INNER JOIN albums ON albums.AlbumID = tracks.AlbumID\n" +
    //                     "         INNER JOIN artists ON artists.ArtistId = albums.ArtistId\n" +
    //                     "WHERE TrackId =?

    public static List<Track> advancedSearch(int page, int count,
                                             String search, Integer artistId, Integer albumId,
                                             Integer maxRuntime, Integer minRuntime) {
        LinkedList<Object> args = new LinkedList<>();

        String query = "SELECT tracks.*, Albums.Title As AlbumName, " +
                "Artists.Name as ArtistName FROM tracks " +
                "INNER JOIN albums on albums.AlbumId = tracks.AlbumId " +
                "INNER JOIN artists on artists.ArtistId = albums.ArtistId " +
                "WHERE tracks.name LIKE ?";
        args.add("%" + search + "%");

        // Conditionally include the query and argument
        if (artistId != null) {
            query += " AND artists.ArtistId=? ";
            args.add(artistId);
        }

        query += " LIMIT ?";
        args.add(count);

        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < args.size(); i++) {
                Object arg = args.get(i);
                stmt.setObject(i + 1, arg);
            }
            ResultSet results = stmt.executeQuery();
            List<Track> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static List<Track> search(int page, int count, String orderBy, String search) {
        String query = "SELECT tracks.*, Albums.Title As AlbumName, " +
                "Artists.Name as ArtistName FROM tracks " +
                "INNER JOIN albums on albums.AlbumId = tracks.AlbumId " +
                "INNER JOIN artists on artists.ArtistId = albums.ArtistId" +
                " WHERE tracks.name LIKE ? LIMIT ?";
        search = "%" + search + "%";
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, search);
            stmt.setInt(2, count);
            ResultSet results = stmt.executeQuery();
            List<Track> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static List<Track> forAlbum(Long albumId) {
        String query = "SELECT tracks.*, Albums.Title as AlbumName, Artists.Name as ArtistName " +
                "From tracks " +
                "INNER JOIN albums on albums.AlbumId = tracks.AlbumId " +
                "INNER JOIN artists on tracks.Name = artists.Name " +
                "WHERE tracks.AlbumId=?";
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, albumId);
            ResultSet results = stmt.executeQuery();
            List<Track> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    // Sure would be nice if java supported default parameter values
    public static List<Track> all() {
        return all(0, Integer.MAX_VALUE);
    }

    public static List<Track> all(int page, int count) {
        //
        return all(page, count, "TrackId");
    }

    public static List<Track> all(int page, int count, String orderBy) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT tracks.*, Albums.Title as AlbumName, Artists.Name as ArtistName FROM tracks " +
                             "INNER JOIN albums ON albums.AlbumID = tracks.AlbumID " +
                             "INNER JOIN artists ON artists.ArtistId = albums.ArtistId" +
                             " ORDER BY " + orderBy + " ASC LIMIT  ? OFFSET ?"
             )) {
            stmt.setInt(1, count);
            stmt.setInt(2, (page-1)*count);
            ResultSet results = stmt.executeQuery();
            List<Track> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public boolean verify() {
        _errors.clear(); // clear any existing errors
        if (name == null || "".equals(name)) {
            addError("Name can't be null or blank!");
        }
        if (albumId == null || "".equals(albumId)) {
            addError("Wack you gotta put a name in");
        }
        return !hasErrors();
    }


    @Override
    public boolean create() {
        Jedis redisClient = new Jedis();
        redisClient.flushDB();
        if (verify()) {
            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO tracks (name, Milliseconds, Bytes, UnitPrice, AlbumId, MediaTypeId, GenreId)" +
                                 " VALUES (?,?,?,?,?,?,?)")) {
                stmt.setString(1, this.getName());
                stmt.setLong(2, this.getMilliseconds());
                stmt.setLong(3, this.getBytes());
                stmt.setBigDecimal(4, this.getUnitPrice());
                stmt.setLong(5, this.getAlbumId());
                stmt.setLong(6, this.getMediaTypeId());
                stmt.setLong(7, this.getGenreId());
                stmt.executeUpdate();
                trackId = DB.getLastID(conn);
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean update() {
        if (verify()) {
            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE tracks  SET name=?, AlbumId=?  WHERE TrackId=?")) {
                stmt.setString(1, this.getName());
                stmt.setLong(2, this.getAlbumId());
                stmt.setLong(3, this.getTrackId());

                stmt.executeUpdate();
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            return false;
        }
    }

    @Override
    public void delete() {
        Jedis redisClient = new Jedis();
        redisClient.flushDB();
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM tracks WHERE TrackId=?")) {
            stmt.setLong(1, this.getTrackId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

}
