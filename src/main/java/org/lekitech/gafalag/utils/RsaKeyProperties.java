package org.lekitech.gafalag.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Component to hold RSA key properties.
 * This class is responsible for generating and storing RSA public and private keys
 * used for cryptographic operations such as JWT signing and verification.
 */
@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class RsaKeyProperties {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    /**
     * Default constructor that initializes the RSA key properties.
     * It generates a new RSA key pair using the {@link KeyGeneratorUtility} and assigns
     * the public and private keys to the respective fields.
     */
    public RsaKeyProperties() {
        final KeyPair keyPair = KeyGeneratorUtility.generateRsaKey();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }
}
