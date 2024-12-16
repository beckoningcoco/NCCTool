package com.zht.testjavafx2.openapi;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
public class KeysFactory {
    /**
     * buildAsymKey 生成一组非对称密钥
     *
     * @return KeyPair key是PublicKey和PrivateKey
     * @throws NoSuchAlgorithmException
     */
    public static KeyPairs buildAsymKey() throws Exception {

        /* 初始化密钥生成器 */
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CipherConstant.RSA);
        keyPairGenerator.initialize(2048, SecureRandomProxy.getRandomInstance());

        /* 生成密钥 */
        return new KeyPairs(keyPairGenerator.generateKeyPair());
    }

    /**
     * buildAsymKey 生成一个对称密钥
     *
     * @return 对称密钥
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    public static String buildSymKey() throws Exception {
        // 生成Key
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CipherConstant.AES);

        keyGenerator.init(256, SecureRandomProxy.getRandomInstance());
        // 使用上面这种初始化方法可以特定种子来生成密钥，这样加密后的密文是唯一固定的。
        SecretKey secretKey = keyGenerator.generateKey();

        return Base64Util.encryptBASE64(secretKey.getEncoded());

    }

    public static Key getPublicKey(String pubKey) throws Exception {
        Key key = null;

        try {
            byte[] keyBytes = Base64Util.decryptBASE64(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance(CipherConstant.RSA);

            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            key = keyFactory.generatePublic(x509KeySpec);

        } catch (Exception e) {
            throw new Exception("无效的密钥  " + e.getMessage());
        }

        return key;
    }

    public static Key getPrivateKey(String priKey) throws Exception {
        Key key = null;

        try {
            byte[] keyBytes = Base64Util.decryptBASE64(priKey);

            KeyFactory keyFactory = KeyFactory.getInstance(CipherConstant.RSA);

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            key = keyFactory.generatePrivate(pkcs8KeySpec);

        } catch (Exception e) {
            throw new Exception("无效密钥 " + e.getMessage());
        }

        return key;
    }

    public static Key getSymKey(String symKey) throws Exception {
        Key key = null;

        try {
            byte[] keyBytes = Base64Util.decryptBASE64(symKey);
            // Key转换
            key = new SecretKeySpec(keyBytes, CipherConstant.AES);
        } catch (Exception e) {
            throw new Exception("无效密钥 " + e.getMessage());
        }

        return key;
    }
}
