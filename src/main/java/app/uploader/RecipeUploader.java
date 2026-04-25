package app.uploader;

import app.api.ApiResponse;
import app.object.Ingredient;
import java.io.IOException;
import java.util.Arrays;
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
        System.out.println("recipe_name: " + recipe_name);
        System.out.println("serving_size: " + serving_size);
        System.out.println("prep_hours: " + prep_hours);
        System.out.println("prep_min: " + prep_min);
        System.out.println("cook_hours: " + cook_hours);
        System.out.println("cook_min: " + cook_min);
        System.out.println("calories: " + calories);
        System.out.println("ingredient_names: " + Arrays.toString(ingredient_names));
        System.out.println("ingredient_amount: " + Arrays.toString(ingredient_amount));
        System.out.println("ingredient_units: " + Arrays.toString(ingredient_units));
        System.out.println("categoryId: " + categoryId);
        System.out.println("dietIds: " + Arrays.toString(dietIds));
        System.out.println("steps: " + Arrays.toString(steps));

        try {
            List<Object> recipe_summary = InputVerifier.verify_summary(recipe_name, serving_size, prep_hours, prep_min, cook_hours, cook_min, calories);
            System.out.println(recipe_summary);
            List<Ingredient>recipe_ingredients = InputVerifier.verify_ingredients(ingredient_names, ingredient_amount, ingredient_units);
            System.out.println(recipe_ingredients);
            String category = InputVerifier.verify_category(categoryId);
            System.out.println(category);
            List<String>diets = InputVerifier.verify_diets(dietIds);
            System.out.println(diets);
        } catch (IllegalArgumentException e) {
            data.put("error", e.getMessage());
            ApiResponse.write(res, 400, data);
            return;
        }
        data.put("message", "Recipe received successfully");
        ApiResponse.write(res, 200, data);
    }
}