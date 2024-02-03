package org.lekitech.gafalag.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * Utility class for generating RSA key pairs.
 * This class provides a method to generate a RSA key pair, which can be used for JWT signing and verification.
 */
@Slf4j
@Deprecated
public class KeyGeneratorUtility {

    /**
     * Generates an RSA key pair using the RSA algorithm.
     * The method initializes a KeyPairGenerator for the RSA algorithm and sets the key size to 2048 bits.
     *
     * @return A KeyPair object containing RSA public and private keys.
     * @throws IllegalStateException if the RSA algorithm is not supported or if the key generation fails.
     */
    public static KeyPair generateRsaKey() {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            log.error("Error generating RSA KeyPair: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to generate RSA KeyPair", e);
        }
    }
}
