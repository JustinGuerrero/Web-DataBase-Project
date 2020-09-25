package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.model.Playlist;
import edu.montana.csci.csci440.util.Web;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class PlaylistsController {
    public static void init() {
        /* CREATE */
        get("/playlists/new", (req, resp) -> {
            Playlist playlist = new Playlist();
            return Web.renderTemplate("templates/playlists/new.vm", "playlist", playlist);
        });

        post("/playlists/new", (req, resp) -> {
            Playlist playlist = new Playlist();
            Web.putValuesInto(playlist, "Name");
            if (playlist.create()) {
                Web.message("Created A Playlist!");
                return Web.redirect("/playlists/" + playlist.getPlaylistId());
            } else {
                Web.error("Could Not Create A Playlist!");
                return Web.renderTemplate("templates/playlists/new.vm",
                        "playlist", playlist);
            }
        });

        /* READ */
        get("/playlists", (req, resp) -> {
            List<Playlist> playlists = Playlist.all(Web.getPage(), Web.PAGE_SIZE);
            return Web.renderTemplate("templates/playlists/index.vm",
                    "playlists", playlists);
        });

        get("/playlists/:id", (req, resp) -> {
            Playlist playlist = Playlist.find(Integer.parseInt(req.params(":id")));
            return Web.renderTemplate("templates/playlists/show.vm",
                    "playlist", playlist);
        });

        /* UPDATE */
        get("/playlists/:id/edit", (req, resp) -> {
            Playlist playlist = Playlist.find(Integer.parseInt(req.params(":id")));
            return Web.renderTemplate("templates/playlists/edit.vm",
                    "playlist", playlist);
        });

        post("/playlists/:id", (req, resp) -> {
            Playlist playlist = Playlist.find(Integer.parseInt(req.params(":id")));
            Web.putValuesInto(playlist, "Name");
            if (playlist.update()) {
                Web.message("Updated Playlist!");
                return Web.redirect("/playlists/" + playlist.getPlaylistId());
            } else {
                Web.error("Could Not Update Playlist!");
                return Web.renderTemplate("templates/playlists/edit.vm",
                        "playlist", playlist);
            }
        });

        /* DELETE */
        get("/playlists/:id/delete", (req, resp) -> {
            Playlist playlist = Playlist.find(Integer.parseInt(req.params(":id")));
            playlist.delete();
            Web.message("Deleted Playlist " + playlist.getName());
            return Web.redirect("/playlists");
        });
    }
}
