package com.example.controller;

import com.example.dto.AuthResponse;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserAuthService;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authMgr;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Cek apakah ini user pertama, jika ya jadikan admin
            boolean isFirstUser = userRepository.count() == 0;
            
            Map<String, Object> result;
            if (isFirstUser) {
                result = userService.registerAdmin(request);
                response.put("message", "First user registered as admin successfully");
            } else {
                result = userService.register(request);
                response.put("message", "User registered successfully");
            }
            
            response.put("status", "Success");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (request.getUsername() == null || request.getPassword() == null) {
                response.put("status", "Error");
                response.put("message", "Username and password are required");
                return ResponseEntity.badRequest().body(response);
            }

            Authentication auth = authMgr.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());

            String token = jwtUtil.generateToken(userDetails, claims);

            response.put("status", "Success");
            response.put("token", token);
            response.put("username", userDetails.getUsername());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            response.put("status", "Error");
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
