package app.user.likes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.auth.AuthMiddleware;
import app.auth.AuthService;
import app.auth.UserSession;

public class LikesController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getLikedRecipes(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String sessionId = AuthMiddleware.getSessionId(req, res);
            UserSession userSession = AuthService.getUserSession(sessionId);
            if (userSession != null) {
                int userId = userSession.getUserId();
                List<Map<String, Object>> liked_recipes = LikesService.getLikedRecipes(userId);

                res.setStatus(200);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                mapper.writeValue(res.getWriter(), liked_recipes);
            } else {
                res.setStatus(401);
            }
        } catch (SQLException e) {
            res.setStatus(500);
        }
    }

    public static void getLikes(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Integer recipeId = Integer.valueOf(req.getParameter("recipeId"));
        mapper.writeValue(res.getWriter(), LikesService.getLikes(recipeId));
        res.setStatus(200);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
    }
    public static void addLike(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String sessionId = AuthMiddleware.getSessionId(req, res);
            UserSession userSession = AuthService.getUserSession(sessionId);

            if (userSession != null) {
                int userId = userSession.getUserId();
                int recipeId = mapper.readTree(req.getReader()).get("recipeId").asInt();

                LikesService.addLike(userId, recipeId);
                res.setStatus(201);
            } else {
                res.setStatus(401);
            }
        } catch (SQLException e) {
            res.setStatus(500);
        }
    }

    public static void removeLike(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String sessionId = AuthMiddleware.getSessionId(req, res);
            UserSession userSession = AuthService.getUserSession(sessionId);

            if (userSession != null) {
                int userId = userSession.getUserId();
                int recipeId = mapper.readTree(req.getReader()).get("recipeId").asInt();

                LikesService.removeLike(userId, recipeId);
                res.setStatus(200);
            } else {
                res.setStatus(401);
            }
        } catch (SQLException e) {
            res.setStatus(500);
        }
    }
}