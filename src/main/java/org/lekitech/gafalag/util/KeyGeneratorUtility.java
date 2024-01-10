package org.lekitech.gafalag.util;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

@Slf4j
public class KeyGeneratorUtility {

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;

        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException();
        }

        return keyPair;
    }
}
