package app.auth;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Controller {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void login(HttpServletRequest req, HttpServletResponse res) throws StreamWriteException, DatabindException, IOException {
        try {
            UserCredentials cred = getUserCredentials(req);
            String sessionId = Service.login(cred.username(), cred.password());
            
            if (sessionId != null) {
                Cookie cookie = new Cookie("SESSION_ID", sessionId);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge(86400);
                res.addCookie(cookie);

                res.setStatus(200);

                writeUserData(cred.username(), res);
            } else {
                res.setStatus(401);
            }
            
        } catch (SQLException e) {
            res.setStatus(500);
        }
    }

    public static void signup(HttpServletRequest req, HttpServletResponse res) {
        try {
            UserCredentials cred = getUserCredentials(req);
            if (Service.signup(cred.username(), cred.password())) {
                res.setStatus(201);
            } else {
                res.setStatus(400);
            }
        } catch (SQLException e) {
            res.setStatus(409);
            
        } catch (IOException e) {
            res.setStatus(400);
        }
    }

    public static void validate(HttpServletRequest req, HttpServletResponse res) {
        String sessionId = req.getParameter("sessionId");

        try {
            if (Service.validate(sessionId)) {
                res.setStatus(201);
                writeUserData(sessionId, res);
            } else {
                res.setStatus(401);
            }
        } catch (IOException e) {
            res.setStatus(500);
        }
    }

    private static void writeUserData(String username, HttpServletResponse res) throws StreamWriteException, DatabindException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("username", username);
        mapper.writeValue(res.getWriter(), responseData);
    }

    private static record UserCredentials(String username, char[] password) {};

    private static UserCredentials getUserCredentials(HttpServletRequest req) throws IOException {
        JsonNode node = mapper.readTree(req.getReader());
        String username = node.get("username").asText();
        char[] password = node.get("password").asText().toCharArray();
        return new UserCredentials(username, password);
    }
}
