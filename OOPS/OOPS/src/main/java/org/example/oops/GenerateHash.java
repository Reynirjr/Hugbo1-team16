package org.example.oops;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("admin123");
        String hash2 = encoder.encode("staff123");
        System.out.println("Hash for admin123:");
        System.out.println(hash);
        System.out.println("Hash for staff123:");
        System.out.println(hash2);
    }
}
