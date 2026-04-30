package app.user.stats;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/stats/*")
public class StatsRouter extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";
        switch (path) {
            case "/like" -> StatsController.addLike(req, res);
            case "/bookmark" -> StatsController.addBookmark(req, res);
            default -> res.sendError(404);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String path = req.getPathInfo();
        if (path == null) path = "";
        switch (path) {
            case "/like" -> StatsController.removeLike(req, res);
            case "/bookmark" -> StatsController.removeBookmark(req, res);
            default -> res.sendError(404);
        }
    }
}