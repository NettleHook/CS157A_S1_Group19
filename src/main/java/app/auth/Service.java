package app.auth;

import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Pattern;

import app.Database;

public class Service {
    public static boolean login(String username, char[] password) throws SQLException {
        if (!validateUsername(username) || !validatePassword(password)) {
            return false;
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

        if (hash == null) return false;
        
        boolean isVerified = Auth.verify(hash, password);
        Arrays.fill(password, '0');
        return isVerified;
    }

    public static boolean  signup(String username, char[] password) throws SQLException {
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

    private static boolean validateUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]$");
        return pattern.matcher(username).matches();
    }

    private static boolean validatePassword(char[] password) {
        Pattern pattern = Pattern.compile("^(?!.*[\\s'\\\"=]).{8,}$");
        CharBuffer buffer = CharBuffer.wrap(password);
        return pattern.matcher(buffer).matches();
    }
}
