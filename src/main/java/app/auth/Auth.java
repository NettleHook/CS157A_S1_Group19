package app.auth;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Auth {
    private static final Argon2 argon2 = Argon2Factory.create();

    public static String hash(char[] password) {
        return argon2.hash(10, 65536, 1, password);
    }

    public static boolean verify(String hash, char[] password) {
        return argon2.verify(hash, password);
    }
}
