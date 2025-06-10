package com.example.controller;

import com.example.dto.RegisterRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/reset-users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> resetUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Hapus semua user kecuali admin
            userRepository.deleteByRole("ROLE_USER");
            
            response.put("status", "Success");
            response.put("message", "All users have been deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> result = userService.registerAdmin(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAdmin(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            User updatedUser = userService.updateAdmin(request);
            response.put("status", "Success");
            response.put("message", "Admin updated successfully");
            response.put("data", mapUserToResponse(updatedUser));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.deleteAdmin(id);
            response.put("status", "Success");
            response.put("message", "Admin deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            Sort.Direction dir = Sort.Direction.fromString(direction);
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(dir, sort));
            
            Page<User> adminsPage = userRepository.findByRole("ROLE_ADMIN", pageRequest);
            
            List<Map<String, Object>> admins = adminsPage.getContent()
                    .stream()
                    .map(this::mapUserToResponse)
                    .collect(Collectors.toList());

            Map<String, Object> pagination = new HashMap<>();
            pagination.put("page", adminsPage.getNumber());
            pagination.put("size", adminsPage.getSize());
            pagination.put("totalElements", adminsPage.getTotalElements());
            pagination.put("totalPages", adminsPage.getTotalPages());

            response.put("status", "Success");
            response.put("data", admins);
            response.put("pagination", pagination);
            
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
