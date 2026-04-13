package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import app.object.Ingredient;
import app.object.Recipe;
import app.object.User;
import io.github.cdimascio.dotenv.Dotenv;

public class Seeder {
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();

        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            con.setAutoCommit(false);
            try {
                insertUsers(con);
                insertRecipes(con);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertUsers(Connection con) throws Exception {
        List<User> userList = Parser.parseUsers();
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            for (User user : userList) {
                stmt.setString(1, user.username());
                stmt.setString(2, user.password());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private static void insertRecipes(Connection con) throws Exception {
        List<Recipe> recipeList = Parser.parseRecipes();
        for (Recipe recipe : recipeList) {
            int recipeId = insertRecipeSummary(con, recipe);
            insertRecipeFull(con, recipeId, recipe);
            insertRecipeDiets(con, recipeId, recipe);
            insertRecipeCategories(con, recipeId, recipe);
            insertRecipeIngredients(con, recipeId, recipe);
            insertUploadedRecipes(con, recipeId, recipe);
        }
    }

    private static int insertRecipeSummary(Connection con, Recipe recipe) throws Exception {
        String query = """
            INSERT INTO recipe_summaries (
                name, 
                serving_size, 
                prep_time_min, 
                cook_time_min, 
                calories
            ) 
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, recipe.name());
            stmt.setInt(2, recipe.servingSize());
            stmt.setInt(3, recipe.prepTime());
            stmt.setInt(4, recipe.cookTime());
            
            if (recipe.calories() == null) {
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(5, recipe.calories());
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating recipe failed, no ID obtained.");
                }
            }
        }
    }

    private static void insertRecipeFull(Connection con, int recipeId, Recipe recipe) throws SQLException {
        String query = "INSERT INTO recipe_full (recipe_id, description) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, recipeId);
            stmt.setString(2, recipe.recipeDetails().description());
            stmt.executeUpdate();
        }
    }

    private static void insertRecipeCategories(Connection con, int recipeId, Recipe recipe) throws SQLException {
        String query = """
            INSERT INTO recipe_categories (recipe_id, category_id) 
            SELECT ?, id
            FROM categories
            WHERE name = ?
        """;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            for (String category : recipe.categories()) {
                stmt.setInt(1, recipeId);
                stmt.setString(2, category);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private static void insertRecipeDiets(Connection con, int recipeId, Recipe recipe) throws SQLException {
        String query = """
            INSERT INTO recipe_diets (recipe_id, diet_id) 
            SELECT ?, id
            FROM diets
            WHERE name = ?
        """;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            for (String diet : recipe.diets()) {
                stmt.setInt(1, recipeId);
                stmt.setString(2, diet);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private static void insertRecipeIngredients(Connection con, int recipeId, Recipe recipe) throws SQLException {
        String query = """
            INSERT INTO recipe_ingredients (recipe_id, ingredient_id, unit_id, amount)
            VALUES (
                ?,
                ?,
                (SELECT id FROM units WHERE name = ?),
                ?
            )
        """;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            for (Ingredient ingredient : recipe.ingredients()) {
                stmt.setInt(1, recipeId);
                stmt.setString(2, ingredient.name());
                stmt.setString(3, ingredient.unit());
                
                if (ingredient.amount() != null) {
                    stmt.setDouble(4, ingredient.amount());
                }
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private static void insertUploadedRecipes(Connection con, int recipeId, Recipe recipe) throws SQLException {
        String query = """
            INSERT INTO uploaded_recipes (user_id, recipe_id)
            SELECT id, ?
            FROM users
            WHERE username = ?
        """;

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, recipeId);
            stmt.setString(2, recipe.username());
            stmt.executeUpdate();
        }
    }
}