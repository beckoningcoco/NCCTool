package com.zht.testjavafx2.openapi;

import java.security.KeyPair;

public class KeyPairs {
    private KeyPair keyPair;

    public KeyPairs(KeyPair keyPair){
        this.keyPair = keyPair;
    }

    public String getPublicKey(){
        return Base64Util.encryptBASE64(keyPair.getPublic().getEncoded());
    }

    public String getPrivateKey(){
        return Base64Util.encryptBASE64(keyPair.getPrivate().getEncoded());
    }
}
