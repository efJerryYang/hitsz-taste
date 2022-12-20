package me.efjerryyang.webserver.util;

import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class CryptoUtilHash {
    public static Logger logger = LoggerFactory.getLogger(CryptoUtilHash.class);

    public static String generateSalt() {
        logger.info("Generating salt");
        // generate a random salt
        Random random = new Random();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt);
    }

    public static String hashWithSalt(String password, String salt) {
        logger.info("Hashing password with salt");
        // hash the text with salt
        // first examine the password is a valid sha256 hash or not
        String hashRegex = "[0-9a-fA-F]{64}";
        if (!password.matches(hashRegex)) {
            // if it is not a valid sha256 hash
            Exception e = new IllegalArgumentException();
            logger.error("Invalid password hash", e);
            throw new IllegalArgumentException("Invalid password hash", e);
        }
        logger.info("Password is a valid sha256 hash");
        // do xor with password and salt
        // then hash the result
        byte[] passwordBytes = password.getBytes();
        byte[] saltBytes = salt.getBytes();
        byte[] xorBytes = new byte[passwordBytes.length];
        for (int i = 0; i < passwordBytes.length; i++) {
            xorBytes[i] = (byte) (passwordBytes[i] ^ saltBytes[i % saltBytes.length]);
        }
        logger.info("hashing the result");
        return hash(xorBytes);
    }

    public static String hash(byte[] password) {
        logger.info("Hashing password bytes");
        // use sha256 to hash the password
        // it can be either a plain text or a sha256 hash
        // byte number of a sha256 hash is 32
        SHA256.Digest digest = new SHA256.Digest();
        return new String(digest.digest(password));
    }

    public static String hash(String password) {
        logger.info("Hashing password string");
        // use sha256 to hash the password
        // it can be either a plain text or a sha256 hash
        SHA256.Digest digest = new SHA256.Digest();
        byte[] hash = digest.digest(password.getBytes());
        return new String(hash);
    }
}
