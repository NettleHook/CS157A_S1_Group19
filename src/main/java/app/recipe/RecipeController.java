package app.recipe;

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
}
