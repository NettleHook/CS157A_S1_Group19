package app.search;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Constants;
import app.Database;
import app.api.ApiResponse;
import app.auth.UserSession;

public class SearchService {

    public static void getGuestResults(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> data = new HashMap<>();
        String[] ingredient = request.getParameterValues("ingredient-input");
        if (ingredient == null) {
            ingredient = new String[0];
        }
        List<String> ingredients = Arrays.stream(ingredient).map(String::trim).filter(s -> !s.isEmpty()) // drop blank inputs
                .collect(Collectors.toList());

        String categoryId = request.getParameter("food-cat");
        if (categoryId == null) {
            categoryId = "all";
        }
        String[] dietIds = request.getParameterValues("diet-cat");
        List<String> diets = new ArrayList<>();
        if (dietIds != null) {
            for (String dietId : dietIds) {
                for (Constants.Option option : Constants.DIETS) {
                    if (option.id().equals(dietId)) {
                        diets.add(option.text());
                        break;
                    }
                }
            }
        }

        String servSize = request.getParameter("serving-size");
        String prepTime = getTime(request.getParameter("prep-time-hours"), request.getParameter("prep-time-minutes"));
        String cookTime = getTime(request.getParameter("cook-time-hours"), request.getParameter("cook-time-minutes"));
        String calories = request.getParameter("calories");

        boolean inclusiveIngredients = !"exclusive".equals(request.getParameter("ingredient-mode"));
        boolean inclusiveDiets = "inclusive".equals(request.getParameter("diet-mode"));

        java.util.function.Function<String, String> esc = s
                -> s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        List<String> conditions = new ArrayList<>();
        List<String> params = new ArrayList<>();

        if (ingredients != null && !ingredients.isEmpty()) {
            String placeholders = String.join(", ", Collections.nCopies(ingredients.size(), "?"));
            if (inclusiveIngredients) {
                conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_ingredients WHERE ingredient_id IN (" + placeholders
                        + "))");
            } else {
                conditions.add("id IN (SELECT recipe_id FROM recipe_ingredients WHERE ingredient_id IN (" + placeholders
                        + ") GROUP BY recipe_id HAVING COUNT(*) = " + ingredients.size() + ")");
            }
            params.addAll(ingredients);
        }
        if (diets != null && !diets.isEmpty()) {
            String diet_placeholders = String.join(",", Collections.nCopies(diets.size(), "?"));
            if (inclusiveDiets) {
                conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_diets WHERE diet_id IN (SELECT id FROM diets WHERE name IN (" + diet_placeholders + ")))");

            } else {
                conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_diets WHERE diet_id IN (SELECT id FROM diets WHERE name IN (" + diet_placeholders + ")) GROUP BY recipe_id HAVING COUNT(*) = " + diets.size() + ")");
            }
            params.addAll(diets);
        }
        if (servSize != null && !servSize.isEmpty()) {
            conditions.add("serving_size >= ?");
            params.add(servSize);
        }
        if (!prepTime.equals("0")) {
            conditions.add("prep_time_min <= ?");
            params.add(prepTime);
        }
        if (!cookTime.equals("0")) {
            conditions.add("cook_time_min <= ?");
            params.add(cookTime);
        }
        if (calories != null && !calories.isEmpty()) {
            conditions.add("calories <= ?");
            params.add(calories);
        }
        // Build the final query
        String view = categoryId.equals("all") ? "recipe_summaries_likes" : categoryId + "_recipes";
        String query = "SELECT * FROM " + view;
        if (!conditions.isEmpty()) {
            query += " WHERE " + String.join(" AND ", conditions);
        }

        String results = new String();
        try (java.sql.Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(query);) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                results = "<table class = 'results' id = 'results' border='1'>" + "<tr>" + "<th onclick='sortTableAlphabetically(0)'>Recipe Name:</th>" + "<th onclick='sortTableNumerically(1)'>Serving Size:</th>"
                        + "<th onclick='sortTableNumerically(2)'>Prep Time:</th>" + "<th onclick='sortTableNumerically(3)'>Cook Time:</th>" + "<th onclick='sortTableNumerically(4)'>Calories:</th>" + "<th onclick='sortTableNumerically(5)'>Likes:</th>" + "<th>Bookmarks</th>" + "</tr>";

                do {
                    results += "<tr>" + "<td><a href = './recipe_page.jsp?rsid=" + esc.apply(rs.getString(1)) + "'>" + esc.apply(rs.getString(2)) + "</a></td>" + "<td>" + esc.apply(rs.getString(3)) + " </td>" + "<td>"
                            + esc.apply(rs.getString(4)) + " </td>" + "<td>" + esc.apply(rs.getString(5)) + " </td>" + "<td>" + esc.apply(rs.getString(6))
                            + " </td>" + "<td><div class = 'like-container' data-liked='false' onclick='toggleLike(this, " + rs.getInt(1) + ")' style='cursor:pointer;'> <img src='./assets/unliked.svg' alt = 'Likes'><span>" + rs.getInt(7) + "</span></td>" + "<td><img class = 'bookmark-container' data-bookmarked='false' src='./assets/unbookmarked.svg' alt = 'Bookmark' onclick='toggleBookmark(this, " + rs.getInt(1) + ")' style='cursor:pointer;'></td></tr>";
                } while (rs.next());
                results += "</table>";
            } else {
                results = "<p> No recipes found.<a href = './index.jsp'> Try another search </a></p>";
            }
            data.put("results", results);
            ApiResponse.write(response, 200, data);
            return;
        } catch (SQLException e) {
            ApiResponse.error(response, 400, e.getMessage());
            return;
        }
    }

    public static void getUserResults(UserSession userSession, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> data = new HashMap<>();
        String[] ingredient = request.getParameterValues("ingredient-input");
        if (ingredient == null) {
            ingredient = new String[0];
        }
        List<String> ingredients = Arrays.stream(ingredient).map(String::trim).filter(s -> !s.isEmpty()) // drop blank inputs
                .collect(Collectors.toList());

        String categoryId = request.getParameter("food-cat");
        if (categoryId == null) {
            categoryId = "all";
        }

        String[] dietIds = request.getParameterValues("diet-cat");
        List<String> diets = new ArrayList<>();
        if (dietIds != null) {
            for (String dietId : dietIds) {
                for (Constants.Option option : Constants.DIETS) {
                    if (option.id().equals(dietId)) {
                        diets.add(option.text());
                        break;
                    }
                }
            }
        }

        String servSize = request.getParameter("serving-size");
        String prepTime = getTime(request.getParameter("prep-time-hours"), request.getParameter("prep-time-minutes"));
        String cookTime = getTime(request.getParameter("cook-time-hours"), request.getParameter("cook-time-minutes"));
        String calories = request.getParameter("calories");

        boolean inclusiveIngredients = !"exclusive".equals(request.getParameter("ingredient-mode"));
        boolean inclusiveDiets = "inclusive".equals(request.getParameter("diet-mode"));

        java.util.function.Function<String, String> esc = s
                -> s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        List<String> conditions = new ArrayList<>();
        List<String> params = new ArrayList<>();
        params.add(userSession.getUserId() + "");
        params.add(userSession.getUserId() + "");

        if (ingredients != null && !ingredients.isEmpty()) {
            String placeholders = String.join(", ", Collections.nCopies(ingredients.size(), "?"));
            if (inclusiveIngredients) {
                conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_ingredients WHERE ingredient_id IN (" + placeholders
                        + "))");
            } else {
                conditions.add("id IN (SELECT recipe_id FROM recipe_ingredients WHERE ingredient_id IN (" + placeholders
                        + ") GROUP BY recipe_id HAVING COUNT(*) = " + ingredients.size() + ")");
            }
            params.addAll(ingredients);
        }
        if (diets != null && !diets.isEmpty()) {
            String diet_placeholders = String.join(",", Collections.nCopies(diets.size(), "?"));
            if (inclusiveDiets) {
                conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_diets WHERE diet_id IN (SELECT id FROM diets WHERE name IN (" + diet_placeholders + ")))");

            } else {
                conditions.add("id IN (SELECT DISTINCT recipe_id FROM recipe_diets WHERE diet_id IN (SELECT id FROM diets WHERE name IN (" + diet_placeholders + ")) GROUP BY recipe_id HAVING COUNT(*) = " + diets.size() + ")");
            }
            params.addAll(diets);
        }
        if (servSize != null && !servSize.isEmpty()) {
            conditions.add("serving_size >= ?");
            params.add(servSize);
        }
        if (!prepTime.equals("0")) {
            conditions.add("prep_time_min <= ?");
            params.add(prepTime);
        }
        if (!cookTime.equals("0")) {
            conditions.add("cook_time_min <= ?");
            params.add(cookTime);
        }
        if (calories != null && !calories.isEmpty()) {
            conditions.add("calories <= ?");
            params.add(calories);
        }
        // Build the final query
        String view = categoryId.equals("all") ? "recipe_summaries_likes" : categoryId + "_recipes";
        String query = "SELECT vew.*, IF(ISNULL(br.recipe_id), 0, 1) AS bookmarked, IF(ISNULL(lr.recipe_id), 0, 1) AS liked FROM " + view + " AS vew LEFT JOIN (SELECT recipe_id FROM bookmarked_recipes WHERE user_id = ?) br ON vew.id = br.recipe_id LEFT JOIN (SELECT recipe_id FROM liked_recipes WHERE user_id = ?) lr ON vew.id = lr.recipe_id";
        if (!conditions.isEmpty()) {
            query += " WHERE " + String.join(" AND ", conditions);
        }

        String results = new String();
        try (java.sql.Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(query);) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            System.out.println("Query: " + stmt);
            System.out.println("Params: " + params);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                results = "<table class = 'results' id = 'results' border='1'>" + "<tr>" + "<th onclick='sortTableAlphabetically(0)'>Recipe Name:</th>" + "<th onclick='sortTableNumerically(1)'>Serving Size:</th>"
                        + "<th onclick='sortTableNumerically(2)'>Prep Time:</th>" + "<th onclick='sortTableNumerically(3)'>Cook Time:</th>" + "<th onclick='sortTableNumerically(4)'>Calories:</th>" + "<th onclick='sortTableNumerically(5)'>Likes:</th>" + "<th>Bookmarks</th>" + "</tr>";

                do {
                    results += "<tr>" + "<td><a href = './recipe_page.jsp?rsid=" + rs.getInt(1) + "'>" + esc.apply(rs.getString(2)) + "</a></td>" + "<td>" + esc.apply(rs.getString(3)) + " </td>" + "<td>"
                            + esc.apply(rs.getString(4)) + " </td>" + "<td>" + esc.apply(rs.getString(5)) + " </td>" + "<td>" + esc.apply(rs.getString(6))
                            + " </td>";
                    results += "<td><div class = 'like-container' " + ((rs.getInt(9) > 0) ? "data-liked='true' onclick='toggleLike(this, " + rs.getInt(1) + ")' style='cursor:pointer;'> <img src='./assets/liked.svg'" : "data-liked='false' onclick='toggleLike(this, " + rs.getInt(1) + ")' style='cursor:pointer;'> <img src='./assets/unliked.svg'") + " alt = 'Likes'><span>" + rs.getInt(7) + "</span></div></td>";
                    results += "<td><img class = 'bookmark-container'" + ((rs.getInt(8) > 0) ? " data-bookmarked='true' src='./assets/bookmarked.svg'" : " data-bookmarked='false' src='./assets/unbookmarked.svg'") + " alt = 'Bookmark' onclick='toggleBookmark(this, " + rs.getInt(1) + ")' style='cursor:pointer;'></td></tr>";
                } while (rs.next());
                results += "</table>";
            } else {
                results = "<p> No recipes found.<a href = './index.jsp'> Try another search </a></p>";
            }
            data.put("results", results);
            ApiResponse.write(response, 200, data);
            return;
        } catch (SQLException e) {
            ApiResponse.error(response, 400, e.getMessage());
            return;
        }
    }

    public static void getFullRecipeGuest(HttpServletRequest request, HttpServletResponse response, int recipe_id) throws IOException {
        Map<String, String> data = new HashMap<>();
        java.util.function.Function<String, String> esc = s
                -> s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        String query1 = "SELECT rs.*, rf.description FROM recipe_summaries_likes rs, recipe_full rf WHERE rs.id = rf.recipe_id AND rs.id = ?";
        String query2 = "SELECT ri.ingredient_id, u.name, ri.amount FROM recipe_ingredients ri, units u WHERE ri.unit_id = u.id AND ri.recipe_id = ?";
        String query3 = "SELECT diets.name FROM diets, recipe_diets WHERE diets.id=recipe_diets.diet_id AND recipe_diets.recipe_id = ?";
        try (Connection con = Database.getConnection(); PreparedStatement stmt1 = con.prepareStatement(query1); PreparedStatement stmt2 = con.prepareStatement(query2); PreparedStatement stmt3 = con.prepareStatement(query3);) {

            stmt1.setInt(1, recipe_id);
            ResultSet recipe_rs = stmt1.executeQuery();

            //second query for ingredients + amounts
            stmt2.setInt(1, recipe_id);
            ResultSet ingredients_rs = stmt2.executeQuery();
            String ingredients_html = "";
            String amount = "";
            while (ingredients_rs.next()) {
                amount = ingredients_rs.getString(3);
                ingredients_html += "<tr><td>" + esc.apply(ingredients_rs.getString(1)) + "</td><td>"
                        + esc.apply(amount) + "</td><td>"
                        + esc.apply(ingredients_rs.getString(2)) + "</td></tr>";
            }

            stmt3.setInt(1, recipe_id);
            ResultSet diet_rs = stmt3.executeQuery();
            //stringify
            String diet_html = "<h3> Diet Compatibility: </h3> <p> ";
            while (diet_rs.next()) {
                diet_html += diet_rs.getString(1) + ", ";
            }
            if (diet_html.endsWith(", ")) {
                diet_html = diet_html.substring(0, diet_html.length() - 2);
            }
            diet_html += "</p>";
            String return_string = "";
            //Should only be one result from query1!
            if (recipe_rs.next()) {
                return_string += "<div id='full_recipe_stats'><div class = 'like-container' data-liked='false' onclick='toggleLike(this, "
                        + recipe_rs.getInt(1) + ")' style='cursor:pointer;'> <img src='./assets/unliked.svg' alt = 'Likes'><span>"
                        + recipe_rs.getInt(7) + "</span></div><img class = 'bookmark-container' data-bookmarked='false' src='./assets/unbookmarked.svg' alt = 'Bookmark' onclick='toggleBookmark(this, "
                        + recipe_rs.getInt(1) + ")' style='cursor:pointer;'></div>";
                return_string += "<div id='recipe_slip'><h1>" + esc.apply(recipe_rs.getString(2)) + " </h1><div class = 'recipe-metadata'><p> Serving Size: "
                        + esc.apply(recipe_rs.getString(3)) + "</p><p> Calories: " + esc.apply(recipe_rs.getString(6)) + "</p><p> Prep Time: " + esc.apply(recipe_rs.getString(4)) + " min </p><p> Cook Time: "
                        + esc.apply(recipe_rs.getString(5)) + " min </p></div><div  class='diet_blurb'>"
                        + diet_html + "</div>"
                        + "<div class = 'ingredients'><h3> Ingredients </h3> <table>" + ingredients_html
                        + "</table></div><div class = 'instructions'><h3> Steps: </h3><pre> " + esc.apply(recipe_rs.getString(8))
                        + " </pre></div></div>";

            } else {
                return_string = "<p> No recipes found.<a href = './index.jsp'> Try another search </a></p>";
            }
            data.put("results", return_string);
            ApiResponse.write(response, 200, data);
        } catch (SQLException e) {
            ApiResponse.error(response, 500, "SQLException caught: " + e.getMessage());
        }
    }

    public static void getFullRecipeUser(HttpServletRequest request, HttpServletResponse response, int recipe_id, UserSession userSession) throws IOException {
        Map<String, String> data = new HashMap<>();
        java.util.function.Function<String, String> esc = s
                -> s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        String query1 = "SELECT vew.*, IF(ISNULL(br.recipe_id), 0, 1) AS bookmarked, IF(ISNULL(lr.recipe_id), 0, 1) AS liked, rf.description FROM recipe_summaries_likes AS vew LEFT JOIN (SELECT recipe_id FROM bookmarked_recipes WHERE user_id = ?) br ON vew.id = br.recipe_id LEFT JOIN (SELECT recipe_id FROM liked_recipes WHERE user_id = ?) lr ON vew.id = lr.recipe_id JOIN recipe_full rf ON vew.id = rf.recipe_id WHERE vew.id = ?";
        String query2 = "SELECT ri.ingredient_id, u.name, ri.amount FROM recipe_ingredients ri, units u WHERE ri.unit_id = u.id AND ri.recipe_id = ?";
        String query3 = "SELECT diets.name FROM diets, recipe_diets WHERE diets.id=recipe_diets.diet_id AND recipe_diets.recipe_id = ?";
        try (Connection con = Database.getConnection(); PreparedStatement stmt1 = con.prepareStatement(query1); PreparedStatement stmt2 = con.prepareStatement(query2); PreparedStatement stmt3 = con.prepareStatement(query3);) {

            stmt1.setInt(1, userSession.getUserId());
            stmt1.setInt(2, userSession.getUserId());
            stmt1.setInt(3, recipe_id);
            System.out.println(stmt1);
            ResultSet recipe_rs = stmt1.executeQuery();

            //second query for ingredients + amounts
            stmt2.setInt(1, recipe_id);
            ResultSet ingredients_rs = stmt2.executeQuery();
            String ingredients_html = "";
            String amount = "";
            while (ingredients_rs.next()) {
                amount = ingredients_rs.getString(3);
                ingredients_html += "<tr><td>" + esc.apply(ingredients_rs.getString(1)) + "</td><td>"
                        + esc.apply(amount) + "</td><td>"
                        + esc.apply(ingredients_rs.getString(2)) + "</td></tr>";
            }

            stmt3.setInt(1, recipe_id);
            ResultSet diet_rs = stmt3.executeQuery();
            //stringify
            String diet_html = "<h3> Diet Compatibility: </h3> <p> ";
            while (diet_rs.next()) {
                diet_html += diet_rs.getString(1) + ", ";
            }
            if (diet_html.endsWith(", ")) {
                diet_html = diet_html.substring(0, diet_html.length() - 2);
            }
            diet_html += "</p>";
            String return_string = "";
            //Should only be one result from query1!
            if (recipe_rs.next()) {
                return_string += "<div id='full_recipe_stats'><div class = 'like-container' "
                        + ((recipe_rs.getInt(9) > 0) ? "data-liked='true' onclick='toggleLike(this, " + recipe_rs.getInt(1) + ")' style='cursor:pointer;'> <img src='./assets/liked.svg'" : "data-liked='false' onclick='toggleLike(this, "
                        + recipe_rs.getInt(1) + ")' style='cursor:pointer;'> <img src='./assets/unliked.svg'")
                        + " alt = 'Likes'><span>" + recipe_rs.getInt(7) + "</span></div>"
                        + "<img class = 'bookmark-container'" + ((recipe_rs.getInt(8) > 0) ? " data-bookmarked='true' src='./assets/bookmarked.svg'" : " data-bookmarked='false' src='./assets/unbookmarked.svg'")
                        + " alt = 'Bookmark' onclick='toggleBookmark(this, " + recipe_rs.getInt(1) + ")' style='cursor:pointer;'></div>";
                return_string += "<div id='recipe_slip'><h1>" + esc.apply(recipe_rs.getString(2)) + " </h1><div class = 'recipe-metadata'><p> Serving Size: "
                        + esc.apply(recipe_rs.getString(3)) + "</p><p> Calories: " + esc.apply(recipe_rs.getString(6)) + "</p><p> Prep Time: " + esc.apply(recipe_rs.getString(4)) + " min </p><p> Cook Time: "
                        + esc.apply(recipe_rs.getString(5)) + " min </p></div><div  class='diet_blurb'>"
                        + diet_html + "</div>";
                return_string += "<div id='all_ingredients'><div class = 'ingredients'><h3> Ingredients </h3> <table>" + ingredients_html
                        + "</table></div>"
                        + "<div id='my_ingredients'>"
                        + "<div class='header' onclick='toggleIngredientDropdown()'>" + "<h2>My Ingredients <span id='chevron' class='chevron'>▼</span></h2>"
                        + "</div><div id='panel' style='display: none;'>"
                        + "<div id='list-view'>"
                        + "<div id='ingredients-container'><p>Your saved ingredients will appear here.</p></div></div>"
                        + "<button class='add-ingredient-btn' onclick='showAddIngredient(event)'>+</button>"
                        + "<div id='add-view' style='display: none;'>"
                        + "<form id='add-ingredient-form' class='add-ingredient-form' onsubmit='addIngredient(event)'>"
                        + "<input type='text' name='ingredientId' placeholder='Enter ingredient name' required />"
                        + "<input type='number' name='amount'       placeholder='Enter amount (if applicable)' />"
                        + "<select id='ingredient-input-unit' name='unitId' required></select>"
                        + "<input type='submit' value='Add' /></form><div id='error' style='display: none; color: red;'></div></div></div></div></div>"
                        + "<div class = 'instructions'><h3> Steps: </h3><pre> " + esc.apply(recipe_rs.getString(10))
                        + " </pre></div></div>";

            } else {
                return_string = "<p> No recipes found.<a href = './index.jsp'> Try another search </a></p>";
            }
            data.put("results", return_string);
            ApiResponse.write(response, 200, data);
        } catch (SQLException e) {
            ApiResponse.error(response, 500, "SQLException caught: " + e.getMessage());
        }
    }

    private static String getTime(String hour, String minute) {
        int minutes = 0;
        if (hour != null && !hour.isEmpty()) {
            minutes = Integer.parseInt(hour) * 60;
        }
        if (minute != null && !minute.isEmpty()) {
            minutes += Integer.parseInt(minute);
        }
        return minutes + "";
    }
}
