package com.semantria.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    private Utils() {}

    public static String getHashCode(String input)
    {
        String hexStr = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();

            hexStr = "";
            for (int i = 0; i < digest.length; i++) {
                hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
            }
        } catch (NoSuchAlgorithmException e) {
            // let's don't throw it to the constructor
        } catch (UnsupportedEncodingException e) {
            // let's don't throw it to the constructor
        }

        return hexStr;
    }

    public static boolean isEmail(String value) {
        return value.matches("([\\w\\.]+)@([\\w\\.]+)\\.(\\w+)");
    }
}
