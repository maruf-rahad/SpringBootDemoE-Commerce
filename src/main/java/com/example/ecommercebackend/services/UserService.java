package com.example.ecommercebackend.services;

import com.example.ecommercebackend.models.dto.ChangePasswordRequest;
import com.example.ecommercebackend.models.dto.UpdateProfileRequest;
import com.example.ecommercebackend.models.dto.UserProfileResponse;
import com.example.ecommercebackend.exceptions.BadRequestException;
import com.example.ecommercebackend.exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.models.Users;
import com.example.ecommercebackend.models.enums.Role;
import com.example.ecommercebackend.repositories.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelMapper modelMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    // --- Authentication & User Management ---
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users registerUser(Users user) {
        Users existingUser = usersRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new BadRequestException("Username '" + user.getUsername() + "' is already taken!");
        }

        // Default role if not provided
        if (user.getRole() == null) {
            user.setRole(Role.ROLE_CUSTOMER);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

    public String verifyUserForLogin(Users user) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            throw new BadRequestException("Invalid username or password");
        }
    }

    public Users createAdmin(Users newAdmin) {
        if (usersRepository.findByUsername(newAdmin.getUsername()) != null) {
            throw new BadRequestException("Username '" + newAdmin.getUsername() + "' is already taken!");
        }

        newAdmin.setRole(Role.ROLE_ADMIN);
        newAdmin.setPassword(bCryptPasswordEncoder.encode(newAdmin.getPassword()));

        return usersRepository.save(newAdmin);
    }

    // --- Profile & Password Management ---

    public UserProfileResponse getUserProfile(String username) {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        UserProfileResponse response = modelMapper.map(user, UserProfileResponse.class);
        if (user.getRole() != null) {
            response.setRole(user.getRole().name());
        }
        return response;
    }

    @Transactional
    public UserProfileResponse updateUserProfile(String username, UpdateProfileRequest request) {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAddress() != null) user.setAddress(request.getAddress());

        Users updatedUser = usersRepository.save(user);

        UserProfileResponse response = modelMapper.map(updatedUser, UserProfileResponse.class);
        if (updatedUser.getRole() != null) {
            response.setRole(updatedUser.getRole().name());
        }
        return response;
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found: " + username);
        }

        // Verify current password match using BCryptPasswordEncoder
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password provided is same as old password.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        usersRepository.save(user);
    }
}