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
            WHERE u.user_id = ?
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

    public static void insertUserIngredient(int userId, int ingredientId, int unitId, double amount) throws SQLException {
        String query = """
            INSERT INTO user_ingredient_lists (user_id, ingredient_id, unit_id, amount)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, ingredientId);
                stmt.setInt(3, unitId);
                stmt.setDouble(4, amount);
                stmt.executeUpdate();
            }
        }
    }
}
