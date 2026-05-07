package app.feedback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.api.ApiMessage;
import app.api.ApiResponse;
import app.auth.AuthMiddleware;
import app.auth.AuthService;
import app.auth.UserSession;
import app.ingredient.IngredientService.Unit;

public class FeedbackController {

    public static void addMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            FeedbackService.addMessage(request, response);
            ApiResponse.write(response, 200, "Success");
        } catch (SQLException e) {
            ApiResponse.error(response, 500, e.getMessage());
        }
    }

    public static void updateMessageStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!validate(request, response)) {
            response.setStatus(401);
        }
        try {
            FeedbackService.updateMessageStatus(request, response);
            ApiResponse.write(response, 200, "Success");
        } catch (SQLException e) {
            ApiResponse.error(response, 500, e.getMessage());
        }
    }

    public static void viewAllMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!validate(request, response)) {
            response.setStatus(401);
        }
    }

    public static void viewUnresolvedMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!validate(request, response)) {
            response.setStatus(401);
        }
    }

    private static boolean validate(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = AuthMiddleware.getSessionId(request, response);
        UserSession userSession = AuthService.getUserSession(sessionId);
        if (userSession == null) {
            return false;
        }
        String userName = userSession.getUsername();
        if (userName.equals("dev_cad")) {
            return true;
        }
        return false;
    }

    public static void getCategories(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            List<Unit> units = FeedbackService.getCategories();
            ApiResponse.write(res, 200, units);
        } catch (SQLException e) {
            ApiResponse.error(res, 500, ApiMessage.DB_ERROR);
        } catch (IOException e) {
            ApiResponse.error(res, 400, ApiMessage.INVALID_JSON);
        }
    }
}
