package app.user.diets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.Constants;
import app.Database;
import app.auth.UserSession;

public class DietsService {

    public static List<String> getDiets(UserSession usersession) throws SQLException {
        int userId = usersession.getUserId();
        String query = "SELECT name FROM diets WHERE id in (SELECT diet_id FROM user_diets WHERE user_id = ?)";
        try (java.sql.Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
            List<String> userDiets = new ArrayList<>();
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString(1);
                    for (Constants.Option option : Constants.DIETS) {
                        if (option.text().equals(name)) {
                            userDiets.add(option.id());
                            break;
                        }
                    }
                }
            }
            return userDiets;
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void registerDiets(UserSession session, List<String> diets) throws SQLException {
        if (diets == null) {
            diets = new ArrayList<>();
        }
        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);
            try {
                // Delete all existing diets for this user
                String delete = "DELETE FROM user_diets WHERE user_id = ?";
                try (PreparedStatement stmt = con.prepareStatement(delete)) {
                    stmt.setInt(1, session.getUserId());
                    stmt.executeUpdate();
                }
                // Insert the new set
                String insert = "INSERT INTO user_diets (user_id, diet_id) "
                        + "SELECT ?, id FROM diets WHERE name = ?";
                try (PreparedStatement stmt = con.prepareStatement(insert)) {
                    for (String diet : diets) {
                        stmt.setInt(1, session.getUserId());
                        stmt.setString(2, diet);
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }
}
