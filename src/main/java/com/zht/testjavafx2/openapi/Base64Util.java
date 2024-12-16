package com.zht.testjavafx2.openapi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util
{

    /**
     * Base64编码
     */
    public static String encryptBASE64(byte[] key) {
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * Base64解码
     * @throws IOException
     */
    public static byte[] decryptBASE64(String key) throws IOException {
        return Base64.getDecoder().decode(key);
    }
}
