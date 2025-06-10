package com.example.service;

import com.example.dto.RegisterRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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
        user.setRole("ROLE_USER");

        userRepository.save(user);

        result.put("status", "Success");
        result.put("message", "User registered successfully");
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
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);

        result.put("status", "Success");
        result.put("message", "Admin registered successfully");
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

        userRepository.deleteById(id);
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

        return userRepository.save(user);
    }

    public User getUserProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
