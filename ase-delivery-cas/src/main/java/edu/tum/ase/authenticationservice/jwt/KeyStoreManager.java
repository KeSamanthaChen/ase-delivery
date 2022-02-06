package edu.tum.ase.authenticationservice.jwt;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;

@Component
public class KeyStoreManager {

    private KeyStore keyStore;

    private String keyAlias;

    private final char[] password = "ase_delivery".toCharArray();

    private static final String KEYSTORE_FILE = "ase_delivery.keystore";

    public KeyStoreManager() throws KeyStoreException, IOException {
        this.loadKeyStore();
    }

    public void loadKeyStore() throws KeyStoreException, IOException {
        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream is = null;

        try {
            // Get the path to the keystore file in the resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            is = classLoader.getResourceAsStream(KEYSTORE_FILE);
            keyStore.load(is, password);
            keyAlias = keyStore.aliases().nextElement();
        } catch (Exception e) {
            System.err.println("Error when loading KeyStore");
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    protected PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    protected Key getPrivateKey() {
        try {
            return keyStore.getKey(keyAlias, password);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
