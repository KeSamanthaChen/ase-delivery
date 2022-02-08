package edu.tum.dse.deliveryservice.jwt;

import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class KeyManager {

    private PublicKey publicKey;

    public synchronized PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(byte[] publicKeyEncoded) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyEncoded);
                this.publicKey = keyFactory.generatePublic(publicKeySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
    }
}
