package at.geyser.cashier;

import java.security.MessageDigest;

public class Hasher {

    private static final String SALT = "at.geyser";

    private static final String HASH_ALGORITHM = "SHA-256";

    private static MessageDigest messageDigest;

    public static String hash(Object object) {
        if (messageDigest == null) {
            init();
        }

        byte[] hash = messageDigest.digest((object.toString() + SALT).getBytes());

        StringBuilder stringBuilder = new StringBuilder();

        for (byte b : hash) {
            stringBuilder.append(String.format("%02x", b));
        }

        return stringBuilder.toString();
    }

    private static void init() {
        try {
            messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}