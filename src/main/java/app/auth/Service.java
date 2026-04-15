package app.auth;

import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import app.Database;

public class Service {
    private static final Map<String, String> sessions = new HashMap<>();

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?!.*[\\s'\\\"=]).{8,}$");

    public static String login(String username, char[] password) throws SQLException {
        if (!validateUsername(username) || !validatePassword(password)) {
            return null;
        }

        String query = """
            SELECT password
            FROM users
            WHERE username = ?
        """;

        String hash = null;
        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet res = stmt.executeQuery()) {
                    if (res.next()) {
                        hash = res.getString("password");
                    }
                }
            }
        }

        boolean isValid = (hash != null && Auth.verify(hash, password));
        Arrays.fill(password, '0');

        if (!isValid) return null;

        String sessionId = generateSessionId();
        sessions.put(sessionId, username);
        return sessionId;
    }

    // Do not expose success state to client
    public static boolean logout(String sessionId) {
        if (sessions.containsKey(sessionId)) {
            sessions.remove(sessionId);
            return true;
        }
        return false;
    }

    public static boolean signup(String username, char[] password) throws SQLException {
        if (!validateUsername(username) || !validatePassword(password)) {
            return false;
        }

        String query = """
            INSERT INTO users (username, password)
            VALUES (?, ?)
        """;

        String hash = Auth.hash(password);
        Arrays.fill(password, '0');

        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, hash);
                stmt.executeUpdate();
            }
        }
        return true;
    }

    public static boolean validate(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    private static boolean validateUsername(String username) {
        boolean isMatch = USERNAME_PATTERN.matcher(username).matches();
        return isMatch;
    }

    private static boolean validatePassword(char[] password) {
        CharBuffer buffer = CharBuffer.wrap(password);
        boolean isMatch = PASSWORD_PATTERN.matcher(buffer).matches();
        return isMatch;
    }
    
    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
