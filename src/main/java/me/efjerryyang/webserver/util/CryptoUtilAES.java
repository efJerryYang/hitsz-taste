package me.efjerryyang.webserver.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class CryptoUtilAES {
    public static final Logger logger = LoggerFactory.getLogger(CryptoUtilAES.class);
    /**
     * The secret key used for encryption and decryption
     */
    private final Key key;

    /**
     * The initialization vector used for encryption and decryption
     */
    private final IvParameterSpec iv;

    /**
     * Constructs a new CryptoUtilAES instance with the given key and initialization vector (IV) strings.
     *
     * @param keyString The key string to use for encryption/decryption operations.
     * @param ivString  The initialization vector (IV) string to use for encryption/decryption operations.
     */
    public CryptoUtilAES(String keyString, String ivString) {
        Security.addProvider(new BouncyCastleProvider());
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyString);
            key = new SecretKeySpec(keyBytes, "AES");
            iv = new IvParameterSpec(Base64.getDecoder().decode(ivString));
        } catch (IllegalArgumentException e) {
            // Handle invalid Base64 input
            logger.error("Error creating instance of CryptoUtilAES", e);
            throw new IllegalArgumentException("Invalid key or IV string: " + e.getMessage(), e);
        }
    }

    /**
     * Decrypts the given ciphertext using the key and initialization vector (IV) provided in the constructor.
     *
     * @param ciphertext The ciphertext to decrypt.
     * @return The decrypted plaintext.
     */
    public String decrypter(String ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decryptedBytes);
        } catch (IllegalBlockSizeException e) {
            logger.error("Error while decrypting: illegal block size", e);
            return null;
        } catch (BadPaddingException e) {
            logger.error("Error while decrypting: bad padding", e);
            return null;
        } catch (NoSuchPaddingException e) {
            logger.error("Error while decrypting: no such padding", e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error while decrypting: no such algorithm", e);
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("Error while decrypting: invalid algorithm parameter", e);
            return null;
        } catch (InvalidKeyException e) {
            logger.error("Error while decrypting: invalid key", e);
            return null;
        }
    }

}