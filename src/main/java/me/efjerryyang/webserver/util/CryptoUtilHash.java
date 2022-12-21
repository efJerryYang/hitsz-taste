package me.efjerryyang.webserver.util;

import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Random;

public class CryptoUtilHash {
    public static final int SALT_LENGTH = 16;
    public static Logger logger = LoggerFactory.getLogger(CryptoUtilHash.class);

    public static String getSalt() {
        logger.info("Generating salt");
        // generate a random salt
        Random random = new Random();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Hex.toHexString(salt);
    }

    public static String hash(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        logger.info("Hashing password string");
        // use sha256 to hash the password
        // it can be either a plain text or a sha256 hash
        SHA256.Digest digest = new SHA256.Digest();
        // use hex string to represent the hash
        return Hex.toHexString(digest.digest(password.getBytes()));
    }

    public static String hashWithSalt(String password, String salt) {
        if (password == null || salt == null) {
            throw new IllegalArgumentException("Password or salt cannot be null");
        }
        logger.info("Hashing password with salt");
        // hash the text with salt
        // first examine the password is a valid sha256 hash or not
        String hashRegex = "[0-9a-fA-F]{64}";
        if (!password.matches(hashRegex)) {
            // if it is not a valid sha256 hash
            Exception e = new IllegalArgumentException();
            logger.error("Invalid password hash: " + password, e);
            throw new IllegalArgumentException("Invalid password hash", e);
        }
        logger.info("Password is a valid sha256 hash");
        // do xor with password and salt
        // then hash the result
        byte[] passwordBytes = Hex.decode(password);
        byte[] saltBytes = Hex.decode(salt);
        byte[] xorBytes = new byte[passwordBytes.length];
        for (int i = 0; i < passwordBytes.length; i++) {
            xorBytes[i] = (byte) (passwordBytes[i] ^ saltBytes[i % saltBytes.length]);
        }
        logger.info("hash with salt, producing the result...");
        // use base64 to encode the string, so that get the conventional format
        SHA256.Digest digest = new SHA256.Digest();
        return Base64.getEncoder().encodeToString(digest.digest(xorBytes));
    }

}
