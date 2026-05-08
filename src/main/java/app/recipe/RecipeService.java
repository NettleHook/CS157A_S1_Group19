package app.recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.Database;

public class RecipeService {
    public record UploadedRecipe(int id, String name) {}
    public record LikedRecipe(int id, String name) {}
    public record BookmarkedRecipe(int id, String name) {}

    public static List<UploadedRecipe> getUploadedRecipes(int userId) throws SQLException {
        String query = """
            SELECT rs.id, rs.name
            FROM uploaded_recipes ur
            JOIN recipe_summaries rs ON rs.id = ur.recipe_id
            WHERE ur.user_id = ?
        """;

        List<UploadedRecipe> recipes = new ArrayList<>();
        try (Connection con = Database.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet res = stmt.executeQuery()) {
                while (res.next()) {
                    recipes.add(new UploadedRecipe(
                        res.getInt("id"),
                        res.getString("name")
                    ));
                }
            }
        }
        return recipes;
    }

    public static List<LikedRecipe> getLikedRecipes(int userId) throws SQLException {
        String query = """
            SELECT rs.id, rs.name
            FROM liked_recipes lr
            JOIN recipe_summaries rs ON rs.id = lr.recipe_id
            WHERE lr.user_id = ?
        """;

        List<LikedRecipe> recipes = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet res = stmt.executeQuery()) {
                while (res.next()) {
                    recipes.add(new LikedRecipe(
                        res.getInt("id"),
                        res.getString("name")
                    ));
                }
            }
        }
        return recipes;
    }

    public static boolean toggleLike(int userId, int recipeId) throws SQLException {
    if (isLiked(userId, recipeId)) {
            removeLike(userId, recipeId);
            return false;
        } else {
            addLike(userId, recipeId);
            return true;
        }
    }

    private static boolean isLiked(int userId, int recipeId) throws SQLException {
    String query = "SELECT 1 FROM liked_recipes WHERE user_id = ? AND recipe_id = ?";
    try (Connection con = Database.getConnection();
        PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
        try (ResultSet res = stmt.executeQuery()) {
            return res.next();
            }
        }
    }

    private static void addLike(int userId, int recipeId) throws SQLException {
    String query = "INSERT IGNORE INTO liked_recipes(user_id, recipe_id) VALUES (?, ?)";
    try (Connection con = Database.getConnection();
        PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            stmt.executeUpdate();
        }
    }

    private static void removeLike(int userId, int recipeId) throws SQLException {
    String query = "DELETE FROM liked_recipes WHERE user_id = ? AND recipe_id = ?";
    try (Connection con = Database.getConnection();
        PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            stmt.executeUpdate();
        }
    }

    public static List<BookmarkedRecipe> getBookmarkedRecipes(int userId) throws SQLException {
    String query = """
        SELECT rs.id, rs.name
        FROM bookmarked_recipes br
        JOIN recipe_summaries rs ON rs.id = br.recipe_id
        WHERE br.user_id = ?
    """;

    List<BookmarkedRecipe> recipes = new ArrayList<>();
    try (Connection con = Database.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setInt(1, userId);
        try (ResultSet res = stmt.executeQuery()) {
            while (res.next()) {
                recipes.add(new BookmarkedRecipe(
                    res.getInt("id"),
                    res.getString("name")
                ));
            }
        }
    }
    return recipes;
}

    public static boolean toggleBookmark(int userId, int recipeId) throws SQLException {
        if (isBookmarked(userId, recipeId)) {
            removeBookmark(userId, recipeId);
            return false;
        } else {
            addBookmark(userId, recipeId);
            return true;
        }
    }

    private static boolean isBookmarked(int userId, int recipeId) throws SQLException {
        String query = "SELECT 1 FROM bookmarked_recipes WHERE user_id = ? AND recipe_id = ?";
        try (Connection con = Database.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            try (ResultSet res = stmt.executeQuery()) {
                return res.next();
            }
        }
    }

    private static void addBookmark(int userId, int recipeId) throws SQLException {
        String query = "INSERT IGNORE INTO bookmarked_recipes(user_id, recipe_id) VALUES (?, ?)";
        try (Connection con = Database.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            stmt.executeUpdate();
        }
    }

    private static void removeBookmark(int userId, int recipeId) throws SQLException {
        String query = "DELETE FROM bookmarked_recipes WHERE user_id = ? AND recipe_id = ?";
        try (Connection con = Database.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipeId);
            stmt.executeUpdate();
        }
    }
}
