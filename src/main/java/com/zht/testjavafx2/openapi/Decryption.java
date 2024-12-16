package com.zht.testjavafx2.openapi;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.MGF1ParameterSpec;

public class Decryption {

    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 256;

    /**
     * symDecrypt 对称解密
     *
     * @param strkey 对称密钥
     * @param src    密文
     * @return 原文
     * @throws Exception
     */
    public static String symDecrypt(String strkey, String src) throws Exception {

        String target = null;
        try {
            Key key = KeysFactory.getSymKey(strkey);
            // 解密
            Cipher cipher = Cipher.getInstance(CipherConstant.AES_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(strkey.substring(0, 16).getBytes());
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decodeResult = cipher.doFinal(Base64Util.decryptBASE64(src));
            target = new String(decodeResult, StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                 InvalidKeyException e) {
            throw new Exception("解密失败" + e.getMessage());
        }

        return target;
    }

    /**
     * priDecrypt 私钥解密
     *
     * @param priKey 私钥
     * @param src    密文
     * @return 原文
     * @throws Exception
     */
    public static String priDecrypt(String priKey, String src) throws Exception {
        String target = null;
        ByteArrayOutputStream out = null;
        try {
            Key key = KeysFactory.getPrivateKey(priKey);
            // 解密
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT));
            byte[] data = Base64Util.decryptBASE64(src);
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            target = new String(out.toByteArray());

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("解密失败" + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return target;
    }
}
