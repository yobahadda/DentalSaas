package com.example.dentalsaas.testings;

import com.example.dentalsaas.util.JwtUtil;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class JwtTest {
    public static void main(String[] args) {
        // Mock UserDetails object
        UserDetails userDetails = new User(
                "john_doe", // username
                "password", // password (won't be used in token)
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Example: create and validate token
        JwtUtil jwtUtil = new JwtUtil();
        jwtUtil.secret = "mySuperSecretKeyForJWTGeneration123456"; // normally injected
        jwtUtil.expiration = 86400000L; // 1 day

        String token = jwtUtil.generateToken(userDetails);
        System.out.println("Generated token: " + token);

        boolean valid = jwtUtil.validateToken(token, userDetails);
        System.out.println("Is valid? " + valid);
    }
}
