package app.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.exc.StreamWriteException;

public class AuthRoutes {
    public static void handlePostRoutes(HttpServletRequest req, HttpServletResponse res) throws StreamWriteException, IOException {
        String path = req.getPathInfo();

        if (path == null) return;

        switch (path) {
            case "/login" -> AuthController.login(req, res);
            case "/signup" -> AuthController.signup(req, res);
            case "/logout" -> AuthController.logout(req, res);
        }
    }

    public static void handleGetRoutes(HttpServletRequest req, HttpServletResponse res) throws StreamWriteException, IOException {
        String path = req.getPathInfo();

        if (path == null) return;

        switch (path) {
            case "/validate" -> AuthController.validate(req, res);
        }
    }
}
