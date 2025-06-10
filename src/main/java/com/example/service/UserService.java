package com.example.service;

import com.example.dto.RegisterRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> result = new HashMap<>();

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole("ROLE_USER"); // Pastikan format role konsisten

        userRepository.save(user);
        
        logger.info("User registered successfully: {}", user.getUsername());

        result.put("status", "Success");
        result.put("message", "User registered successfully");
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }

    public Map<String, Object> registerAdmin(RegisterRequest request) {
        Map<String, Object> result = new HashMap<>();

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole("ROLE_ADMIN"); // Pastikan format role konsisten

        userRepository.save(user);
        
        logger.info("Admin registered successfully: {}", user.getUsername());

        result.put("status", "Success");
        result.put("message", "Admin registered successfully");
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }

    public User updateAdmin(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (!existingUser.isPresent()) {
            throw new RuntimeException("Admin not found");
        }

        User user = existingUser.get();
        if (!"ROLE_ADMIN".equals(user.getRole())) {
            throw new RuntimeException("User is not an admin");
        }

        user.setName(request.getName());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setPhone(request.getPhone());
        
        logger.info("Admin updated successfully: {}", user.getUsername());

        return userRepository.save(user);
    }

    public void deleteAdmin(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new RuntimeException("Admin not found");
        }

        if (!"ROLE_ADMIN".equals(user.get().getRole())) {
            throw new RuntimeException("User is not an admin");
        }

        // Check if this is the last admin
        long adminCount = userRepository.count();
        if (adminCount == 1) {
            throw new RuntimeException("Cannot delete the last admin");
        }

        userRepository.deleteById(id);
        logger.info("Admin deleted successfully: ID {}", id);
    }
    
    public User updateUserProfile(String username, RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (!existingUser.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = existingUser.get();
        user.setName(request.getName());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setPhone(request.getPhone());
        
        logger.info("User profile updated successfully: {}", user.getUsername());

        return userRepository.save(user);
    }

    public User getUserProfile(String username) {
        logger.info("Fetching user profile for: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public long countAdmins() {
        return userRepository.countByRole("ROLE_ADMIN");
    }
}
