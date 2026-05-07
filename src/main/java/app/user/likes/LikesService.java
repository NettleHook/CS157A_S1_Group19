package app.user.likes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import app.Database;

public class LikesService {

    public static Map<String, String> getLikedRecipes(int userId) throws SQLException {
        String query = """
                    SELECT rs.id, rs.name
                    FROM liked_recipes lr
                    JOIN recipe_summaries rs ON lr.recipe_id = rs.id
                    WHERE lr.user_id = ?
                """;
        Map<String, String> likes = new HashMap<>();
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    likes.put(rs.getString(1), rs.getString(2));
                }
            }
        }
        return likes;
    }

    public static int getLikes(int recipeId) {
        String query = "SELECT likes FROM likes_tracker WHERE recipe_id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

    public static void addLike(int userId, int recipeId) throws SQLException {
        String query = "INSERT INTO liked_recipes (user_id, recipe_id) VALUES (?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            stmt.executeUpdate();
        }
    }

    public static void removeLike(int userId, int recipeId) throws SQLException {
        String query = "DELETE FROM liked_recipes WHERE user_id = ? AND recipe_id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            stmt.executeUpdate();
        }
    }
}