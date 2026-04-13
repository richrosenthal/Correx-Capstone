package com.rrosenthal.corrix.controller;

import com.rrosenthal.corrix.dto.LoginRequest;
import com.rrosenthal.corrix.entity.User;
import com.rrosenthal.corrix.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        var userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if(userOpt.isEmpty()){
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        User user = userOpt.get();

        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPasswordHash()
        );

        if(!passwordMatches){
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        return ResponseEntity.ok(user);
    }
}
