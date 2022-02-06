package edu.tum.dse.deliveryservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TokenGenerator {

    @Autowired
    private SecureRandom secureRandom;

    private static final int TOKEN_LENGTH = 16;

    private final static String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateToken() {
        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        for(int i = 0; i < TOKEN_LENGTH; i++) {
            int char_i = secureRandom.nextInt(ALLOWED_CHARS.length());
            char c = ALLOWED_CHARS.charAt(char_i);
            sb.append(c);
        }
        return sb.toString();
    }

}
