package org.tem.calendar.util;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class CipherUtils {


    @NonNull
    public static String getHash(InputStream inputStream){
        return getHash(inputStream, "SHA-256");
    }

    @NonNull
    public static String getHash(InputStream inputStream, String algorithm){
        try {
            byte[] buffer = new byte[8192];
            int count;
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            while ((count = bis.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }
            bis.close();

            byte[] hash = digest.digest();
            return new BigInteger(1, hash).toString(16);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
