package app.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthRoutes {
    public static void handlePostRoutes(HttpServletRequest req, HttpServletResponse res) {
        String path = req.getPathInfo();

        if (path == null) return;

        switch (path) {
            case "/login" -> Controller.login(req, res);
            case "/signup" -> Controller.signup(req, res);
        }
    }
}
