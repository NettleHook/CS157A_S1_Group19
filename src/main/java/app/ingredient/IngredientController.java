package app.ingredient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.api.ApiMessage;
import app.api.ApiResponse;
import app.auth.AuthMiddleware;
import app.auth.AuthService;
import app.auth.UserSession;
import app.ingredient.IngredientService.Unit;
import app.ingredient.IngredientService.UserIngredient;
import app.utils.JsonUtils;

public class IngredientController {
    public static void getIngredients(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            List<String> ingredients = IngredientService.getIngredients();
            ApiResponse.write(res, 200, ingredients);
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        } catch (IOException e) {
            ApiResponse.error(res, 400, ApiMessage.INVALID_JSON);
        }
    }

    public static void getUserIngredients(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);

        if (userSession == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }

        try {
            List<UserIngredient> ingredients = IngredientService.getUserIngredients(userSession.getUserId());
            ApiResponse.write(res, 200, ingredients);
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        }
    }

    public record ParsedUserIngredient(String ingredientId, int unitId, Double amount) {}

    public static void postUserIngredient(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);

        if (userSession == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }

        try {
            var ingredient = JsonUtils.MAPPER.readValue(
                req.getReader(), 
                ParsedUserIngredient.class
            );
            if (ingredient.amount != null) {
                IngredientService.insertUserIngredient(
                    userSession.getUserId(), 
                    ingredient.ingredientId(), 
                    ingredient.unitId(), 
                    ingredient.amount()
                );
            } else {
                IngredientService.insertUserIngredient(
                    userSession.getUserId(), 
                    ingredient.ingredientId(), 
                    ingredient.unitId(), 
                    0
                );
            }
            ApiResponse.write(res, 201, "Ingredient added.");
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        }
    }

    public static void deleteUserIngredient(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);

        if (userSession == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }

        try {
            String ingredientId = req.getParameter("id");
            int rows = IngredientService.deleteUserIngredient(
                userSession.getUserId(), 
                ingredientId
            );
            if (rows > 0) {
                ApiResponse.write(res, 200, "Ingredient deleted.");
            } else {
                ApiResponse.error(res, 404, "Ingredient not found.");
            }
            
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        }
    }

    public static void getUnits(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            List<Unit> units = IngredientService.getUnits();
            ApiResponse.write(res, 200, units);
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        } catch (IOException e) {
            ApiResponse.error(res, 400, ApiMessage.INVALID_JSON);
        }
    }
}
