package app.recipe;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.exc.StreamWriteException;

public class RecipeRoutes {
    public static void handleGetRoutes(HttpServletRequest req, HttpServletResponse res) throws StreamWriteException, IOException {
        String path = req.getPathInfo();

        if (path == null) return;

        switch (path) {
            case "/me/recipes" -> RecipeController.getUploadedRecipes(req, res);
        }
    }
}
