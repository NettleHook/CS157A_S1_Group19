package app.user.stats;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.api.ApiMessage;
import app.api.ApiResponse;
import app.auth.AuthMiddleware;
import app.user.bookmark.BookmarkController;

public class StatsController {

    public static void addLike(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        if (sessionId == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }
        // likes
        ApiResponse.write(res, 200, "ok");
    }

    public static void removeLike(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        if (sessionId == null) {
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }
        // unlike
         ApiResponse.write(res, 200, "ok");
    }

    public static void addBookmark(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BookmarkController.addBookmark(req, res);
    }

    public static void removeBookmark(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BookmarkController.removeBookmark(req, res);
    }
}