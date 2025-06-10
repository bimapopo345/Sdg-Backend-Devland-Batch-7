package com.example.controller;

import com.example.dto.RegisterRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserProfile() {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserProfile(auth.getName());
            
            response.put("status", "Success");
            response.put("data", mapUserToResponse(user));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/user/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserProfile(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User updatedUser = userService.updateUserProfile(auth.getName(), request);
            
            response.put("status", "Success");
            response.put("message", "Profile updated successfully");
            response.put("data", mapUserToResponse(updatedUser));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            Sort.Direction dir = Sort.Direction.fromString(direction);
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(dir, sort));
            
            Page<User> usersPage = userRepository.findByRole("ROLE_USER", pageRequest);
            
            List<Map<String, Object>> users = usersPage.getContent()
                    .stream()
                    .map(this::mapUserToResponse)
                    .collect(Collectors.toList());

            Map<String, Object> pagination = new HashMap<>();
            pagination.put("page", usersPage.getNumber());
            pagination.put("size", usersPage.getSize());
            pagination.put("totalElements", usersPage.getTotalElements());
            pagination.put("totalPages", usersPage.getTotalPages());

            response.put("status", "Success");
            response.put("data", users);
            response.put("pagination", pagination);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/admin/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminUpdateUser(
            @PathVariable Integer id,
            @RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.updateUserProfile(
                    userRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("User not found"))
                            .getUsername(),
                    request
            );
            
            response.put("status", "Success");
            response.put("message", "User updated successfully");
            response.put("data", mapUserToResponse(user));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/admin/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminDeleteUser(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            if ("ROLE_ADMIN".equals(user.getRole())) {
                throw new RuntimeException("Cannot delete admin user through this endpoint");
            }
            
            userRepository.deleteById(id);
            
            response.put("status", "Success");
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    private Map<String, Object> mapUserToResponse(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("name", user.getName());
        userMap.put("username", user.getUsername());
        userMap.put("phone", user.getPhone());
        userMap.put("role", user.getRole());
        return userMap;
    }
}
