package com.outils;

import android.util.Log;

import org.bouncycastle.crypto.generators.SCrypt;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordService {
    private static final int DEFAULT_COST = 16384; // N parameter, the CPU/memory cost parameter
    private static final int DEFAULT_BLOCK_SIZE = 8; // r parameter, block size
    private static final int DEFAULT_PARALLELISM = 1; // p parameter, parallelization parameter
    private static final int DEFAULT_KEY_LENGTH = 32; // length of derived key in bytes
    private static final String DEFAULT_SALT= "[B@7bb3c50";//A nous de choisir

    // Method to securely hash a password
    public static String hashPassword(String password) {
        byte[] salt = DEFAULT_SALT.getBytes();
        byte[] hashedPassword = SCrypt.generate(
                password.getBytes(), salt,
                DEFAULT_COST, DEFAULT_BLOCK_SIZE, DEFAULT_PARALLELISM,
                DEFAULT_KEY_LENGTH);
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}