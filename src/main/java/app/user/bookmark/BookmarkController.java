package app.user.bookmark;

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

public class BookmarkController {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void getBookmarks(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String sessionId = AuthMiddleware.getSessionId(req, res);
            UserSession userSession = AuthService.getUserSession(sessionId);
            if (userSession!= null) {
                int userId = userSession.getUserId();
                List<Map<String, Object>> bookmarks = BookmarkService.getBookmarks(userId);

                res.setStatus(200);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                mapper.writeValue(res.getWriter(), bookmarks);
            } else {
                res.setStatus(401);
            }
        } catch (SQLException e) {
            res.setStatus(500);
        }
    }

    public static void addBookmark(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String sessionId = AuthMiddleware.getSessionId(req, res);
            UserSession userSession = AuthService.getUserSession(sessionId);

            if (userSession != null) {
                int userId = userSession.getUserId();
                int recipeId = mapper.readTree(req.getReader()).get("recipeId").asInt();

                BookmarkService.addBookmark(userId, recipeId);
                res.setStatus(201);
            } else {
                res.setStatus(401);
            }
        } catch (SQLException e) {
            res.setStatus(500);
        }
    }

    public static void removeBookmark(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String sessionId = AuthMiddleware.getSessionId(req, res);
            UserSession userSession = AuthService.getUserSession(sessionId);

            if (userSession != null) {
                int userId = userSession.getUserId();
                int recipeId = mapper.readTree(req.getReader()).get("recipeId").asInt();

                BookmarkService.removeBookmark(userId, recipeId);
                res.setStatus(200);
            } else {
                res.setStatus(401);
            }
        } catch (SQLException e) {
            res.setStatus(500);
        }
    }
}