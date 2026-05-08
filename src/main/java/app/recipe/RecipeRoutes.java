package app.recipe;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.exc.StreamWriteException;

public class RecipeRoutes {
    public static void handleGetRoutes(HttpServletRequest req, HttpServletResponse res) throws StreamWriteException, IOException {
        String path = req.getPathInfo();
        System.out.println("RecipeRoutes path: " + path);  //Used as testing to see errors in teriminal

        if (path == null) return;

        switch (path) {
            case "/me/recipes" -> RecipeController.getUploadedRecipes(req, res);
            case "/me/liked"   -> RecipeController.getLikedRecipes(req, res);
            case "/me/bookmarked" -> RecipeController.getBookmarkedRecipes(req, res);
        }
    }

    public static void handlePostRoutes(HttpServletRequest req, HttpServletResponse res) throws StreamWriteException, IOException {
        String path = req.getPathInfo();

        if (path == null) return;

        switch (path) {
            case "/me/liked/toggle" -> RecipeController.toggleLike(req, res);
            case "/me/bookmarked/toggle" -> RecipeController.toggleBookmark(req, res);
        }
    }
}
