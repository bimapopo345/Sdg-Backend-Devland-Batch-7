package com.example.repository;

import com.example.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Page<User> findByRole(String role, Pageable pageable);
    long countByRole(String role);
    
    // Get user dengan role tertentu
    Page<User> findByRoleOrderByIdAsc(String role, Pageable pageable);
    Page<User> findByRoleOrderByIdDesc(String role, Pageable pageable);
    
    // Check username exist
    boolean existsByUsername(String username);
    
    // Delete by role with native query
    @Modifying
    @Query("DELETE FROM User u WHERE u.role = :role")
    void deleteByRole(@Param("role") String role);
    
    // Count total users by role
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long getRoleCount(@Param("role") String role);
}
