package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.auth.AuthRoutes;
import app.ingredient.IngredientRoutes;
import app.user.bookmark.BookmarkRoutes;

@WebServlet("/api/*")
public class Index extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthRoutes.handlePostRoutes(req, resp);
        BookmarkRoutes.handleGetRoutes(req, resp);
        IngredientRoutes.handlePostRoutes(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthRoutes.handleGetRoutes(req, resp);
        BookmarkRoutes.handleGetRoutes(req, resp);
        IngredientRoutes.handleGetRoutes(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookmarkRoutes.handleDeleteRoutes(req, resp);
    }
}
