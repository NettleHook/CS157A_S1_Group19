package app.user.bookmark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.Database;

public class BookmarkService {

    //Get all bookmarks for a user
    public static List<String> getBookmarks(String username) throws SQLException {
        String query = "SELECT recipieName FROM bookmarkedrecipe WHERE userID = ?";

        List<String> bookmarks = new ArrayList<>();
        try (Connection con = Database.getConnection()) {
            //Tries to prep the statement query
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, username);
                //Executes once it achieves a query
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        bookmarks.add(rs.getString("recipeName"));
                    }
                }
            }
        }
        return bookmarks;
    }

    // ADD a bookmark
    public static void addBookmark(String username, String recipeName) throws SQLException {
        String query = "INSERT INTO bookmarkedrecipes (userID, recipeName) VALUES (?, ?)";

        // Gets connection again to the database to see
        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, recipeName);
                stmt.executeUpdate();
            }
        }
    }

    // REMOVE a bookmark
    public static void removeBookmark(String username, String recipeName) throws SQLException {
        String query = "DELETE FROM bookmarkedrecipes WHERE userID = ? AND recpieName = ?";
        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, recipeName);
                stmt.executeUpdate();
            }
        }
    }
}
