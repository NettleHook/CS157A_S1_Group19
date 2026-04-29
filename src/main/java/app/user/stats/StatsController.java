package app.user.stats;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.api.ApiResponse;
import app.user.bookmark.BookmarkController;
import app.user.likes.LikesController;

public class StatsController {
    public static void getLikes(HttpServletRequest req, HttpServletResponse res) throws IOException{
        //Return search result likes through search backend instead for search?
        //Still worth returning results for one specific recipe on the recipe page
        ApiResponse.write(res, 200, "ok");
    }
    public static void addLike(HttpServletRequest req, HttpServletResponse res) throws IOException {
       LikesController.addLike(req, res);
    }

    public static void removeLike(HttpServletRequest req, HttpServletResponse res) throws IOException {
        LikesController.removeLike(req, res);
    }

    public static void addBookmark(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BookmarkController.addBookmark(req, res);
    }

    public static void removeBookmark(HttpServletRequest req, HttpServletResponse res) throws IOException {
        BookmarkController.removeBookmark(req, res);
    }
}