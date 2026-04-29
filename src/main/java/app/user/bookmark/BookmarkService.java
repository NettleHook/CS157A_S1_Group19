package app.user.bookmark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.Database;

public class BookmarkService {

    public static List<String> getBookmarks(int userId) throws SQLException {
        String query = """
            SELECT rs.name 
            FROM bookmarked_recipes br
            JOIN recipe_summaries rs ON br.recipe_id = rs.id
            WHERE br.user_id = ?
        """;

        List<String> bookmarks = new ArrayList<>();
        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        bookmarks.add(rs.getString("name"));
                    }
                }
            }
        }
        return bookmarks;
    }

    public static void addBookmark(int userId, int recipeId) throws SQLException {
        String query = "INSERT INTO bookmarked_recipes (user_id, recipe_id) VALUES (?, ?)";

        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                // 4. what two parameters need to be set?
                stmt.setInt(1, userId);
                stmt.setInt(2, recipeId);
                stmt.executeUpdate();
            }
        }
    }

    public static void removeBookmark(int userId, int recipeId) throws SQLException {
        String query = "DELETE FROM bookmarked_recipes WHERE user_id = ? AND recipe_id = ?";

        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, recipeId);
                stmt.executeUpdate();
            }
        }
    }
}