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
}
