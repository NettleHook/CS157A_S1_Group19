package app.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthMiddleware {
    private static final String SESSION_ID = "SESSION_ID"; 

    public static String getSessionId(HttpServletRequest req, HttpServletResponse res) {
        String authHeader = req.getHeader(SESSION_ID);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7);
    }
}
