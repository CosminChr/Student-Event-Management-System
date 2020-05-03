package student.event.management.application.service.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncodingService {


    /**
     * encode a string using the SHA-256
     *
     * @param password
     * @return
     */
    public static String encode(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedPasswordhash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        String encodedPassword = convertBytesToHex(encodedPasswordhash);
        return encodedPassword;
    }

    /**
     * convert an array of bytes to a string containing hexadecimal numbers
     *
     * @param hash
     * @return
     */
    private static String convertBytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
