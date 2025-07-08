package org.example.userservice.repository;

import org.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailOrUsername(String email, String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> getUserByUsername(String username);

    boolean existsByIdAndRole(Long managerId, String manager);
}
