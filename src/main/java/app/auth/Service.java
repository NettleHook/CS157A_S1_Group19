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

    public static String login(String username, char[] password) throws SQLException {
        if (!validateUsername(username) || !validatePassword(password)) {
            return null;
        }

        String query = """
            SELECT password
            FROM users
            WHERE username = ?
        """;

        String hash;
        try (Connection con = Database.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet res = stmt.executeQuery();
                hash = res.getString("password");
            }
        }

        boolean isVerified = Auth.verify(hash, password);
        Arrays.fill(password, '0');
        if (hash == null || !isVerified) {
            return null;
        }
        
        String sessionId = generateSessionId();
        sessions.put(sessionId, username);
        return sessionId;
    }

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
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]$");
        return pattern.matcher(username).matches();
    }

    private static boolean validatePassword(char[] password) {
        Pattern pattern = Pattern.compile("^(?!.*[\\s'\\\"=]).{8,}$");
        CharBuffer buffer = CharBuffer.wrap(password);
        return pattern.matcher(buffer).matches();
    }
    
    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
