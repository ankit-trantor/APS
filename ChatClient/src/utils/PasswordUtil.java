package utils;

import java.math.BigInteger;
import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtil {
    public static String hashPassword(String plainText) {
        byte[] utf8;
        byte[] encoded;
        String password = null;
        try {
            utf8 = plainText.getBytes("UTF-8");
            encoded = DigestUtils.sha1(DigestUtils.sha1(utf8));
            password = "*" + toHex(encoded).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    private static String toHex(byte[] bytes) {
        return String.format("%040x", new BigInteger(1, bytes));
    }
}