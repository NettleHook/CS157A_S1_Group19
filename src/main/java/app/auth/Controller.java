package app.auth;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller {
    public static void login(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        char[] password = req.getParameter("password").toCharArray();

        try {
            boolean isSuccess = Service.login(username, password);

            if (isSuccess) {
                res.setStatus(200);
            } else {
                res.setStatus(401);
            }
            
        } catch (SQLException e) {
            res.setStatus(500);
        }
    }

    public static void signup(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        char[] password = req.getParameter("password").toCharArray();

        try {
            Service.signup(username, password);

            res.setStatus(201);
        } catch (SQLException e) {
            res.setStatus(409);
            
        } catch (Exception e) {
            res.setStatus(500);
        }
    }
}
