package app.recipe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.api.ApiMessage;
import app.api.ApiResponse;
import app.auth.AuthMiddleware;
import app.auth.AuthService;
import app.auth.UserSession;
import app.recipe.RecipeService.BookmarkedRecipe;
import app.recipe.RecipeService.LikedRecipe;
import app.recipe.RecipeService.UploadedRecipe;

public class RecipeController {
    public static void getUploadedRecipes(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);

        if (userSession == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }

        try {
            List<UploadedRecipe> recipes = RecipeService.getUploadedRecipes(userSession.getUserId());
            ApiResponse.write(res, 200, recipes);
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        }
    }

    public static void getLikedRecipes(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String sessionId = AuthMiddleware.getSessionId(req, res);
    UserSession userSession = AuthService.getUserSession(sessionId);

    if (userSession == null) {
        ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
        return;
    }

    try {
        List<LikedRecipe> recipes = RecipeService.getLikedRecipes(userSession.getUserId());
        ApiResponse.write(res, 200, recipes);
    } catch (SQLException e) {
        ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        }
    }

    public static void toggleLike(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);

        if (userSession == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }

        String recipeIdParam = req.getParameter("recipeId");
        if (recipeIdParam == null) {
            ApiResponse.error(res, 400, "Missing recipeId parameter.");
            return;
        }

        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            boolean nowLiked = RecipeService.toggleLike(userSession.getUserId(), recipeId);
            Map<String, Object> data = new HashMap<>();
            data.put("liked", nowLiked);
            ApiResponse.write(res, 200, data);

        } catch (NumberFormatException e) {
            ApiResponse.error(res, 400, "Invalid recipeId.");
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        }   
    }

    public static void getBookmarkedRecipes(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String sessionId = AuthMiddleware.getSessionId(req, res);
    UserSession userSession = AuthService.getUserSession(sessionId);

    if (userSession == null) {
        ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
        return;
    }

    try {
        List<BookmarkedRecipe> recipes = RecipeService.getBookmarkedRecipes(userSession.getUserId());
        ApiResponse.write(res, 200, recipes);
    } catch (SQLException e) {
        ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
    }
}

public static void toggleBookmark(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);

        if (userSession == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }

        String recipeIdParam = req.getParameter("recipeId");
        if (recipeIdParam == null) {
            ApiResponse.error(res, 400, "Missing recipeId parameter.");
            return;
        }

        try {
            int recipeId = Integer.parseInt(recipeIdParam);
            boolean nowBookmarked = RecipeService.toggleBookmark(userSession.getUserId(), recipeId);
            Map<String, Object> data = new HashMap<>();
            data.put("bookmarked", nowBookmarked);
            ApiResponse.write(res, 200, data);
        } catch (NumberFormatException e) {
            ApiResponse.error(res, 400, "Invalid recipeId.");
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        }
    }
}
