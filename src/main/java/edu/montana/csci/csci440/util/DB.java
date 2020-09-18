package edu.montana.csci.csci440.util;

import edu.montana.csci.csci440.model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class DB {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:db/chinook.db");
    }

    public static void reset() throws IOException {
        Path dbPath = Paths.get("db/chinook.db");
        Path backupPath = Paths.get("db/backup/original.db");
        if (Files.exists(dbPath)) {
            Files.copy(backupPath, dbPath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            System.err.println("Could not find DB file!");
        }
    }

    public static long getLastID() {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT last_insert_rowid() as ID")) {
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                return results.getLong("ID");
            } else {
                throw new IllegalStateException("Could not get last ID");
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
