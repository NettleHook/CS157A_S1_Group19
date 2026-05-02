package app.auth;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.api.ApiMessage;
import app.api.ApiResponse;
import app.utils.JsonUtils;

public class AuthController {
    private static final String SESSION_ID = "SESSION_ID"; 

    public static void login(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            UserCredentials cred = getUserCredentials(req);
            String sessionId = AuthService.login(cred.username(), cred.password());
            UserSession userSession = AuthService.getUserSession(sessionId);

            if (userSession == null) {
                ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
                return;
            }
            
            Cookie cookie = new Cookie(SESSION_ID, sessionId);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            res.addCookie(cookie);

            writeUserData(userSession, res);
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        } catch (IOException e) {
            ApiResponse.error(res, 400, ApiMessage.INVALID_JSON);
        }
    }

    public static void signup(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            UserCredentials cred = getUserCredentials(req);
            if (AuthService.signup(cred.username(), cred.password())) {
                ApiResponse.write(res, 201, "Signup successful.");
            } else {
                ApiResponse.error(res, 400, "Invalid username or password.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            ApiResponse.error(res, 409, "Username already exists.");
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        } catch (IOException e) {
            ApiResponse.error(res, 400, ApiMessage.INVALID_JSON);
        }
    }

    public static void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
        invalidateUserCookie(req, res);
        ApiResponse.write(res, 200, "Logged out.");
    }   

    public static void validate(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sessionId = AuthMiddleware.getSessionId(req, res);
        UserSession userSession = AuthService.getUserSession(sessionId);

        if (userSession == null) {
            invalidateUserCookie(req, res);
            ApiResponse.error(res, 401, ApiMessage.UNAUTHORIZED);
            return;
        }

        writeUserData(userSession, res);
    }
    
    private static void writeUserData(UserSession userSession, HttpServletResponse res) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userSession.getUserId());
        data.put("username", userSession.getUsername());
        ApiResponse.write(res, 200, data);
    }

    private static record UserCredentials(String username, char[] password) {};

    private static UserCredentials getUserCredentials(HttpServletRequest req) throws IOException {
        return JsonUtils.MAPPER.readValue(req.getReader(), UserCredentials.class);
    }

    private static void invalidateUserCookie(HttpServletRequest req, HttpServletResponse res) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (SESSION_ID.equals(c.getName())) {
                    AuthService.logout(c.getValue());
                    
                    c.setValue("");
                    c.setPath("/");
                    c.setHttpOnly(true);
                    c.setSecure(true);
                    c.setMaxAge(0); 
                    res.addCookie(c);
                }
            }
        }
    }
}
