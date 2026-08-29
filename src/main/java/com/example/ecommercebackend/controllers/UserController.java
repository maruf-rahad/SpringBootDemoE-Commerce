package com.example.ecommercebackend.controllers;

import com.example.ecommercebackend.models.dto.ChangePasswordRequest;
import com.example.ecommercebackend.models.dto.UpdateProfileRequest;
import com.example.ecommercebackend.models.dto.UserProfileResponse;
import com.example.ecommercebackend.models.Users;
import com.example.ecommercebackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // --- Authentication & Registration Endpoints ---

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        return ResponseEntity.ok(userService.verifyUserForLogin(user));
    }

    @PostMapping("/admin/create-admin")
    public ResponseEntity<Users> createAdmin(@RequestBody Users user) {
        return ResponseEntity.ok(userService.createAdmin(user));
    }

    @GetMapping("/admin/all-users")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // --- User Profile Management Endpoints ---

    @GetMapping("/user/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

    @PutMapping("/user/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.updateUserProfile(username, request));
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {
        String username = authentication.getName();
        userService.changePassword(username, request);
        return ResponseEntity.ok("Password updated successfully.");
    }
}