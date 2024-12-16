package com.zht.testjavafx2.openapi;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureRandomProxy {
    public static SecureRandom getRandomInstance() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return random;
    }
}
