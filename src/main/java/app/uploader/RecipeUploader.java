package app.uploader;

import app.Database;
import app.api.ApiResponse;
import app.object.Ingredient;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/upload")
public class RecipeUploader extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map<String, String> data = new HashMap<>();
        // Pull each field from the request
        String recipe_name = req.getParameter("recipe_name");
        String serving_size = req.getParameter("serving-size");
        String prep_hours = req.getParameter("prep-time-hours");
        String prep_min = req.getParameter("prep-time-minutes");
        String cook_hours = req.getParameter("cook-time-hours");
        String cook_min = req.getParameter("cook-time-minutes");
        String calories = req.getParameter("calories");
        String[] ingredient_names = req.getParameterValues("ingredient-input-name");
        String[] ingredient_amount = req.getParameterValues("ingredient-input-amt");
        String[] ingredient_units = req.getParameterValues("ingredient-input-unit");
	    String categoryId = req.getParameter("food-cat");
	    String[] dietIds = req.getParameterValues("diet-cat");
        String[] steps = req.getParameterValues("step");
        int recipeId = 0;
        try {
            List<Object> recipe_summary = InputVerifier.verify_summary(recipe_name, serving_size, prep_hours, prep_min, cook_hours, cook_min, calories);
            List<Ingredient>recipe_ingredients = InputVerifier.verify_ingredients(ingredient_names, ingredient_amount, ingredient_units);
            String category = InputVerifier.verify_category(categoryId);
            List<String>diets = InputVerifier.verify_diets(dietIds);
            String description = InputVerifier.verify_steps(steps);
            recipeId = commitTransaction(recipe_summary, recipe_ingredients, category, diets, description);            
        } catch (IllegalArgumentException e) {
            data.put("error", e.getMessage());
            ApiResponse.write(res, 400, data);
            return;
        }
        data.put("message", "Recipe received successfully.");
        data.put("resid", recipeId + "");
        ApiResponse.write(res, 200, data);
    }
    private static int commitTransaction(List<Object> recipe_summary, List<Ingredient> ingredients, String category, List<String> diets, String description){
        java.sql.Connection con = null;
        try{
            con = Database.getConnection();
            //Need to commit as a transaction
            con.setAutoCommit(false);

            //recipe_summaries is first:
            PreparedStatement summ_stmt = con.prepareStatement("INSERT INTO recipe_summaries (name, serving_size, prep_time_min, cook_time_min, calories) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            //bind
            summ_stmt.setString(1, (String) recipe_summary.get(0));
            if ((Integer) recipe_summary.get(1) == null) {
                summ_stmt.setNull(2, java.sql.Types.INTEGER);
            } else {
                summ_stmt.setInt(2, (Integer) recipe_summary.get(1));
            }
            summ_stmt.setInt(3, (int) recipe_summary.get(2));
            summ_stmt.setInt(4, (int) recipe_summary.get(3));
            if ((Integer) recipe_summary.get(4) == null) {
                summ_stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                summ_stmt.setInt(5, (Integer) recipe_summary.get(4));
            }

            summ_stmt.executeUpdate();

            ResultSet generatedKeys = summ_stmt.getGeneratedKeys();
            int recipeId = -1;
            if (generatedKeys.next()) {
                recipeId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve generated recipe ID");
            }

            //recipe_full
            PreparedStatement full_stmt = con.prepareStatement("INSERT INTO recipe_full (recipe_id, description) VALUES (?, ?)");
            full_stmt.setInt(1, recipeId);
            full_stmt.setString(2, description);
            full_stmt.executeUpdate();

            //recipe_diets
            PreparedStatement diet_stmt = con.prepareStatement("INSERT INTO recipe_diets (recipe_id, diet_id) SELECT ?, id FROM diets WHERE name = ?");
            for (String diet : diets) {
                diet_stmt.setInt(1, recipeId);
                diet_stmt.setString(2, diet);
                diet_stmt.addBatch();
            }
            diet_stmt.executeBatch();

            //recipe_category
            PreparedStatement cat_stmt = con.prepareStatement("INSERT INTO recipe_categories (recipe_id, category_id) SELECT ?, id FROM categories WHERE name = ?");
            cat_stmt.setInt(1, recipeId);
            cat_stmt.setString(2, category);
            cat_stmt.executeUpdate();

            //add any new ingredients:
            PreparedStatement new_ingredient_stmt = con.prepareStatement("INSERT IGNORE INTO ingredients (name) VALUES (?)");
            for (Ingredient ingredient : ingredients) {
                new_ingredient_stmt.setString(1, ingredient.name());
                new_ingredient_stmt.addBatch();
            }
            new_ingredient_stmt.executeBatch();


            //recipe_ingredients
            PreparedStatement ingredient_stmt = con.prepareStatement("INSERT INTO recipe_ingredients (recipe_id, ingredient_id, unit_id, amount) VALUES ( ?, ?, (SELECT id FROM units WHERE name = ?), ?)");
             for (Ingredient ingredient : ingredients) {
                ingredient_stmt.setInt(1, recipeId);
                ingredient_stmt.setString(2, ingredient.name());
                ingredient_stmt.setString(3, ingredient.unit());      
                if (ingredient.amount() != null) {
                    ingredient_stmt.setDouble(4, ingredient.amount());
                }else{
                    ingredient_stmt.setNull(4, java.sql.Types.DOUBLE);
                }
                ingredient_stmt.addBatch();
            }
            ingredient_stmt.executeBatch();
            con.commit();
            return recipeId;
        }catch (SQLException e) {
            // Something failed - roll back everything
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Transaction failed, changes rolled back: " + e.getMessage());
        }
        finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
}