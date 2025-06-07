package org.example.userservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dtos.*;
import org.example.userservice.security.JwtUtil;
import org.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    //  Register a new user
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegisterRequestDto request) {
        UserResponseDto registeredUser = userService.registerUser(request);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    //  Login user (returns JWT + user data)
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto request) {
        AuthResponseDto authResponse = userService.loginUser(request);
        return ResponseEntity.ok(authResponse);
    }

    // Get user by ID (requires authentication)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Optional: add role-specific logic if needed
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //  Update user by ID (authenticated)
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @Valid @RequestBody UserUpdateRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // Soft delete user (authenticated)
    @DeleteMapping("/{id}")
    @PreAuthorize("(hasRole('ADMIN') or hasRole('MANAGER')) and isAuthenticated()")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/username/{username}")
    @PreAuthorize("isAuthenticated()") // Requires a valid JWT
    public ResponseEntity<UserResponseDto> getUserByUserName(
            @PathVariable String username,
            @RequestHeader("Authorization") String authHeader, // Extract token
            Authentication authentication // Populated by Spring Security
    ) {
        // (Optional) Verify token manually
        String token = authHeader.substring(7); // Remove "Bearer "
        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        // Extract username from token (alternative to Authentication)
        String tokenUsername = jwtUtil.extractUsername(token);
        if (!username.equals(tokenUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Username mismatch");
        }

        // Fetch user data
        UserResponseDto user = userService.getUserByUserName(username);
        return ResponseEntity.ok(user);
    }
}
