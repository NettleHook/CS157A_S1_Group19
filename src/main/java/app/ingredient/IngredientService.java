package app.ingredient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.Database;

public class IngredientService {
    public static List<String> getIngredients() throws SQLException {
        String query = """
            SELECT name
            FROM ingredients
        """;

        List<String> ingredients = new ArrayList<>();
        try (Connection con = Database.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet res = stmt.executeQuery()) {

            while (res.next()) {
                ingredients.add(res.getString("name"));
            }
        }
        return ingredients;
    }

    public record UserIngredient(String name, String unit, double amount) {}

    public static List<UserIngredient> getUserIngredients(int userId) throws SQLException {
        String query = """
            SELECT i.name AS ingredient, u.name as unit, uil.amount
            FROM user_ingredient_lists uil
            JOIN ingredients i ON i.name = uil.ingredient_id
            JOIN units u ON u.id = uil.unit_id
            WHERE uil.user_id = ?
        """;

        List<UserIngredient> ingredients = new ArrayList<>();
        try (Connection con = Database.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet res = stmt.executeQuery()) {
                while (res.next()) {
                    ingredients.add(new UserIngredient(
                        res.getString("ingredient"),
                        res.getString("unit"),
                        res.getDouble("amount")
                    ));
                }
            }
        }
        return ingredients;
    }

    public static void insertIngredient(String ingredientId) throws SQLException {
        String query = """
            INSERT INTO ingredients (name)
            VALUES (?)
        """;

        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, ingredientId);
                stmt.executeUpdate();
            }
        }
    }

    public static void insertIngredient(String ingredientId, Connection con) throws SQLException {
        String query = """
            INSERT IGNORE INTO ingredients (name)
            VALUES (?)
        """;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, ingredientId);
            stmt.executeUpdate();
        }
    }

    public static void insertUserIngredient(int userId, String ingredientId, int unitId, double amount) throws SQLException {
        String query = """
            INSERT INTO user_ingredient_lists (user_id, ingredient_id, unit_id, amount)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                amount = VALUES(amount),
                unit_id = VALUES(unit_id)
        """;

        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);
            try {
                insertIngredient(ingredientId, con);

                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, userId);
                    stmt.setString(2, ingredientId);
                    stmt.setInt(3, unitId);
                    stmt.setDouble(4, amount);
                    stmt.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }   
        }
    }

    public static int deleteUserIngredient(int userId, String ingredientId) throws SQLException {
        String query = """
            DELETE FROM user_ingredient_lists
            WHERE user_id = ? AND ingredient_id = ?
        """;

        int rows;
        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setString(2, ingredientId);
                rows = stmt.executeUpdate();
            }
        }
        return rows;
    }

    public record Unit(int id, String name) {}

    public static List<Unit> getUnits() throws SQLException {
        String query = """
            SELECT id, name
            FROM units
        """;

        List<Unit> units = new ArrayList<>();
        try (Connection con = Database.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet res = stmt.executeQuery()) {

            while (res.next()) {
                units.add(new Unit(res.getInt("id"), res.getString("name")));
            }
        }
        return units;
    }
 }
