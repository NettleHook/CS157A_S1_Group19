package app.feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Database;
import app.auth.AuthMiddleware;
import app.auth.AuthService;
import app.auth.UserSession;
import app.ingredient.IngredientService.Unit;

public class FeedbackService {

    public static void addMessage(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String sessionId = AuthMiddleware.getSessionId(request, response);
        UserSession userSession = AuthService.getUserSession(sessionId);
        Integer userId = null;
        if (userSession != null) {
            userId = userSession.getUserId();
        }
        String contactInfo = request.getParameter("contact_info");
        String categoryId = request.getParameter("feedback_category");
        String message = request.getParameter("message");
        String query = "INSERT INTO feedback_messages(user_id, contact_info, topic_id, message, status) VALUES (?, ?, ?, ?, 'unresolved')";
        try (Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
            if(userId != null){
                stmt.setInt(1, userId);
            }else{
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, contactInfo);
            stmt.setString(3, categoryId);
            stmt.setString(4, message);
            stmt.executeUpdate();
        }
    }

    public static void updateMessageStatus(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String query = "UPDATE feedback_messages SET status = ?, WHERE id = ?";
        try (Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, "resolved");//FIXME: THIS WILL NEED TO BE RETRIEVED FROM THE REQUEST PARAMETER
            stmt.setInt(2, 1);
            stmt.executeUpdate();
        }
    }

    public static List<Unit> getCategories() throws SQLException {
        String query = """
            SELECT id, category
            FROM feedback_categories
        """;

        List<Unit> units = new ArrayList<>();
        try (Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(query); ResultSet res = stmt.executeQuery()) {

            while (res.next()) {
                units.add(new Unit(res.getInt("id"), res.getString("category")));
            }
        }
        return units;
    }
}
