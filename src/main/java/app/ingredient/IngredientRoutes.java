package app.ingredient;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.exc.StreamWriteException;

public class IngredientRoutes {
    public static void handlePostRoutes(HttpServletRequest req, HttpServletResponse res) throws StreamWriteException, IOException {
        String path = req.getPathInfo();

        if (path == null) return;

        switch (path) {
            case "/me/ingredients" -> IngredientController.postUserIngredient(req, res);
        }
    }

    public static void handleGetRoutes(HttpServletRequest req, HttpServletResponse res) throws StreamWriteException, IOException {
        String path = req.getPathInfo();

        if (path == null) return;

        switch (path) {
            case "/ingredients" -> IngredientController.getIngredients(req, res);
            case "/me/ingredients" -> IngredientController.getUserIngredients(req, res);
        }
    }
}
