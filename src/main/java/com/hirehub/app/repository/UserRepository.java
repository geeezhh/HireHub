package com.hirehub.app.repository;

import com.hirehub.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (for login)
    Optional<User> findByEmail(String email);

    /** Matches stored email regardless of case (fixes accounts created before emails were normalized). */
    Optional<User> findByEmailIgnoreCase(String email);

    // Check if email already exists (for registration)
    boolean existsByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    // Get all users by role
    List<User> findByRole(String role);
}