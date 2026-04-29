package app.user.bookmark;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookmarkRoutes {

    public static void handleGetRoutes(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();
        if (path == null) return;

        switch (path) {
            case "/bookmarks" -> BookmarkController.getBookmarks(req, res);
        }
    }

    public static void handlePostRoutes(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();
        if (path == null) return;

        switch (path) {
            case "/bookmarks" -> BookmarkController.addBookmark(req, res);
        }
    }

    public static void handleDeleteRoutes(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();
        if (path == null) return;

        switch (path) {
            case "/bookmarks" -> BookmarkController.removeBookmark(req, res);
        }
    }
}