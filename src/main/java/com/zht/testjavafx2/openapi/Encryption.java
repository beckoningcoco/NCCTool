package com.zht.testjavafx2.openapi;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.MGF1ParameterSpec;

/**
 * 加密类
 *
 * @author liyang
 */
public class Encryption {

    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * symEncrypt 对称加密
     *
     * @param strkey 对称密钥
     * @param src    原文
     * @return 密文
     */
    public static String symEncrypt(String strkey, String src) throws Exception {
        String target = null;
        try {
            Key key = KeysFactory.getSymKey(strkey);
            //加密
            Cipher cipher = Cipher.getInstance(CipherConstant.AES_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(strkey.substring(0, 16).getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encodeResult = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            target = Base64Util.encryptBASE64(encodeResult);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("加密失败" + e.getMessage());
        }
        return target;
    }

    /**
     * pubEncrypt 公钥加密
     *
     * @param pubKey 公钥
     * @param src    原文
     * @return 密文
     * @throws IOException
     * @throws Exception
     */
    public static String pubEncrypt(String pubKey, String src) throws Exception {
        String target = null;
        ByteArrayOutputStream out = null;
        try {
            Key key = KeysFactory.getPublicKey(pubKey);

            Cipher cipher = Cipher.getInstance(CipherConstant.RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key,new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT));
            byte[] data = src.getBytes();
            int inputLen = data.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }

            target = Base64Util.encryptBASE64(out.toByteArray());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("加密失败" + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return target;
    }

}